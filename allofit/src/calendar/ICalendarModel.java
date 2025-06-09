package calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for the calendar.
 * For adding, removing.
 */
public interface ICalendarModel {

  /**
   * Adds an event to the calendar.
   *
   * @param event the event
   * @throws IllegalArgumentException if the event is a duplicate
   */
  void addEvent(IEvent event);

  /**
   * Removes an event from the calendar.
   *
   * @param event the event
   * @throws IllegalArgumentException if the event is not found
   */
  void removeEvent(IEvent event);

  /**
   * Finds event through subject and start time.
   *
   * @param subject the subject of the event
   * @param start the start time
   * @return the matching event
   */
  IEvent findEvent(String subject, LocalDateTime start);

  /**
   * Gets all events on that certain date.
   *
   * @param date the date
   * @return a list of matching events
   */
  List<IEvent> getEventsOnDate(LocalDate date);

  /**
   * Gets all events within the range of dates.
   *
   * @param beginning the start of the range
   * @param ending the end of the range
   * @return a list of matching events
   */
  List<IEvent> getEventsWithinDates(LocalDateTime beginning, LocalDateTime ending);

  /**
   * Checks if the calendar has an event at that time.
   *
   * @param time the date/time to check
   * @return true if an event overlaps with this time, false otherwise
   */
  boolean isBusy(LocalDateTime time);

  /**
   * Checks whether an event would have the same subject,
   * start, and end time, with existing events.
   *
   * @param e the Event to test
   * @return true if a conflict exists, false otherwise
   */
  boolean hasConflict(Event e);
}
