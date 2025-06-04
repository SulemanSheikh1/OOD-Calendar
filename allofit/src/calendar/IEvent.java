package calendar;

import java.time.LocalDateTime;

public interface IEvent {

  String getSubject();

  LocalDateTime getStart();

  LocalDateTime getEnd();

  String getLocation();

  String getDescription();

  String getStatus();

  boolean isSame(IEvent other);

  String toString();

  boolean isEvent();
}
