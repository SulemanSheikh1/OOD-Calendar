package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a library of calendars, with their own timezone.
 * Allows for creating, selecting, editing, and deleting calendars.
 */
public class CalendarLibrary implements ICalendarLibrary {
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

  public boolean copyEventToCalendar(String subject, LocalDateTime start, String targetCal, LocalDateTime dest) {
    if (currentCalendar == null || !calendars.containsKey(targetCal)) {
      return false;
    }

    CalendarModel source = getActiveCalendar();
    CalendarModel target = calendars.get(targetCal);
    ZoneId sourceZone = getActiveTimezone();
    ZoneId targetZone = calendarTimezones.get(targetCal);

    IEvent original = source.findEvent(subject, start);
    if (original == null) {
      return false;
    }

    IEvent shifted = original.copyWithNewTime(dest.atZone(targetZone).withZoneSameInstant(sourceZone).toLocalDateTime());
    if (target.hasConflict((Event) shifted)) {
      return false;
    }

    target.addEvent(shifted);
    return true;
  }

  public int copyEventsOnDateToCalendar(LocalDate srcDate, String targetCal, LocalDate destDate) {
    if (currentCalendar == null || !calendars.containsKey(targetCal)) {
      return 0;
    }

    CalendarModel source = getActiveCalendar();
    CalendarModel target = calendars.get(targetCal);
    ZoneId sourceZone = getActiveTimezone();
    ZoneId targetZone = calendarTimezones.get(targetCal);

    List<IEvent> events = source.getEventsOnDate(srcDate);
    int copied = 0;

    for (IEvent e : events) {
      long hour = e.getStart().getHour();
      long minute = e.getStart().getMinute();
      LocalDateTime destStart = destDate.atTime((int) hour, (int) minute);

      IEvent shifted = e.copyWithNewTime(destStart.atZone(sourceZone).withZoneSameInstant(targetZone).toLocalDateTime());
      if (!target.hasConflict((Event) shifted)) {
        target.addEvent(shifted);
        copied++;
      }
    }

    return copied;
  }

  public int copyEventsBetweenDatesToCalendar(LocalDateTime start, LocalDateTime end, String targetCal, LocalDateTime destStart) {
    if (currentCalendar == null || !calendars.containsKey(targetCal)) {
      return 0;
    }

    CalendarModel source = getActiveCalendar();
    CalendarModel target = calendars.get(targetCal);
    ZoneId sourceZone = getActiveTimezone();
    ZoneId targetZone = calendarTimezones.get(targetCal);

    List<IEvent> events = source.getEventsWithinDates(start, end);
    int copied = 0;

    for (IEvent e : events) {
      long shiftMinutes = java.time.Duration.between(start, e.getStart()).toMinutes();
      LocalDateTime shiftedStart = destStart.plusMinutes(shiftMinutes);

      IEvent newEvent = e.copyWithNewTime(shiftedStart.atZone(sourceZone).withZoneSameInstant(targetZone).toLocalDateTime());

      if (!target.hasConflict((Event) newEvent)) {
        target.addEvent(newEvent);
        copied++;
      }
    }

    return copied;
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
