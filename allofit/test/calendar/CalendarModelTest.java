package calendar;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class CalendarModelTest {

  private CalendarModel calendar;
  private Event originalEvent;

  @Before
  public void setUp() {
    calendar = new CalendarModel();
    originalEvent = new Event("Meeting",
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 11, 0),
            "Office",
            "Discuss quarterly goals",
            "public");
    calendar.addEvent(originalEvent);
  }

  @Test
  public void testEditEventSuccessfully() {
    // Remove the original event
    calendar.removeEvent(originalEvent);

    // Create and add updated event
    Event updatedEvent = new Event("Meeting",
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 11, 30),  // extended time
            "Office",
            "Discuss Q2 goals in detail",
            "private");
    calendar.addEvent(updatedEvent);

    List<IEvent> events = calendar.getEventsOnDate(updatedEvent.getStart().toLocalDate());
    assertEquals(1, events.size());
    assertEquals("private", events.get(0).getStatus());
    assertEquals("Discuss Q2 goals in detail", events.get(0).getDescription());
    assertEquals(updatedEvent.getEnd(), events.get(0).getEnd());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventFailsIfOriginalNotPresent() {
    Event nonExistent = new Event("NonExistent",
            LocalDateTime.of(2025, 6, 15, 14, 0),
            LocalDateTime.of(2025, 6, 15, 15, 0),
            "", "", "public");
    calendar.removeEvent(nonExistent);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateEditedEventFails() {
    Event duplicate = new Event("Meeting",
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 11, 0),
            "Office",
            "Discuss quarterly goals",
            "public");
    calendar.addEvent(duplicate);
  }
}
