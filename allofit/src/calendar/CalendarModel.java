package calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * The storing and managing of all calendar events.
 */
public class CalendarModel {
  private List<Event> events;

  /**
   * Constructs an empty calendar.
   */
  public CalendarModel() {
    events = new ArrayList<>();
  }
}
