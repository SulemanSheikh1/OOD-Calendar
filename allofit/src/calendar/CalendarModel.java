package calendar;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The storing and managing of all calendar events.
 */
public class CalendarModel {
  private final List<IEvent> events;

  /**
   * Constructs an empty calendar.
   */
  public CalendarModel() {
    events = new ArrayList<>();
  }

  /**
   * Add events to the calendar.
   *
   * @param event represents another event to add to the list.
   */
  public void addEvent(IEvent event) {
    for (int i = 0; i < events.size(); i++) {
      IEvent currEvent = events.get(i);

      if (currEvent.isSame((Event) event)) {
        throw new IllegalArgumentException("Cannot add two events with the same subject");
      }
    }
    events.add(event);
  }

  /**
   * removes events from the calendar.
   *
   * @param event represents another event to remove from the list.
   */
  public void removeEvent(IEvent event) {
    for (int i = 0; i < events.size(); i++) {
      IEvent currEvent = events.get(i);
      if (currEvent.isSame((Event) event)) {
        events.remove(i);
        return;
      }
    }
    throw new IllegalArgumentException("Event not found in calendar");
  }

  /**
   * Finds and returns an event with the given subject and start time.
   * Returns null if no such event exists or if more than one match is found.
   *
   * @param subject the subject to match
   * @param start   the start date/time to match
   * @return the matching IEvent, or null if not found or ambiguous
   */
  public IEvent findEvent(String subject, LocalDateTime start) {
    IEvent found = null;
    for (IEvent e : events) {
      if (e.getSubject().equals(subject) && e.getStart().equals(start)) {
        if (found != null) {
          return null;
        }
        found = e;
      }
    }
    return found;
  }

  /**
   * Get all events on that date.
   *
   * @param date represents the date.
   * @return all events on that specific date.
   */
  public List<IEvent> getEventsOnDate(LocalDate date) {
    List<IEvent> result = new ArrayList<>();
    for (IEvent e : events) {
      if (e.getStart().toLocalDate().equals(date)) {
        result.add(e);
      }
    }
    return result;
  }

  /**
   * Gets all events within two dates.
   *
   * @param beginning the start date of the range.
   * @param ending the end date of the range.
   * @return all events within the date range.
   */
  public List<IEvent> getEventsWithinDates(LocalDateTime beginning, LocalDateTime ending) {
    List<IEvent> result = new ArrayList<>();
    for (int i = 0; i < events.size(); i++) {
      IEvent currEvent = events.get(i);
      if (currEvent.getEnd().isAfter(beginning) && currEvent.getStart().isBefore(ending)) {
        result.add(currEvent);
      }
    }
    return result;
  }

  /**
   * Checks whether the event overlaps with a specific time.
   *
   * @param time is the time.
   * @return whether the event overlaps.
   */
  public boolean isBusy(LocalDateTime time) {
    for (IEvent currEvent : events) {
      if (currEvent.getEnd().isAfter(time) && currEvent.getStart().isBefore(time)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns true if adding the given Event `e` would conflict with an existing event.
   * We define a “conflict” to be: an existing calendar event that has the same
   * subject, the same start, and the same end.  In other words, duplicates are not allowed.
   *
   * @param e the Event to check
   * @return true if there is already an event equal to `e`; false otherwise
   */
  public boolean hasConflict(Event e) {
    for (IEvent existing : events) {
      if (existing.equals(e)) {
        return true;
      }
    }
    return false;
  }
}
