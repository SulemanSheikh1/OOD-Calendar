package calendar;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Comprehensive JUnit 4 test suite for the calendar application.
 */

public class CalendarControllerTest {
  private CalendarController controller;
  private ByteArrayOutputStream outContent;
  private PrintStream originalOut;

  @Before
  public void setUp() {
    originalOut = System.out;
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    CalendarLibrary library = new CalendarLibrary();
    library.createCalendar("default", "America/New_York");
    library.useCalendar("default");

    controller = new CalendarController(library, new CalendarView());
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  public void testUnknownCommand() {
    controller.processCommand("foobar");
    String output = outContent.toString();
    assertTrue(output.toLowerCase().contains("unknown command"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDuplicateEventThrows() {
    controller.processCommand("create event \"Test Event\" on 2025-06-10");
    controller.processCommand("create event \"Test Event\" on 2025-06-10");
  }

  @Test
  public void testCreateAllDayEvent() {
    controller.processCommand("create event \"Meeting\" on 2025-06-10");
    String output = outContent.toString().trim();
    assertTrue(output.contains("Created all-day event: \"Meeting\""));
  }

  @Test
  public void testCreateTimedEvent() {
    controller.processCommand("create event \"Standup\" from 2025-06-10T09:00 to 2025-06-10T09:30");
    String output = outContent.toString().trim();
    assertTrue(output.contains("Created timed event: \"Standup\""));
  }


  @Test
  public void testShowStatusAvailableAndBusy() {
    controller.processCommand("show status on 2025-06-10T08:00");
    String output1 = outContent.toString();
    assertTrue(output1.contains("available"));

    outContent.reset();
    controller.processCommand("create event \"Focus\" from 2025-06-10T09:00 to 2025-06-10T10:00");
    outContent.reset();
    controller.processCommand("show status on 2025-06-10T09:30");
    String output2 = outContent.toString();
    assertTrue(output2.contains("busy"));

    outContent.reset();
    controller.processCommand("show status on 2025-06-10T09:00");
    String output3 = outContent.toString();
    assertTrue(output3.contains("available"));

    outContent.reset();
    controller.processCommand("show status on 2025-06-10T10:00");
    String output4 = outContent.toString();
    assertTrue(output4.contains("available"));
  }

  @Test
  public void testPrintEventsOnDate() {
    controller.processCommand("create event \"Alpha\" on 2025-06-11");
    controller.processCommand("create event \"Beta\" on 2025-06-11");
    outContent.reset();
    controller.processCommand("print events on 2025-06-11");
    String output = outContent.toString();
    assertTrue(output.contains("• \"Alpha\""));
    assertTrue(output.contains("• \"Beta\""));
  }

  @Test
  public void testPrintEventsWithinRange() {
    controller.processCommand("create event \"X\" from 2025-06-09T10:00 to 2025-06-09T11:00");
    controller.processCommand("create event \"Y\" from 2025-06-10T12:00 to 2025-06-10T13:00");
    outContent.reset();
    controller.processCommand("print events from 2025-06-09T00:00 to 2025-06-09T23:59");
    String output = outContent.toString();
    assertTrue(output.contains("• \"X\""));
    assertFalse(output.contains("• \"Y\""));
  }

  @Test
  public void testRecurringTimedEventCountBased() {
    controller.processCommand("create event \"DailyMeeting\" " +
            "from 2025-06-10T09:00 to 2025-06-10T10:00 repeats MTWRFSU for 3");
    String output = outContent.toString().trim();
    assertTrue(output.contains("Created recurring timed event series: \"DailyMeeting\""));

    outContent.reset();
    controller.processCommand("print events from 2025-06-10T00:00 to 2025-06-12T23:59");
    String printed = outContent.toString();
    int countOccurrences = printed.split("DailyMeeting", -1).length - 1;
    assertEquals(3, countOccurrences);
  }


  @Test
  public void testRecurringTimedEventUntilBased() {
    controller.processCommand("create event \"SprintReview\" "
            + "from 2025-06-10T14:00 to 2025-06-10T15:00 " + "repeats MTWRFSU until 2025-06-12");
    String output = outContent.toString().trim();
    assertTrue(output.contains("Created recurring timed event series: \"SprintReview\""));
    outContent.reset();
    controller.processCommand("print events from 2025-06-10T00:00 to 2025-06-13T00:00");
    String printed = outContent.toString();
    int countOccurrences = printed.split("SprintReview", -1).length - 1;
    assertEquals(3, countOccurrences);
  }


  @Test
  public void testRecurringAllDayEventCountBased() {
    controller.processCommand("create event \"Gym\" on 2025-06-09 repeats MW for 2");
    String output = outContent.toString().trim();
    assertTrue(output.contains("Created recurring all-day event series: \"Gym\""));
    outContent.reset();
    controller.processCommand("print events on 2025-06-09");
    String printed1 = outContent.toString();
    assertTrue(printed1.contains("• \"Gym\""));
    outContent.reset();
    controller.processCommand("print events on 2025-06-11");
    String printed2 = outContent.toString();
    assertTrue(printed2.contains("• \"Gym\""));
  }


  @Test
  public void testRecurringAllDayEventUntilBased() {
    controller.processCommand("create event \"Yoga\" on 2025-06-09 repeats MF until 2025-06-13");
    String output = outContent.toString().trim();
    assertTrue(output.contains("Created recurring all-day event series: \"Yoga\""));

    outContent.reset();
    controller.processCommand("print events on 2025-06-09");
    String printed1 = outContent.toString();
    assertTrue(printed1.contains("• \"Yoga\""));

    outContent.reset();
    controller.processCommand("print events on 2025-06-13");
    String printed2 = outContent.toString();
    assertTrue(printed2.contains("• \"Yoga\""));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventInvalidFormatThrows() {
    controller.processCommand("create event \"BadEvent\" 2025-06-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTimedEventInvalidFormatThrows() {
    controller.processCommand("create event \"BadTimed\" from 2025-06-10T09:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateRecurringEventInvalidWeekdayThrows() {
    controller.processCommand("create event \"Faulty\" on 2025-06-10 repeats FOO for 3");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateRecurringEventMissingForOrUntilThrows() {
    controller.processCommand("create event \"Faulty\" on 2025-06-10 repeats MONDAY,BADDAY");
  }

  @Test
  public void testEditNonexistentEventPrintsError() {
    outContent.reset();
    controller.processCommand("edit location \"NoSuchEvent\" from 2025-07-06T10:00"
            + " with \"Room101\"");
    String output = outContent.toString();
    assertTrue(output.contains("Error: Event not found."));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testEditMissingFromThrows() {
    controller.processCommand("edit event subject \"X\" 2025-07-06T10:00 with \"NewVal\"");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditMissingWithThrows() {
    controller.processCommand("edit event subject \"X\" from 2025-07-06T10:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditInvalidPropertyThrows() {
    controller.processCommand("edit event foo \"EventA\" from 2025-07-02T09:00 with \"value\"");
  }
}
