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
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 9, 0); // Monday
    LocalDateTime end = LocalDateTime.of(2025, 6, 2, 10, 0);
    Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

    EventSeries series = new EventSeries("Math Class", start, end, days, 3);
    List<Event> events = series.getEvents();

    assertEquals(3, events.size());
    assertEquals("2025-06-02T09:00", events.get(0).getStart().toString());
    assertEquals("2025-06-04T09:00", events.get(1).getStart().toString());
    assertEquals("2025-06-06T09:00", events.get(2).getStart().toString());
  }


}