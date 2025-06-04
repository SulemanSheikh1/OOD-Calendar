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
   * Returns all events.
   *
   * @param date represents the date.
   */
  public List<IEvent> getEvents(LocalDate date) {
    List<IEvent> result = new ArrayList<>();
    for (int i = 0; i < events.size(); i++) {
      IEvent currEvent = events.get(i);
      if (currEvent.getStart().toLocalDate().equals(date)) {
        result.add(currEvent);
      }
    }
    return result;
  }
}
