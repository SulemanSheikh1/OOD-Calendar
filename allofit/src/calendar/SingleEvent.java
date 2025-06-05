package calendar;

/**
 * Class tha represents a single event.
 */
public class SingleEvent {
  private Event event;

  public SingleEvent(Event event) {
    this.event = event;
  }

  public Event getEvent() { return event; }
}
