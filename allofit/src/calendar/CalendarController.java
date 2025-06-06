package calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

/**
 * Class that represents the controller for the calendar.
 */
public class CalendarController {
  private final CalendarModel model;
  private final CalendarView view;
  private final Scanner scanner = new Scanner(System.in);
  private boolean running;
  private static final DateTimeFormatter DATE_TIME_FORMAT =
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  public CalendarController(CalendarModel model, CalendarView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Public entry‐point used by tests
   * It delegates to handleCommand but only catches exceptions
   * from truly unknown commands.  All other IllegalArgumentExceptions
   * are rethrown.
   *
   * @param command the raw command line from the user or test
   */
  public void processCommand(String command) {
    try {
      handleCommand(command);
    } catch (IllegalArgumentException e) {
      if (e.getMessage() != null && e.getMessage().startsWith("Unrecognized command:")) {
        System.out.println("Unknown command: " + command);
      } else {
        throw e;
      }
    }
  }



  /**
   * Runs the calendar in interactive mode, reading commands from stdin.
   */
  public void runInteractive() {
    Scanner sc = new Scanner(System.in);
    while (true) {
      System.out.print("> ");
      String line = sc.nextLine().trim();
      if (line.equalsIgnoreCase("exit")) {
        break;
      }
      try {
        processCommand(line);
      } catch (IllegalArgumentException e) {
        // should never run
      }
    }
    sc.close();
  }

  /**
   * Starts the interactive mode for user input in a loop.
   * Displays the welcome message and waits for user commands.
   */
  public void startInteractiveMode() {
    view.displayWelcomeMessage();
    while (running) {
      System.out.print("\n> ");
      String command = scanner.nextLine().trim();
      if (command.equalsIgnoreCase("exit")) {
        running = false;
        continue;
      }
      if (command.equalsIgnoreCase("help")) {
        view.displayHelp();
        continue;
      }
      processCommand(command);
    }
  }

  /**
   * Parses a single line of input and dispatches to create/edit/print/show.
   *
   * @param command the raw command string from the user
   */
  private void handleCommand(String command) {
    if (command.isEmpty()) {
      return;
    }

    String lower = command.toLowerCase();
    if (lower.startsWith("create")) {
      handleCreateEvent(command);
    } else if (lower.startsWith("edit ")) {
      handleEditEvent(command);
    } else if (lower.startsWith("print ")) {
      handlePrintEvents(command);
    } else if (lower.startsWith("show status")) {
      handleShowStatus(command);
    } else {
      throw new IllegalArgumentException("Unrecognized command: " + command);
    }
  }

  /**
   * Handles commands that start with "create event".
   * Figures out if it’s a timed event or all-day event.
   *
   * @param command the full "create event" command
   */
  private void handleCreateEvent(String command) {
    if (command.contains(" on ")) {
      handleCreateAllDayEvent(command);
    } else if (command.contains(" from ")) {
      handleCreateTimedEvent(command);
    } else {
      throw new IllegalArgumentException("Invalid create event command");
    }
  }

  /**
   * Creates an all-day event from 8:00 to 17:00.
   * Can also create recurring all-day events.
   *
   * @param command the input string for creating an all-day event
   */
  private void handlePrintEvents(String command) {
    if (command.contains(" on ")) {
      String dateStr = command.substring(command.indexOf(" on ") + 4).trim();
      LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
      List<IEvent> events = model.getEventsOnDate(date);
      List<Event> eventList = new ArrayList<>();
      for (IEvent event : events) {
        eventList.add((Event) event);
      }
      view.displayEvents(eventList);
    } else if (command.contains(" from ")) {
      String range = command.substring(command.indexOf(" from ") + 6);
      String[] parts = range.split(" to ");
      LocalDateTime start = parseDateTime(parts[0].trim());
      LocalDateTime end = parseDateTime(parts[1].trim());
      List<IEvent> events = model.getEventsWithinDates(start, end);
      List<Event> eventList = new ArrayList<>();
      for (IEvent event : events) {
        eventList.add((Event) event);
      }
      view.displayEvents(eventList);
    }
  }

  /**
   * Creates a timed event with specific start and end times.
   * Can also handle recurring timed events.
   *
   * @param command the input string for creating a timed event
   */
  private void handleCreateTimedEvent(String command) {
    String remaining = command.substring("create event ".length());
    String subject = extractQuotedSubject(remaining);
    remaining = remaining.substring(subject.length()).trim();

    if (!remaining.startsWith("from ")) {
      throw new IllegalArgumentException("Missing 'from' in timed event creation");
    }

    String[] parts = remaining.substring(5).split(" to ");
    if (parts.length < 2) {
      throw new IllegalArgumentException("Missing 'to' in timed event creation");
    }

    LocalDateTime start = parseDateTime(parts[0].trim());
    String endPart = parts[1].trim();

    String[] endParts = endPart.split(" repeats ", 2);
    LocalDateTime end = parseDateTime(endParts[0].trim());

    if (endParts.length > 1) {
      handleRecurringTimedEvent(subject, start, end, endParts[1]);
    } else {
      Event event = new Event(subject, start, end);
      if (model.hasConflict(event)) {
        throw new IllegalArgumentException("Cannot create duplicate event.");
      }
      model.addEvent(event);
      System.out.println("Created timed event: " + subject);
    }
  }

  /**
   * Handles the availability status.
   *
   * @param command the full "show status" command with datetime
   */
  private void handleShowStatus(String command) {
    String dateTimeStr = command.substring(command.indexOf(" on ") + 4).trim();
    LocalDateTime dateTime = parseDateTime(dateTimeStr);
    boolean isBusy = model.isBusy(dateTime);
    if (isBusy) {
      System.out.println("busy");
    } else {
      System.out.println("available");
    }
  }

  /**
   * Handles recurring timed events.
   */
  private void handleRecurringTimedEvent(String subject, LocalDateTime start,
                                         LocalDateTime end, String commandPart) {
    String[] repeatParts = commandPart.trim().split(" ");

    if (repeatParts.length < 3) {
      throw new IllegalArgumentException("Invalid recurring event format. " +
              "Expected: <weekdays> for/until <value>");
    }

    String weekdayStr = repeatParts[0].toUpperCase();
    Set<DayOfWeek> days = parseWeekdays(weekdayStr);

    if (repeatParts[1].equals("for")) {
      handleRecurringEventHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring timed event series: " + subject);
    } else if (repeatParts[1].equals("until")) {
      handleRecurringEventSeriesHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring timed event series: " + subject);
    } else {
      throw new IllegalArgumentException("Invalid recurring event specification. " +
              "Must include 'for' or 'until'.");
    }
  }

  /**
   * Handles edit commands like editing a single event, future events, or an entire series.
   *
   * @param command the user's edit command
   */
  private void handleEditEvent(String command) {
    String[] parts = command.split(" ");
    if (parts.length < 4) {
      throw new IllegalArgumentException("Invalid edit command. " +
              "Expected: <edit|edits|edit series> <property> <subject> " +
              "from <start> with <newValue>");
    }

    String editType;
    if (parts[0].equalsIgnoreCase("edits")) {
      editType = "edits";
    } else if (parts[0].equalsIgnoreCase("edit") && parts[1].equalsIgnoreCase("series")) {
      editType = "edit series";
    } else if (parts[0].equalsIgnoreCase("edit")) {
      editType = "edit";
    } else {
      throw new IllegalArgumentException("Invalid edit type: " + parts[0]);
    }

    int idx;
    if (editType.equals("edits")) {
      idx = 1;
    } else if (editType.equals("edit series")) {
      idx = 2;
    } else {
      idx = 1;
    }

    String property = parts[idx].toLowerCase();
    if (!property.matches("subject|start|end|location|description|status")) {
      throw new IllegalArgumentException("Invalid property: " + property);
    }
    idx++;

    String eventSubject;
    if (parts[idx].startsWith("\"")) {
      StringBuilder sbSubject = new StringBuilder();
      String piece = parts[idx];
      if (piece.endsWith("\"") && piece.length() > 1) {
        sbSubject.append(piece, 1, piece.length() - 1);
      } else {
        sbSubject.append(piece.substring(1));
        idx++;
        while (idx < parts.length && !parts[idx].endsWith("\"")) {
          sbSubject.append(" ").append(parts[idx]);
          idx++;
        }
        if (idx >= parts.length) {
          throw new IllegalArgumentException("Missing closing quote for subject.");
        }
        piece = parts[idx];
        sbSubject.append(" ").append(piece, 0, piece.length() - 1);
      }
      eventSubject = sbSubject.toString();
      idx++;
    } else {
      eventSubject = parts[idx];
      idx++;
    }

    if (idx >= parts.length || !parts[idx].equalsIgnoreCase("from")) {
      throw new IllegalArgumentException("Missing 'from' keyword in edit command.");
    }
    idx++;

    if (idx >= parts.length) {
      throw new IllegalArgumentException("Missing start date/time in edit command.");
    }
    LocalDateTime fromDateTime;
    try {
      fromDateTime = LocalDateTime.parse(parts[idx], DATE_TIME_FORMAT);
    } catch (DateTimeParseException ex) {
      throw new IllegalArgumentException("Invalid date/time format; expected YYYY-MM-DDThh:mm");
    }
    idx++;

    if (idx >= parts.length || !parts[idx].equalsIgnoreCase("with")) {
      throw new IllegalArgumentException("Missing 'with' keyword in edit command.");
    }
    idx++;

    StringBuilder sbValue = new StringBuilder();
    for (int i = idx; i < parts.length; i++) {
      sbValue.append(parts[i]);
      if (i < parts.length - 1) {
        sbValue.append(" ");
      }
    }
    String newValue = sbValue.toString();

    IEvent matchingEvent = model.findEvent(eventSubject, fromDateTime);
    if (matchingEvent == null) {
      System.out.println("Error: Event not found.");
      return;
    }

    switch (editType.toLowerCase()) {
      case "edit":
        editSingleEvent(matchingEvent, property, newValue);
        break;
      case "edits":
        editFutureEvents(matchingEvent, property, newValue);
        break;
      case "edit series":
        editWholeSeries(matchingEvent, property, newValue);
        break;
      default:
        throw new IllegalArgumentException("Unrecognized edit type.");
    }
  }

  /**
   * Edits exactly one occurrence (the matchingEvent). If this event is part of a series,
   * other occurrences in that series are unaffected.
   *
   * @param event      the IEvent to change (actually an Event)
   * @param property   which property to modify (subject, start, etc.)
   * @param newValue   new value for the property
   */
  private void editSingleEvent(IEvent event, String property, String newValue) {
    Event base = (Event) event;

    Event modified = createModifiedEvent(base, property, newValue);
    if (model.hasConflict(modified)) {
      System.out.println("Error: Cannot edit event due to a scheduling conflict.");
      return;
    }

    model.removeEvent(base);
    model.addEvent(modified);
    System.out.println("Edited single event.");
  }

  /**
   * Edits this occurrence and all future occurrences in the same series. If the base
   * event is not part of any series (seriesId == null), behaves exactly like editSingleEvent().
   *
   * @param event      the IEvent in the series to start from
   * @param property   which property to modify
   * @param newValue   new value for the property
   */
  private void editFutureEvents(IEvent event, String property, String newValue) {
    Event base = (Event) event;

    UUID seriesId = base.getSeriesId();
    if (seriesId == null) {
      editSingleEvent(event, property, newValue);
      return;
    }

    LocalDateTime baseStart = base.getStart();
    List<IEvent> allAfter = model.getEventsWithinDates(baseStart, LocalDateTime.MAX);
    int count = 0;

    for (IEvent e : allAfter) {
      Event ev = (Event) e;
      if (ev.getSeriesId() != null
              && ev.getSeriesId().equals(seriesId)
              && !ev.getStart().isBefore(baseStart)) {
        Event modified = createModifiedEvent(ev, property, newValue);
        if (!model.hasConflict(modified)) {
          model.removeEvent(ev);
          model.addEvent(modified);
          count++;
        }
      }
    }
    System.out.println("Modified " + count + " future event(s) in the series.");
  }

  /**
   * Edits every occurrence in the same series.
   *
   * @param event      the IEvent in the series
   * @param property   which property to modify
   * @param newValue   new value for the property
   */
  private void editWholeSeries(IEvent event, String property, String newValue) {
    Event base = (Event) event;

    UUID seriesId = base.getSeriesId();
    if (seriesId == null) {
      editSingleEvent(event, property, newValue);
      return;
    }

    List<IEvent> allEvents = model.getEventsWithinDates(LocalDateTime.MIN, LocalDateTime.MAX);
    int count = 0;

    for (IEvent e : allEvents) {
      Event ev = (Event) e;
      if (ev.getSeriesId() != null && ev.getSeriesId().equals(seriesId)) {
        Event modified = createModifiedEvent(ev, property, newValue);
        if (!model.hasConflict(modified)) {
          model.removeEvent(ev);
          model.addEvent(modified);
          count++;
        }
      }
    }
    System.out.println("Modified " + count + " event(s) in the entire series.");
  }

  /**
   * Creates a new Event object by copying all fields of `base`, then changing exactly one property.
   * The returned Event preserves the original seriesId if it was non-null.
   *
   * @param base      the existing Event to copy
   * @param property  which property to change (subject, start, end, location, description, status)
   * @param newValue  the new value for that property (string or date/time text)
   * @return a brand‐new Event reflecting the single change
   * @throws IllegalArgumentException if property is unrecognized or newValue badly formatted
   */
  private Event createModifiedEvent(Event base, String property, String newValue) {
    Event copy = new Event(
                    base.getSubject(),
                    base.getStart(),
                    base.getEnd(),
                    base.getLocation(),
                    base.getDescription(),
                    base.getStatus());

    if (base.getSeriesId() != null) {
      copy.setSeriesId(base.getSeriesId());
    }

    switch (property.toLowerCase()) {
      case "subject":
        copy.setSubject(newValue);
        break;
      case "start":
        LocalDateTime newStart = LocalDateTime.parse(newValue, DATE_TIME_FORMAT);
        copy.setStart(newStart);
        break;
      case "end":
        LocalDateTime newEnd = LocalDateTime.parse(newValue, DATE_TIME_FORMAT);
        copy.setEnd(newEnd);
        break;
      case "location":
        copy.setLocation(newValue);
        break;
      case "description":
        copy.setDescription(newValue);
        break;
      case "status":
        copy.setPublic(newValue.equalsIgnoreCase("public")
                || newValue.equalsIgnoreCase("true"));
        break;
      default:
        throw new IllegalArgumentException("Invalid property: " + property);
    }
    return copy;
  }

  /**
   * Helper to parse a date-time string..
   *
   * @param s the date-time text
   * @return the parsed LocalDateTime
   * @throws DateTimeParseException if the format is invalid
   */
  private LocalDateTime parseDateTime(String s) {
    return LocalDateTime.parse(s, DATE_TIME_FORMAT);
  }

  /**
   * Creates an all-day event from 8:00 to 17:00.
   * Can also create recurring all-day events.
   *
   * @param command the input string for creating an all-day event
   */
  private void handleCreateAllDayEvent(String command) {
    String remaining = command.substring("create event ".length());
    String subject = extractQuotedSubject(remaining);
    remaining = remaining.substring(subject.length()).trim();

    if (!remaining.startsWith("on ")) {
      throw new IllegalArgumentException("Missing 'on' in all-day event creation");
    }

    remaining = remaining.substring(3).trim();

    String[] parts = remaining.split(" repeats ", 2);
    String dateStr = parts[0].trim();
    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

    LocalDateTime start = date.atTime(8, 0);
    LocalDateTime end = date.atTime(17, 0);

    if (parts.length > 1) {
      handleRecurringAllDayEvent(subject, start, end, parts[1]);
    } else {
      Event event = new Event(subject, start, end);
      if (model.hasConflict(event)) {
        throw new IllegalArgumentException("Cannot create duplicate event.");
      }
      model.addEvent(event);
      System.out.println("Created all-day event: " + subject);
    }
  }

  /**
   * Handles the recurring all-day events based on
   * the given subject, start/end time, and repetition.
   *
   * @param subject the event subject
   * @param start the start time of the event
   * @param end the end time of the event
   * @param commandPart the repeat specification string
   */
  private void handleRecurringAllDayEvent(String subject, LocalDateTime start,
                                          LocalDateTime end, String commandPart) {
    String[] repeatParts = commandPart.trim().split(" ");

    if (repeatParts.length < 3) {
      throw new IllegalArgumentException("Invalid recurring event format. " +
              "Expected: <weekdays> for/until <value>");
    }

    Set<DayOfWeek> days = parseWeekdays(repeatParts[0].toUpperCase());

    if (repeatParts[1].equals("for")) {
      handleRecurringEventHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring all-day event series: " + subject);
    } else if (repeatParts[1].equals("until")) {
      handleRecurringEventSeriesHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring all-day event series: " + subject);
    } else {
      throw new IllegalArgumentException("Invalid recurring event specification. " +
              "Must include 'for' or 'until'.");
    }
  }

  /**
   * Helper that creates recurring events for a certain number of times.
   */
  private void handleRecurringEventSeriesHelper(String subject, LocalDateTime start,
                                                LocalDateTime end,
                                                String[] repeatParts, Set<DayOfWeek> days) {
    LocalDate untilDate = LocalDate.parse(repeatParts[2], DateTimeFormatter.ISO_DATE);
    EventSeries series = new EventSeries(subject, start, end, days, untilDate);
    List<Event> events = series.getEvents();
    for (Event event : events) {
      model.addEvent(event);
    }
  }

  /**
   * Helper that creates recurring events that go until a specific date.
   */
  private void handleRecurringEventHelper(String subject, LocalDateTime start, LocalDateTime end,
                                          String[] repeatParts, Set<DayOfWeek> days) {
    int count = Integer.parseInt(repeatParts[2]);
    EventSeries series = new EventSeries(subject, start, end, days, count);
    List<Event> events = series.getEvents();
    for (Event event : events) {
      model.addEvent(event);
    }
  }

  /**
   * Converts weekday letters M = Monday into actual DayOfWeek values.
   *
   * @param weekdayStr the string with weekday letters
   * @return a set of days of the week
   */
  private Set<DayOfWeek> parseWeekdays(String weekdayStr) {
    Set<DayOfWeek> days = new HashSet<>();
    for (char c : weekdayStr.toCharArray()) {
      switch (c) {
        case 'M':
          days.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          days.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          days.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          days.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          days.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          days.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          days.add(DayOfWeek.SUNDAY);
          break;
        default:
          throw new IllegalArgumentException("Invalid weekday character: " + c);
      }
    }
    return days;
  }

  /**
   * Extracts quoted subject name from the input string.
   * And handles unquoted words.
   *
   * @param input the raw input string after "create event"
   * @return the parsed subject string including quotes
   */
  private String extractQuotedSubject(String input) {
    if (input.startsWith("\"")) {
      int endQuote = input.indexOf("\"", 1);
      if (endQuote == -1) {
        throw new IllegalArgumentException("Unclosed quote in subject");
      }
      return input.substring(0, endQuote + 1);
    } else {
      int space = input.indexOf(" ");
      if (space == -1) {
        return input;
      } else {
        return input.substring(0, space);
      }
    }
  }
}