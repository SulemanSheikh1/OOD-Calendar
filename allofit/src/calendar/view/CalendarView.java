package calendar.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import calendar.model.Event;
import calendar.model.IEvent;

/**
 * This class handles all user outputs in the calendar.
 * It displays welcome messages, help menus, and formatted event details.
 */
public class CalendarView implements ICalendarView {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  /**
   * Displays the welcome message.
   */
  public void displayWelcomeMessage() {
    System.out.println("Welcome to Calendar Application");
    System.out.println("Type 'help' to see available commands");
  }

  /**
   * Displays a list of all available commands for creating, editing,
   * and managing events.
   */
  public void displayHelp() {
    System.out.println("\nAvailable Commands:");
    System.out.println("CREATE EVENTS:");
    System.out.println("  create event <subject> from <start> to <end>");
    System.out.println(
            "  create event <subject> from <start> to <end> repeats <weekdays> for <N> times");
    System.out.println(
            "  create event <subject> from <start> to <end> repeats <weekdays> until <date>");
    System.out.println("  create event <subject> on <date>");
    System.out.println("  create event <subject> on <date> repeats <weekdays> for <N> times");
    System.out.println("  create event <subject> on <date> repeats <weekdays> until <date>");

    System.out.println("\nEDIT EVENTS:");
    System.out.println("  edit event <property> <subject> from <start> to <end> with <newValue>");
    System.out.println("  edit events <property> <subject> from <start> with <newValue>");
    System.out.println("  edit series <property> <subject> from <start> with <newValue>");

    System.out.println("\nQUERIES:");
    System.out.println("  print events on <date>");
    System.out.println("  print events from <start> to <end>");
    System.out.println("  show status on <dateTime>");

    System.out.println("\nOTHER:");
    System.out.println("  help - Show this help message");
    System.out.println("  exit - Exit the application");
  }

  /**
   * Displays a list of events.
   *
   * @param events the list of events
   */
  public void displayEvents(List<Event> events) {
    if (events.isEmpty()) {
      System.out.println("No events found");
      return;
    }

    System.out.println("\nEvents:");
    for (Event event : events) {
      System.out.println(formatEvent(event));
    }
  }

  /**
   * Displays a single event.
   *
   * @param event the event.
   */
  public void displayEvent(Event event) {
    System.out.println("\nEvent details:");
    System.out.println(formatEvent(event));
  }

  /**
   * Returns a string describing an event, which shows the subject,
   * time range, location, description, and status.
   *
   * @param event the event.
   * @return a string of event details
   */
  private String formatEvent(IEvent event) {
    StringBuilder sb = new StringBuilder();
    sb.append("• \"").append(event.getSubject()).append("\"");
    sb.append(" (").append(formatDateTime(event.getStart()));
    sb.append(" - ").append(formatDateTime(event.getEnd())).append(")");

    if (!event.getLocation().isEmpty()) {
      sb.append(" @ ").append(event.getLocation());
    }

    if (!event.getDescription().isEmpty()) {
      sb.append("\n  Description: ").append(event.getDescription());
    }

    if (!event.getStatus().isEmpty()) {
      sb.append("\n  Status: ").append(event.getStatus());
    }

    return sb.toString();
  }

  /**
   * Formats a LocalDateTime into a string.
   *
   * @param dateTime the date and time
   * @return the formatted string
   */
  public String formatDateTime(LocalDateTime dateTime) {
    return dateTime.format(DATE_TIME_FORMATTER);
  }

}
