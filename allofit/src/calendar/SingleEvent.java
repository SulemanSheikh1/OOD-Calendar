package calendar;

/**
 * Class tha represents a single event.
 */
public class SingleEvent {
  private Event event;

  /**
   * Constructs a SingleEvent with the given event.
   *
   * @param event the Event to wrap
   */
  public SingleEvent(Event event) {
    this.event = event;
  }

  /**
   * Returns the wrapped Event object.
   *
   * @return the Event instance
   */
  public Event getEvent() {
    return event;
  }
}
