package calendar;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CalendarModelTest {
  private CalendarModel model;
  private Event e1, e2, eDup, eOverlap;

  @Before
  public void setUp() {
    model = new CalendarModel();
    LocalDateTime start1 = LocalDateTime.of(2025, 6, 10, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 6, 10, 10, 0);
    e1 = new Event("Meeting", start1, end1);

    LocalDateTime start2 = LocalDateTime.of(2025, 6, 11, 14, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 6, 11, 15, 0);
    e2 = new Event("Review", start2, end2);

    // Duplicate of e1 by subject, start, end
    eDup = new Event("Meeting", start1, end1);

    // Overlapping in time but different subject
    LocalDateTime startO = LocalDateTime.of(2025, 6, 10, 9, 30);
    LocalDateTime endO = LocalDateTime.of(2025, 6, 10, 10, 30);
    eOverlap = new Event("Overlap", startO, endO);
  }

  @Test
  public void testAddEventAndHasConflict() {
    assertFalse(model.hasConflict(e1));
    model.addEvent(e1);
    assertTrue(model.hasConflict(e1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateEventThrows() {
    model.addEvent(e1);
    model.addEvent(eDup);
  }

  @Test
  public void testRemoveEvent() {
    model.addEvent(e1);
    model.removeEvent(e1);
    assertFalse(model.hasConflict(e1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonexistentEventThrows() {
    model.removeEvent(e1);
  }

  @Test
  public void testFindEventReturnsCorrect() {
    model.addEvent(e1);
    IEvent found = model.findEvent("Meeting", e1.getStart());
    assertNotNull(found);
    assertEquals("Meeting", found.getSubject());
  }

  @Test
  public void testFindEventAmbiguousReturnsNull() {
    // Two events same subject & start but different end
    LocalDateTime startA = LocalDateTime.of(2025, 6, 12, 9, 0);
    LocalDateTime endA1 = LocalDateTime.of(2025, 6, 12, 10, 0);
    LocalDateTime endA2 = LocalDateTime.of(2025, 6, 12, 11, 0);
    Event a1 = new Event("Amb", startA, endA1);
    Event a2 = new Event("Amb", startA, endA2);
    model.addEvent(a1);
    model.addEvent(a2);
    IEvent found = model.findEvent("Amb", startA);
    assertNull(found);
  }

  @Test
  public void testGetEventsOnDate() {
    model.addEvent(e1);
    model.addEvent(e2);
    LocalDate d1 = LocalDate.of(2025, 6, 10);
    List<IEvent> list = model.getEventsOnDate(d1);
    assertEquals(1, list.size());
    assertEquals("Meeting", list.get(0).getSubject());
  }

  @Test
  public void testGetEventsWithinDates() {
    model.addEvent(e1);
    model.addEvent(e2);
    LocalDateTime windowStart = LocalDateTime.of(2025, 6, 9, 0, 0);
    LocalDateTime windowEnd = LocalDateTime.of(2025, 6, 10, 23, 59);
    List<IEvent> list = model.getEventsWithinDates(windowStart, windowEnd);
    assertEquals(1, list.size());
    assertEquals("Meeting", (list.get(0)).getSubject());
  }

  @Test
  public void testIsBusy() {
    model.addEvent(e1);
    // Time inside e1
    assertTrue(model.isBusy(LocalDateTime.of(2025, 6, 10, 9, 30)));
    // Exactly at start
    assertFalse(model.isBusy(LocalDateTime.of(2025, 6, 10, 9, 0)));
    // Exactly at end
    assertFalse(model.isBusy(LocalDateTime.of(2025, 6, 10, 10, 0)));
    // Before
    assertFalse(model.isBusy(LocalDateTime.of(2025, 6, 10, 8, 59)));
    // After
    assertFalse(model.isBusy(LocalDateTime.of(2025, 6, 10, 10, 1)));
  }

  @Test
  public void testHasConflictOnlyForExactMatch() {
    model.addEvent(e1);
    // Overlapping but different subject: hasConflict should be false
    assertFalse(model.hasConflict(eOverlap));
  }
}
