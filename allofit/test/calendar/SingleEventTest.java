package calendar;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class SingleEventTest {
  @Test
  public void testGetEvent() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 12, 12, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 12, 13, 0);
    Event e = new Event("Solo", start, end);
    SingleEvent se = new SingleEvent(e);
    assertEquals(e, se.getEvent());
  }
}