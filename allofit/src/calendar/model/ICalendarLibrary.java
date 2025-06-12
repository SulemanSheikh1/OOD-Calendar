package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

/**
 * Interface for a library of named calendars, each with its own timezone.
 * Provides methods to create, select, edit, delete, list, and copy events across calendars.
 */
public interface ICalendarLibrary {

  /**
   * Creates a new calendar with the given unique name and timezone ID.
   *
   * @param name            the unique name for the calendar
   * @param timezoneString  the IANA timezone ID (e.g. "America/New_York")
   * @throws IllegalArgumentException if the name already exists or the timezone is invalid
   */
  void createCalendar(String name, String timezoneString);

  /**
   * Sets the active calendar by name.
   *
   * @param name the name of the calendar to use
   * @throws IllegalArgumentException if no calendar with the given name exists
   */
  void useCalendar(String name);

  /**
   * Returns the currently active calendar model.
   *
   * @return the CalendarModel in use
   * @throws IllegalStateException if no calendar has been selected
   */
  CalendarModel getActiveCalendar();

  /**
   * Returns the timezone of the currently active calendar.
   *
   * @return the ZoneId of the active calendar
   * @throws IllegalStateException if no calendar has been selected
   */
  ZoneId getActiveTimezone();

  /**
   * Edits a calendar's name or timezone.
   *
   * @param name      the existing calendar name
   * @param property  either "name" or "timezone"
   * @param newValue  the new name or timezone ID
   * @throws IllegalArgumentException if the calendar doesn't exist,
   *         the new name already exists, or the timezone is invalid
   */
  void editCalendar(String name, String property, String newValue);

  /**
   * Deletes a calendar by name. If it was the active calendar, clears the active selection.
   *
   * @param name the name of the calendar to delete
   * @throws IllegalArgumentException if no calendar with the given name exists
   */
  void deleteCalendar(String name);

  boolean copyEventToCalendar(String subject, LocalDateTime start, String targetCal, LocalDateTime dest);
  int copyEventsOnDateToCalendar(LocalDate srcDate, String targetCal, LocalDate destDate);
  int copyEventsBetweenDatesToCalendar(LocalDateTime start, LocalDateTime end, String targetCal, LocalDateTime destStart);


  /**
   * Lists all existing calendar names.
   *
   * @return a set of all calendar names
   */
  Set<String> listCalendars();

  /**
   * Returns the name of the currently active calendar, or null if none is active.
   *
   * @return the current calendar name
   */
  String getCurrentCalendarName();
}
