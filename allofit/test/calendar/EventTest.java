package calendar;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EventTest {
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullSubjectThrows() {
    new Event(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorEmptySubjectThrows() {
    new Event("", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullStartThrows() {
    new Event("Subject", null, LocalDateTime.now().plusHours(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorEndBeforeStartThrows() {
    LocalDateTime now = LocalDateTime.of(2025, 6, 10, 10, 0);
    new Event("Backwards", now, now.minusHours(1));
  }

  @Test
  public void testConstructorNullEndDefaultsToOneHourLater() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 10, 9, 0);
    Event e = new Event("AutoEnd", start, null);
    assertEquals(start.plusHours(1), e.getEnd());
  }

  @Test
  public void testSetSubjectNullThrows() {
    Event e = new Event("Valid", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    try {
      e.setSubject(null);
      fail("Expected IllegalArgumentException for null subject");
    } catch (IllegalArgumentException ex) {
      assertTrue(ex.getMessage().contains("Subject cannot be null or empty"));
    }
  }

  @Test
  public void testSetStartNullThrows() {
    Event e = new Event("Valid", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    try {
      e.setStart(null);
      fail("Expected IllegalArgumentException for null start");
    } catch (IllegalArgumentException ex) {
      assertTrue(ex.getMessage().contains("Start date/time cannot be null"));
    }
  }

  @Test
  public void testSetEndNullThrows() {
    Event e = new Event("Valid", LocalDateTime.of(2025, 6, 10, 9, 0), LocalDateTime.of(2025, 6, 10, 10, 0));
    try {
      e.setEnd(null);
      fail("Expected IllegalArgumentException for null end");
    } catch (IllegalArgumentException ex) {
      assertTrue(ex.getMessage().contains("End date/time cannot be null"));
    }
  }

  @Test
  public void testSetEndBeforeStartAdjusts() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 10, 9, 0);
    Event e = new Event("Adjusted", start, LocalDateTime.of(2025, 6, 10, 10, 0));
    e.setEnd(start.minusHours(1));
    // According to code, end < start → end = start + 1 hour
    assertEquals(start.plusHours(1), e.getEnd());
  }

  @Test
  public void testEqualsAndHashCode() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 10, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 10, 10, 0);
    Event e1 = new Event("Same", start, end);
    Event e2 = new Event("Same", start, end);
    assertEquals(e1, e2);
    assertEquals(e1.hashCode(), e2.hashCode());
  }

  @Test
  public void testIsSameMatchesEqualsLogic() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 11, 14, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 11, 15, 0);
    Event e1 = new Event("Compare", start, end);
    Event e2 = new Event("Compare", start, end);
    assertTrue(e1.isSame(e2));
  }
}
