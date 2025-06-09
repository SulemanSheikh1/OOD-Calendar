package calendar;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a library of calendars, with their own timezone.
 * Allows for creating, selecting, editing, and deleting calendars.
 */
public class CalendarLibrary {
  private final Map<String, CalendarModel> calendars;
  private final Map<String, ZoneId> calendarTimezones;
  private String currentCalendar;

  /**
   * Constructs an empty CalendarLibrary.
   */
  public CalendarLibrary() {
    this.calendars = new HashMap<>();
    this.calendarTimezones = new HashMap<>();
    this.currentCalendar = null;
  }

  /**
   * Creates a new calendar with the name and timezone.
   *
   * @param name the unique name for the calendar
   * @param timezoneString the timezone ID
   * @throws IllegalArgumentException if the name already exists or the timezone is invalid
   */
  public void createCalendar(String name, String timezoneString) {
    if (calendars.containsKey(name)) {
      throw new IllegalArgumentException("Calendar name already exists.");
    }

    ZoneId zone;
    try {
      zone = ZoneId.of(timezoneString);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid timezone: " + timezoneString);
    }

    calendars.put(name, new CalendarModel());
    calendarTimezones.put(name, zone);
  }

  /**
   * Sets the active calendar by name.
   *
   * @param name the name of the calendar to use
   * @throws IllegalArgumentException if the calendar does not exist
   */
  public void useCalendar(String name) {
    if (!calendars.containsKey(name)) {
      throw new IllegalArgumentException("No such calendar exists.");
    }
    this.currentCalendar = name;
  }

  /**
   * Returns the currently active calendar.
   *
   * @return the current CalendarModel
   * @throws IllegalStateException if no calendar is currently in use
   */
  public CalendarModel getActiveCalendar() {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar in use.");
    }
    return calendars.get(currentCalendar);
  }

  /**
   * Returns the timezone of the active calendar.
   *
   * @return the ZoneId of the current calendar
   * @throws IllegalStateException if no calendar is currently in use
   */
  public ZoneId getActiveTimezone() {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar in use.");
    }
    return calendarTimezones.get(currentCalendar);
  }

  /**
   * Edits a calendar's name or timezone.
   *
   * @param name the name of the calendar to edit
   * @param property either "name" or "timezone"
   * @param newValue the new name or timezone ID
   * @throws IllegalArgumentException if the calendar doesn't exist,
   *         the new name already exists, or the timezone is invalid
   */
  public void editCalendar(String name, String property, String newValue) {
    if (!calendars.containsKey(name)) {
      throw new IllegalArgumentException("Calendar not found.");
    }

    if (property.equalsIgnoreCase("name")) {
      if (calendars.containsKey(newValue)) {
        throw new IllegalArgumentException("New calendar name already exists.");
      }
      calendars.put(newValue, calendars.remove(name));
      ZoneId zone = calendarTimezones.remove(name);
      calendarTimezones.put(newValue, zone);
      if (name.equals(currentCalendar)) {
        currentCalendar = newValue;
      }
    } else if (property.equalsIgnoreCase("timezone")) {
      ZoneId zone;
      try {
        zone = ZoneId.of(newValue);
      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid timezone: " + newValue);
      }
      calendarTimezones.put(name, zone);
    } else {
      throw new IllegalArgumentException("Unsupported calendar property: " + property);
    }
  }

  /**
   * Deletes a calendar by name. If it is the active calendar, it is unset.
   *
   * @param name the name of the calendar to delete
   * @throws IllegalArgumentException if the calendar does not exist
   */
  public void deleteCalendar(String name) {
    if (!calendars.containsKey(name)) {
      throw new IllegalArgumentException("Calendar not found: " + name);
    }
    calendars.remove(name);
    calendarTimezones.remove(name);
    if (name.equals(currentCalendar)) {
      currentCalendar = null;
    }
  }

  /**
   * Returns the set of all calendar names.
   *
   * @return a set of all existing calendar names
   */
  public Set<String> listCalendars() {
    return calendars.keySet();
  }

  /**
   * Returns the name of the currently active calendar.
   *
   * @return the name of the current calendar, or null if none is active
   */
  public String getCurrentCalendarName() {
    return currentCalendar;
  }
}
