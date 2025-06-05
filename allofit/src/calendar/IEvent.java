package calendar;

import java.time.LocalDateTime;

/**
 * Represents a general event in the calendar.
 * This interface defines the basic information and behaviors all events have.
 */
public interface IEvent {

  /**
   * Gets the subject of the event.
   *
   * @return the subject as a String
   */
  String getSubject();

  /**
   * Gets the start date and time of the event.
   *
   * @return the start time as a LocalDateTime
   */
  LocalDateTime getStart();

  /**
   * Gets the end date and time of the event.
   *
   * @return the end time as a LocalDateTime
   */
  LocalDateTime getEnd();

  /**
   * Gets the location of the event.
   *
   * @return the location as a String
   */
  String getLocation();

  /**
   * Gets the description of the event.
   *
   * @return the description as a String
   */
  String getDescription();

  /**
   * Gets the status of the event.
   *
   * @return the status as a String
   */
  String getStatus();

  /**
   * Checks if this event is the same as another event.
   *
   * @param other another IEvent
   * @return true if they are same event, else false
   */
  boolean isSame(IEvent other);

  /**
   * Converts the event to a string for display.
   *
   * @return the event information as a String
   */
  String toString();

  /**
   * Checks if this is a single event.
   *
   * @return true if it's a single event, false if it's not (series).
   */
  boolean isEvent();
}
