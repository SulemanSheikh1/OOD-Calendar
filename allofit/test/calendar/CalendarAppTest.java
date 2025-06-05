package calendar;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CalendarAppTest {

  @Test
  public void testGenerateByCountMWF3Occ() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 2, 10, 0);
    Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

    EventSeries series = new EventSeries("Math Class", start, end, days, 3);
    List<Event> events = series.getEvents();

    assertEquals(3, events.size());
    assertEquals("2025-06-02T09:00", events.get(0).getStart().toString());
    assertEquals("2025-06-04T09:00", events.get(1).getStart().toString());
    assertEquals("2025-06-06T09:00", events.get(2).getStart().toString());
    assertEquals("2025-06-02T10:00", events.get(0).getEnd().toString());
    assertEquals("2025-06-04T10:00", events.get(1).getEnd().toString());
    assertEquals("2025-06-06T10:00", events.get(2).getEnd().toString());
    assertEquals("Math Class", events.get(0).getSubject());
    assertEquals("Math Class", events.get(1).getSubject());
    assertEquals("Math Class", events.get(2).getSubject());
  }

  @Test
  public void testGenerateByCountTT2Occ() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 3, 13, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 3, 14, 0);
    Set<DayOfWeek> days = Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);

    EventSeries series = new EventSeries("Beach Time", start, end, days, 2);
    List<Event> events = series.getEvents();

    assertEquals(2, events.size());
    assertEquals("2025-06-03T13:00", events.get(0).getStart().toString());
    assertEquals("2025-06-05T13:00", events.get(1).getStart().toString());
    assertEquals("2025-06-03T14:00", events.get(0).getEnd().toString());
    assertEquals("2025-06-05T14:00", events.get(1).getEnd().toString());
    assertEquals("Beach Time", events.get(0).getSubject());
    assertEquals("Beach Time", events.get(1).getSubject());
  }

  @Test
  public void testGenerateByCountS1Occ() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 7, 3, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 7, 5, 0);
    Set<DayOfWeek> days = Set.of(DayOfWeek.SATURDAY);

    EventSeries series = new EventSeries("Dancing", start, end, days, 1);
    List<Event> events = series.getEvents();

    assertEquals(1, events.size());
    assertEquals("2025-06-07T03:00", events.get(0).getStart().toString());
    assertEquals("2025-06-07T05:00", events.get(0).getEnd().toString());
    assertEquals("Dancing", events.get(0).getSubject());
    assertEquals("Dancing", events.get(0).getSubject());
  }
}