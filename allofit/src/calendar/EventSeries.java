package calendar;

import java.time.LocalDateTime;
import java.util.List;

public class EventSeries {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final List<Event> series;

  public EventSeries(String subject, LocalDateTime start, LocalDateTime end, List<Event> series) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.series = series;
  }
}
