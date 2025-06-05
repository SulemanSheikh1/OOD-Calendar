package calendar;

import java.time.LocalDateTime;
import java.util.UUID;

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
   * @return the start as a LocalDateTime
   */
  LocalDateTime getStart();

  /**
   * Gets the end date and time of the event.
   *
   * @return the end as a LocalDateTime
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
   * Returns true if the event is public, false if it is private.
   *
   * @return boolean indicating public/private status
   */
  boolean isPublic();

  /**
   * Converts the event to a string for display.
   *
   * @return the event information as a String
   */
  String toString();

  /**
   * Returns the raw status string of this event ("public" or "private").
   *
   * @return the status as a String
   */
  String getStatus();

  /**
   * If non-null, this event is part of a recurring series. All events in that
   * recurring group share the same UUID.
   *
   * @return the seriesId (UUID), or null if this is not part of a series
   */
  UUID getSeriesId();

  /**
   * Sets the seriesId for this event. If non-null, this event belongs to
   * that recurring series. If null, this event is treated as a standalone.
   *
   * @param seriesId the UUID of the series (or null to indicate no series)
   */
  void setSeriesId(UUID seriesId);

  void setSubject(String subject);

  void setStart(LocalDateTime start);

  void setEnd(LocalDateTime end);

  void setLocation(String location);

  void setDescription(String description);

  void setPublic(boolean isPublic);

  boolean isSame(Event event);
}
