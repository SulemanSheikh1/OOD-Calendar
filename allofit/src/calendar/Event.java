package calendar;

import java.time.LocalDateTime;

/**
 *
 */
public class Event implements IEvent {
  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;
  private String location;
  private String description;
  private String status;


  // An event is required to have a subject, start date and time.
  // Optionally, it may have a longer description, end date and time, location
  // (physical or online) and a status (whether the event is public or private).
  //
  //There cannot be two events with the same subject, start date/time and end date/time.

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
    this.location = "";
    this.description = "";
    this.status = "";
  }

  public Event(String subject, LocalDateTime start, LocalDateTime end, String location,
                   String description, String status) {
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
    this.location = location;
    this.description = description;
    this.status = status;
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

  public String getLocation() { return location; }

  public String getDescription() { return description; }

  public String getStatus() { return status; }

  public boolean isSame(IEvent other) {
    if (other == null) {
      return false;
    }
    return subject.equals(other.getSubject())
            && start.equals(other.getStart())
            && end.equals(other.getEnd())
            && location.equals(other.getLocation())
            && description.equals(other.getDescription())
            && status.equals(other.getStatus());
  }

  public String toString() {
    return String.format("%s %s %s", subject, start, end);
  }
}
