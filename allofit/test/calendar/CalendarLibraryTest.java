package calendar;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import calendar.model.CalendarLibrary;
import calendar.model.Event;
import calendar.model.IEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * JUnit 4 test suite for the CalendarLibrary class.
 * Verifies creation, editing, deletion, and event copying between calendars with timezones.
 */
public class CalendarLibraryTest {
  private CalendarLibrary lib;

  /**
   * Sets up a fresh calendar library before each test.
   */
  @Before
  public void setUp() {
    lib = new CalendarLibrary();
  }

  /**
   * Creates and switches to a calendar with the given name and timezone.
   *
   * @param name   the calendar name
   * @param zoneId the IANA timezone ID
   */
  private void createAndUse(String name, String zoneId) {
    lib.createCalendar(name, zoneId);
    lib.useCalendar(name);
  }

  /**
   * Creates two calendars: "Work" and "Home".
   */
  private void createTwoCalendars() {
    lib.createCalendar("Work", "America/New_York");
    lib.createCalendar("Home", "America/Los_Angeles");
  }

  /**
   * Tests creating a calendar and making it active.
   */
  @Test
  public void testCreateCalendarAndUseIt() {
    createAndUse("Work", "America/New_York");

    assertEquals("Work", lib.getCurrentCalendarName());
    assertNotNull(lib.getActiveCalendar());
    assertEquals(ZoneId.of("America/New_York"), lib.getActiveTimezone());
  }

  /**
   * Tests switching between two existing calendars.
   */
  @Test
  public void testSwitchCalendars() {
    createTwoCalendars();

    lib.useCalendar("Work");
    assertEquals("Work", lib.getCurrentCalendarName());

    lib.useCalendar("Home");
    assertEquals("Home", lib.getCurrentCalendarName());
  }

  /**
   * Tests renaming an existing calendar and verifying the change.
   */
  @Test
  public void testRenameCalendar() {
    createAndUse("School", "America/Chicago");

    lib.editCalendar("School", "name", "College");
    assertEquals("College", lib.getCurrentCalendarName());
    assertNotNull(lib.getActiveCalendar());
  }

  /**
   * Tests editing the timezone of an existing calendar.
   */
  @Test
  public void testEditTimezone() {
    lib.createCalendar("Trip", "UTC");
    lib.editCalendar("Trip", "timezone", "Europe/London");

    lib.useCalendar("Trip");
    assertEquals(ZoneId.of("Europe/London"), lib.getActiveTimezone());
  }

  /**
   * Tests successfully editing a calendar's timezone.
   */
  @Test
  public void testEditCalendarTimezoneSuccessfully() {
    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("Travel", "America/New_York");

    library.useCalendar("Travel");
    assertEquals(ZoneId.of("America/New_York"), library.getActiveTimezone());
    library.editCalendar("Travel", "timezone", "Asia/Tokyo");
    assertEquals(ZoneId.of("Asia/Tokyo"), library.getActiveTimezone());
  }

  /**
   * Tests error on creating a calendar with an invalid timezone.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarWithInvalidTimezoneThrows() {
    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("BadCalendar", "Mars/Phobos");
  }

  /**
   * Tests error when editing a calendar to use an invalid timezone.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarWithInvalidTimezoneThrows() {
    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("Test", "Europe/London");
    library.editCalendar("Test", "timezone", "Not/ARealZone");
  }

  /**
   * Tests deleting a calendar and clearing the active calendar.
   */
  @Test
  public void testDeleteCalendar() {
    createAndUse("Temp", "UTC");

    lib.deleteCalendar("Temp");

    assertNull(lib.getCurrentCalendarName());
    assertFalse(lib.listCalendars().contains("Temp"));
  }

  /**
   * Tests copying a date range of events from EST calendar to PST calendar.
   */
  @Test
  public void testCopyEventsInRangeDifferentTimezones() {
    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("EST", "America/New_York");
    library.createCalendar("PST", "America/Los_Angeles");

    library.useCalendar("EST");

    LocalDateTime estStart1 = LocalDateTime.of(2025, 7, 1, 9, 0);
    LocalDateTime estEnd1 = LocalDateTime.of(2025, 7, 1, 10, 0);
    Event event1 = new Event("Event A", estStart1, estEnd1);
    library.getActiveCalendar().addEvent(event1);

    LocalDateTime estStart2 = LocalDateTime.of(2025, 7, 2, 14, 0);
    LocalDateTime estEnd2 = LocalDateTime.of(2025, 7, 2, 15, 0);
    Event event2 = new Event("Event B", estStart2, estEnd2);
    library.getActiveCalendar().addEvent(event2);
    List<IEvent> toCopy = library.getActiveCalendar()
            .getEventsWithinDates(LocalDateTime.of(2025, 7, 1, 0, 0),
                    LocalDateTime.of(2025, 7, 2, 23, 59));

    library.useCalendar("PST");
    ZoneId estZone = ZoneId.of("America/New_York");
    ZoneId pstZone = ZoneId.of("America/Los_Angeles");
    LocalDateTime destStart = LocalDateTime.of(2025, 7, 10, 0, 0);

    for (IEvent e : toCopy) {
      long dayShift = java.time.Duration.between(estStart1.toLocalDate().atStartOfDay(),
              e.getStart().toLocalDate().atStartOfDay()).toDays();
      LocalDateTime placeholder = destStart.plusDays(dayShift);

      ZonedDateTime sourceZoned = e.getStart().atZone(estZone);
      ZonedDateTime shiftedZoned = sourceZoned
              .withZoneSameInstant(pstZone)
              .withYear(placeholder.getYear())
              .withMonth(placeholder.getMonthValue())
              .withDayOfMonth(placeholder.getDayOfMonth());

      LocalDateTime pstShifted = shiftedZoned.toLocalDateTime();
      Event shifted = (Event) e.copyWithNewTime(pstShifted);
      library.getActiveCalendar().addEvent(shifted);
    }

    List<IEvent> result = library.getActiveCalendar().getEventsOnDate(LocalDate.of(2025, 7, 10));
    assertEquals("Event A", result.get(0).getSubject());
    assertEquals(LocalDateTime.of(2025, 7, 10, 6, 0),
            result.get(0).getStart());
  }


  /**
   * Tests that creating a calendar with a duplicate name throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateCalendarName() {
    lib.createCalendar("X", "UTC");
    lib.createCalendar("X", "UTC");
  }

  /**
   * Tests renaming a calendar and verifying it is reflected in the list.
   */
  @Test
  public void testRenameCalendarUpdatesName() {
    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("Work", "America/New_York");
    library.editCalendar("Work", "name", "Office");
    assertFalse(library.listCalendars().contains("Work"));
    assertTrue(library.listCalendars().contains("Office"));
    library.useCalendar("Office");
    assertEquals("Office", library.getCurrentCalendarName());
  }

  /**
   * Tests error when trying to use a nonexistent calendar.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUseInvalidCalendar() {
    lib.useCalendar("Nonexistent");
  }

  /**
   * Tests error when using an invalid timezone.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimezone() {
    lib.createCalendar("Bad", "Not/AZone");
  }

  /**
   * Tests error when renaming to an already used name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRenameToExistingName() {
    lib.createCalendar("A", "UTC");
    lib.createCalendar("B", "UTC");
    lib.editCalendar("A", "name", "B");
  }

  /**
   * Tests error when deleting a calendar that does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDeleteNonexistent() {
    lib.deleteCalendar("Ghost");
  }

  /**
   * Tests that creating a calendar sets the correct name and timezone.
   */
  @Test
  public void testCreateCalendarSetsNameAndTimezone() {
    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("Work", "Europe/Paris");
    assertTrue(library.listCalendars().contains("Work"));
    library.useCalendar("Work");
    assertEquals("Work", library.getCurrentCalendarName());
    assertEquals(ZoneId.of("Europe/Paris"), library.getActiveTimezone());
  }
}
