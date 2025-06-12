package calendar.view;

import java.time.LocalDateTime;
import java.util.List;

import calendar.model.Event;

/**
 * This interface handles all user outputs in the calendar.
 * It displays welcome messages, help menus, and formatted event details.
 */
public interface ICalendarView {

  /**
   * Displays the welcome message.
   */
  void displayWelcomeMessage();

  /**
   * Displays a list of all available commands for creating, editing,
   * and managing events.
   */
  void displayHelp();

  /**
   * Displays a list of events.
   *
   * @param events the list of events
   */
  void displayEvents(List<Event> events);

  /**
   * Displays a single event.
   *
   * @param event the event.
   */
  void displayEvent(Event event);

  /**
   * Formats a LocalDateTime into a string.
   *
   * @param dateTime the date and time
   * @return the formatted string
   */
  String formatDateTime(LocalDateTime dateTime);
}
