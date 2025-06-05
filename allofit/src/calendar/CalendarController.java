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

public class CalendarController {
  private final CalendarModel model;
  private final CalendarView view;
  private final Scanner scanner;
  private boolean running;

  public CalendarController(CalendarModel model, CalendarView view) {
    this.model = model;
    this.view = view;
    this.scanner = new Scanner(System.in);
    this.running = true;
  }

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

  public void processCommand(String command) {
    try {
      if (command.startsWith("create event")) {
        handleCreateEvent(command);
      } else if (command.startsWith("edit")) {
        handleEditEvent(command);
      } else if (command.startsWith("print events")) {
        handlePrintEvents(command);
      } else if (command.startsWith("show status")) {
        handleShowStatus(command);
      } else {
        System.out.println("Error: Unknown command. Type 'help' for available commands.");
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (DateTimeParseException e) {
      System.out.println("Error: Invalid date/time format");
    }
  }

  private void handleCreateEvent(String command) {
    if (command.contains(" on ")) {
      handleCreateAllDayEvent(command);
    } else if (command.contains(" from ")) {
      handleCreateTimedEvent(command);
    } else {
      throw new IllegalArgumentException("Invalid create event command");
    }
  }

  private void handleCreateAllDayEvent(String command) {
    String remaining = command.substring("create event ".length());
    String subject = extractQuotedSubject(remaining);
    remaining = remaining.substring(subject.length()).trim();

    if (!remaining.startsWith("on ")) {
      throw new IllegalArgumentException("Missing 'on' in all-day event creation");
    }

    remaining = remaining.substring(3).trim(); // after "on "

    String[] parts = remaining.split(" repeats ", 2);
    String dateStr = parts[0].trim();
    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

    LocalDateTime start = date.atTime(8, 0);
    LocalDateTime end = date.atTime(17, 0);

    if (parts.length > 1) {
      handleRecurringAllDayEvent(subject, start, end, parts[1]);
    } else {
      Event event = new Event(subject, start, end);
      model.addEvent(event);
      System.out.println("Created all-day event: " + subject);
    }
  }

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
      model.addEvent(event);
      System.out.println("Created timed event: " + subject);
    }
  }

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

  private void handleRecurringAllDayEvent(String subject, LocalDateTime start, LocalDateTime end, String commandPart) {
    String[] repeatParts = commandPart.trim().split(" ");

    if (repeatParts.length < 3) {
      throw new IllegalArgumentException("Invalid recurring event format. Expected: <weekdays> for/until <value>");
    }

    Set<DayOfWeek> days = parseWeekdays(repeatParts[0].toUpperCase());

    if (repeatParts[1].equals("for")) {
      handleRecurringEventHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring all-day event series: " + subject);
    } else if (repeatParts[1].equals("until")) {
      handleRecurringEventSeriesHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring all-day event series: " + subject);
    } else {
      throw new IllegalArgumentException("Invalid recurring event specification. Must include 'for' or 'until'.");
    }
  }

  private void handleRecurringEventSeriesHelper(String subject, LocalDateTime start, LocalDateTime end, String[] repeatParts, Set<DayOfWeek> days) {
    LocalDate untilDate = LocalDate.parse(repeatParts[2], DateTimeFormatter.ISO_DATE);
    EventSeries series = new EventSeries(subject, start, end, days, untilDate);
    List<Event> events = series.getEvents();
    for (Event event : events) {
      model.addEvent(event);
    }
  }

  private void handleRecurringEventHelper(String subject, LocalDateTime start, LocalDateTime end, String[] repeatParts, Set<DayOfWeek> days) {
    int count = Integer.parseInt(repeatParts[2]);
    EventSeries series = new EventSeries(subject, start, end, days, count);
    List<Event> events = series.getEvents();
    for (Event event : events) {
      model.addEvent(event);
    }
  }

  private void handleRecurringTimedEvent(String subject, LocalDateTime start, LocalDateTime end, String commandPart) {
    String[] repeatParts = commandPart.trim().split(" ");

    if (repeatParts.length < 3) {
      throw new IllegalArgumentException("Invalid recurring event format. Expected: <weekdays> for/until <value>");
    }

    // Only take the first token, and force it to uppercase
    String weekdayStr = repeatParts[0].toUpperCase();
    Set<DayOfWeek> days = parseWeekdays(weekdayStr);

    if (repeatParts[1].equals("for")) {
      handleRecurringEventHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring timed event series: " + subject);
    } else if (repeatParts[1].equals("until")) {
      handleRecurringEventSeriesHelper(subject, start, end, repeatParts, days);
      System.out.println("Created recurring timed event series: " + subject);
    } else {
      throw new IllegalArgumentException("Invalid recurring event specification. Must include 'for' or 'until'.");
    }
  }

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

  private LocalDateTime parseDateTime(String dateTimeStr) {
    if (dateTimeStr.contains("T")) {
      return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    } else {
      return LocalDate.parse(dateTimeStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
    }
  }

  private void handleEditEvent(String command) {
    String[] parts = command.split(" ");
    if (parts.length < 2) {
      throw new IllegalArgumentException("Invalid edit command");
    }

    String editType = parts[0]; // edit, edits, series
    String property = parts[1];
    String remaining = command.substring(editType.length() + property.length() + 2).trim();

    String subject = extractQuotedSubject(remaining);
    remaining = remaining.substring(subject.length()).trim();

    boolean hasEnd = remaining.contains(" to ");
    if (!hasEnd && editType.equals("edit")) {
      throw new IllegalArgumentException("Missing 'to' for single event edit");
    }

    String startStr = remaining.substring(remaining.indexOf("from ") + 5,
            hasEnd ? remaining.indexOf(" to ") : remaining.indexOf(" with ")).trim();
    LocalDateTime startTime = parseDateTime(startStr);

    LocalDateTime endTime = null;
    if (hasEnd) {
      String endStr = remaining.substring(remaining.indexOf(" to ") + 4, remaining.indexOf(" with ")).trim();
      endTime = parseDateTime(endStr);
    }

    if (!remaining.contains(" with ")) {
      throw new IllegalArgumentException("Missing 'with' in edit command");
    }

    String newValue = remaining.substring(remaining.indexOf(" with ") + 6).trim();

    List<IEvent> candidates = model.getEventsWithinDates(startTime.minusMinutes(1), startTime.plusHours(1));
    IEvent matchingEvent = null;

    for (IEvent e : candidates) {
      boolean match = e.getSubject().equals(subject.replace("\"", "")) && e.getStart().equals(startTime);
      if (hasEnd) {
        match = match && e.getEnd().equals(endTime);
      }
      if (match) {
        matchingEvent = e;
        break;
      }
    }

    if (matchingEvent == null) {
      throw new IllegalArgumentException("No matching event found");
    }

    switch (editType) {
      case "edit":
        editSingleEvent(matchingEvent, property, newValue);
        break;
      case "edits":
        editFutureEvents(matchingEvent, property, newValue);
        break;
      case "series":
        editWholeSeries(matchingEvent, property, newValue);
        break;
      default:
        throw new IllegalArgumentException("Invalid edit type");
    }
  }

  private void editSingleEvent(IEvent event, String property, String newValue) {
    Event modified = createModifiedEvent(event, property, newValue);
    model.removeEvent(event);
    model.addEvent(modified);
    System.out.println("Edited single event");
  }

  private void editFutureEvents(IEvent event, String property, String newValue) {
    if (!event.isEvent()) {
      throw new IllegalArgumentException("Can only edit Event instances");
    }

    Event base = (Event) event;
    List<IEvent> all = model.getEventsWithinDates(base.getStart().minusDays(1), LocalDateTime.MAX);

    boolean foundStart = false;
    int count = 0;

    for (IEvent e : all) {
      if (e.isSame(base)) {
        foundStart = true;
      }
      if (foundStart && e.getSubject().equals(base.getSubject())) {
        Event modified = createModifiedEvent(e, property, newValue);
        model.removeEvent(e);
        model.addEvent(modified);
        count++;
      }
    }

    System.out.println("Modified " + count + " future events in series");
  }

  private void editWholeSeries(IEvent event, String property, String newValue) {
    if (!event.isEvent()) {
      throw new IllegalArgumentException("Can only edit Event instances");
    }

    Event base = (Event) event;
    List<IEvent> all = model.getEventsWithinDates(LocalDateTime.MIN, LocalDateTime.MAX);

    int count = 0;
    for (IEvent e : all) {
      if (e.getSubject().equals(base.getSubject())) {
        Event modified = createModifiedEvent(e, property, newValue);
        model.removeEvent(e);
        model.addEvent(modified);
        count++;
      }
    }

    System.out.println("Modified " + count + " events in entire series");
  }

  private Event createModifiedEvent(IEvent original, String property, String newValue) {
    String subject = original.getSubject();
    LocalDateTime start = original.getStart();
    LocalDateTime end = original.getEnd();
    String location = original.getLocation();
    String description = original.getDescription();
    String status = original.getStatus();

    switch (property) {
      case "subject":
        subject = newValue;
        break;
      case "start":
        start = parseDateTime(newValue);
        break;
      case "end":
        end = parseDateTime(newValue);
        break;
      case "location":
        location = newValue;
        break;
      case "description":
        description = newValue;
        break;
      case "status":
        status = newValue;
        break;
      default:
        throw new IllegalArgumentException("Invalid property: " + property);
    }

    return new Event(subject, start, end, location, description, status);
  }
}