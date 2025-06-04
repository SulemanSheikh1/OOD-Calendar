package calendar;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The storing and managing of all calendar events.
 */
public class CalendarModel {
  private List<IEvent> events;

  /**
   * Constructs an empty calendar.
   */
  public CalendarModel() {
    events = new ArrayList<>();
  }

  /**
   * Add events to the calendar.
   *
   * @param event represents other event.
   */
  public void addEvent(IEvent event) {
    for (int i = 0; i < events.size(); i++) {
      IEvent currEvent = events.get(i);

      if (currEvent.getSubject().equals(event.getSubject())
              && currEvent.getStart().equals(event.getStart())
              && currEvent.getEnd().equals(event.getEnd())) {
        throw new IllegalArgumentException("Cannot add two events with the same subject");
      }
    }
    events.add(event);
  }

  /**
   * Get all events on that date.
   *
   * @param date represents the date.
   * @return all events on that specific date.
   */
  public List<IEvent> getEventsOnDate(LocalDate date) {
    List<IEvent> result = new ArrayList<>();
    for (int i = 0; i < events.size(); i++) {
      IEvent currEvent = events.get(i);
      if (currEvent.getStart().toLocalDate().equals(date)) {
        result.add(currEvent);
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
}
