package calendar;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Testing Calendar Application.
 */
public class CalendarAppTest {

  @Test
  public void testCountEmptyDays() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 2, 10, 0);
    Set<DayOfWeek> days = Set.of();

    EventSeries series = new EventSeries("Empty", start, end, days, 3);

    assertEquals(0, series.getEvents().size());
  }

  @Test
  public void testDateEmptyDays() {
    LocalDateTime start = LocalDateTime.parse("2025-06-02T09:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-02T10:00");
    Set<DayOfWeek> days = Set.of();
    LocalDate until = LocalDate.parse("2025-06-30");

    EventSeries series = new EventSeries("Empty", start, end, days, until);
    List<Event> events = series.getEvents();

    assertEquals(0, events.size());
  }

  @Test
  public void testCountZeroEvents() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 2, 10, 0);
    Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY);

    EventSeries series = new EventSeries("Zero Events", start, end, days, 0);

    assertEquals(0, series.getEvents().size());
  }

  @Test
  public void testDateZeroEvents() {
    LocalDateTime start = LocalDateTime.parse("2025-06-02T09:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-02T10:00");
    Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY);
    LocalDate until = LocalDate.parse("2025-06-01");

    EventSeries series = new EventSeries("Zero Events", start, end, days, until);
    List<Event> events = series.getEvents();

    assertEquals(0, events.size());
  }

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

  @Test
  public void testAllWeekdays() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 2, 11, 0);
    Set<DayOfWeek> days = Set.of(DayOfWeek.values());

    EventSeries series = new EventSeries("Party", start, end, days, 7);
    List<Event> events = series.getEvents();

    assertEquals(7, events.size());
    assertEquals("2025-06-02T10:00", events.get(0).getStart().toString());
    assertEquals("2025-06-08T10:00", events.get(6).getStart().toString());
    assertEquals("2025-06-02T11:00", events.get(0).getEnd().toString());
    assertEquals("2025-06-08T11:00", events.get(6).getEnd().toString());
  }

  @Test
  public void testGenerateByDateMWF3Occ() {
    LocalDateTime start = LocalDateTime.parse("2025-06-02T09:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-02T10:00");
    Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
    LocalDate until = LocalDate.parse("2025-06-08");

    EventSeries series = new EventSeries("Math Class", start, end, days, until);
    List<Event> events = series.getEvents();

    assertEquals(3, events.size());
    assertEquals("2025-06-02T09:00", events.get(0).getStart().toString());
    assertEquals("2025-06-02T10:00", events.get(0).getEnd().toString());
    assertEquals("2025-06-04T09:00", events.get(1).getStart().toString());
    assertEquals("2025-06-04T10:00", events.get(1).getEnd().toString());
    assertEquals("2025-06-06T09:00", events.get(2).getStart().toString());
    assertEquals("2025-06-06T10:00", events.get(2).getEnd().toString());
    assertEquals("Math Class", events.get(0).getSubject());
  }

  @Test
  public void testGenerateByDateTT2Occ() {
    LocalDateTime start = LocalDateTime.parse("2025-06-03T13:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-03T14:00");
    Set<DayOfWeek> days = Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);
    LocalDate until = LocalDate.parse("2025-06-10");

    EventSeries series = new EventSeries("Beach Time", start, end, days, until);
    List<Event> events = series.getEvents();

    assertEquals(3, events.size());
    assertEquals("2025-06-03T13:00", events.get(0).getStart().toString());
    assertEquals("2025-06-03T14:00", events.get(0).getEnd().toString());
    assertEquals("2025-06-05T13:00", events.get(1).getStart().toString());
    assertEquals("2025-06-05T14:00", events.get(1).getEnd().toString());
    assertEquals("Beach Time", events.get(0).getSubject());
  }

  @Test
  public void testGenerateByDateS1Occ() {
    LocalDateTime start = LocalDateTime.parse("2025-06-07T03:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-07T05:00");
    Set<DayOfWeek> days = Set.of(DayOfWeek.SATURDAY);
    LocalDate until = LocalDate.parse("2025-06-07");

    EventSeries series = new EventSeries("Dancing", start, end, days, until);
    List<Event> events = series.getEvents();

    assertEquals(1, events.size());
    assertEquals("2025-06-07T03:00", events.get(0).getStart().toString());
    assertEquals("2025-06-07T05:00", events.get(0).getEnd().toString());
    assertEquals("Dancing", events.get(0).getSubject());
  }

  @Test
  public void testGenerateByDateAllWeekdays() {
    LocalDateTime start = LocalDateTime.parse("2025-06-02T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-02T11:00");
    Set<DayOfWeek> days = Set.of(DayOfWeek.values());
    LocalDate until = LocalDate.parse("2025-06-08");

    EventSeries series = new EventSeries("Party", start, end, days, until);
    List<Event> events = series.getEvents();

    assertEquals(7, events.size());
    assertEquals("2025-06-02T10:00", events.get(0).getStart().toString());
    assertEquals("2025-06-08T10:00", events.get(6).getStart().toString());
    assertEquals("2025-06-02T11:00", events.get(0).getEnd().toString());
    assertEquals("2025-06-08T11:00", events.get(6).getEnd().toString());
  }
}