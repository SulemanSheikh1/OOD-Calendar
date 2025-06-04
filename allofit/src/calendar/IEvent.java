package calendar;

import java.time.LocalDateTime;

public interface IEvent {

  public String getSubject();

  public LocalDateTime getStart();

  public LocalDateTime getEnd();

  public String getLocation();

  public String getDescription();

  public String getStatus();

  public boolean isSame(IEvent other);

  public String toString();
}
