package calendar;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.*;

public class CalendarLibraryTest {
  private CalendarLibrary lib;

  /**
   * Sets up a fresh calendar library before each test.
   */
  @Before
  public void init() {
    lib = new CalendarLibrary();
  }

  /**
   * Tests creating a calendar and retrieving it.
   */
  @Test
  public void testCreateCalendarAndUseIt() {
    lib.createCalendar("Work", "America/New_York");
    lib.useCalendar("Work");

    assertEquals("Work", lib.getCurrentCalendarName());
    assertNotNull(lib.getActiveCalendar());
    assertEquals(ZoneId.of("America/New_York"), lib.getActiveTimezone());
  }

  /**
   * Tests switching between two calendars.
   */
  @Test
  public void testSwitchCalendars() {
    lib.createCalendar("Work", "America/New_York");
    lib.createCalendar("Home", "America/Los_Angeles");

    lib.useCalendar("Work");
    assertEquals("Work", lib.getCurrentCalendarName());

    lib.useCalendar("Home");
    assertEquals("Home", lib.getCurrentCalendarName());
  }

  /**
   * Tests renaming a calendar.
   */
  @Test
  public void testRenameCalendar() {
    lib.createCalendar("School", "America/Chicago");
    lib.useCalendar("School");

    lib.editCalendar("School", "name", "College");
    assertEquals("College", lib.getCurrentCalendarName());
    assertNotNull(lib.getActiveCalendar());
  }

  /**
   * Tests editing timezone of an existing calendar.
   */
  @Test
  public void testEditTimezone() {
    lib.createCalendar("Trip", "UTC");
    lib.editCalendar("Trip", "timezone", "Europe/London");

    lib.useCalendar("Trip");
    assertEquals(ZoneId.of("Europe/London"), lib.getActiveTimezone());
  }

  /**
   * Tests deleting a calendar.
   */
  @Test
  public void testDeleteCalendar() {
    lib.createCalendar("Temp", "UTC");
    lib.useCalendar("Temp");
    lib.deleteCalendar("Temp");

    assertNull(lib.getCurrentCalendarName());
    assertFalse(lib.listCalendars().contains("Temp"));
  }

  /**
   * Tests error on creating a calendar with existing name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateCalendarName() {
    lib.createCalendar("X", "UTC");
    lib.createCalendar("X", "UTC");
  }

  /**
   * Tests error on using nonexistent calendar.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUseInvalidCalendar() {
    lib.useCalendar("Nonexistent");
  }

  /**
   * Tests error on invalid timezone.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimezone() {
    lib.createCalendar("Bad", "Not/AZone");
  }

  /**
   * Tests error on renaming to an existing name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRenameToExistingName() {
    lib.createCalendar("A", "UTC");
    lib.createCalendar("B", "UTC");
    lib.editCalendar("A", "name", "B");
  }

  /**
   * Tests error on deleting nonexistent calendar.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDeleteNonexistent() {
    lib.deleteCalendar("Ghost");
  }
}
