package calendar;

import java.time.LocalDateTime;

public class Event {
  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;

  public Event(String subject, LocalDateTime start, LocalDateTime end) {
    if (subject == null || subject.isEmpty()) {
      throw new IllegalArgumentException("Subject cannot be null or empty");
    }
    if (start == null || start.isAfter(end)) {
      throw new IllegalArgumentException("Must need a valid start date");
    }
    if (end != null || end.isBefore(start)) {
      throw new IllegalArgumentException("End date cannot be before start date");
    }

    this.subject = subject;
    this.start = start;
    this.end = end;
  }

  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public boolean isSame(Event other) {
    return subject.equals(other.getSubject())
            && start.equals(other.getStart())
            && end.equals(other.getEnd());
  }
}
