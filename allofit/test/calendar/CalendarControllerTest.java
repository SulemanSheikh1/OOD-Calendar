package calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalendarControllerTest {
  private CalendarModel model;
  private CalendarController controller;
  private java.io.ByteArrayOutputStream out;

  @Before
  public void setUp() {
    model = new CalendarModel();
    CalendarView view = new CalendarView();
    controller = new CalendarController(model, view);
    out = new java.io.ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(out));
  }

  @After
  public void tearDown() {
    System.setOut(System.out); // Restore normal output
  }

  @Test
  public void testCreateAllDayEvent() {
    controller.processCommand("create event \"Test Event\" on 2025-06-10");
    List<IEvent> events = model.getEventsOnDate(LocalDate.of(2025, 6, 10));
    assertEquals(1, events.size());
    assertEquals("\"Test Event\"", events.get(0).getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 10, 8, 0), events.get(0).getStart());
    assertEquals(LocalDateTime.of(2025, 6, 10, 17, 0), events.get(0).getEnd());
  }


  @Test
  public void testCreateTimedEvent() {
    controller.processCommand("create event Meeting from 2025-06-10T09:00 to 2025-06-10T10:00");
    List<IEvent> events = model.getEventsOnDate(LocalDate.of(2025, 6, 10));
    assertEquals(1, events.size());
    assertEquals("Meeting", events.get(0).getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 10, 9, 0), events.get(0).getStart());
    assertEquals(LocalDateTime.of(2025, 6, 10, 10, 0), events.get(0).getEnd());
  }

  @Test
  public void testPrintEventsOnDate() {
    Event e = new Event("Test", LocalDateTime.of(2025, 6, 10, 8, 0), LocalDateTime.of(2025, 6, 10, 9, 0));
    model.addEvent(e);
    controller.processCommand("print events on 2025-06-10");
    String output = out.toString();
    assertTrue(output.contains("Test"));
    assertTrue(output.contains("2025-06-10 08:00"));
  }

  @Test
  public void testShowStatusBusy() {
    Event e = new Event("Busy", LocalDateTime.of(2025, 6, 10, 8, 0), LocalDateTime.of(2025, 6, 10, 10, 0));
    model.addEvent(e);
    controller.processCommand("show status on 2025-06-10T09:00");
    String output = out.toString();
    assertTrue(output.toLowerCase().contains("busy"));
  }

  @Test
  public void testShowStatusAvailable() {
    controller.processCommand("show status on 2025-06-10T09:00");
    String output = out.toString();
    assertTrue(output.toLowerCase().contains("available"));
  }

  @Test
  public void testUnknownCommand() {
    controller.processCommand("foobar");
    String output = out.toString();
    assertTrue(output.toLowerCase().contains("unknown command"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDuplicateEventThrows() {
    controller.processCommand("create event \"Test Event\" on 2025-06-10");
    controller.processCommand("create event \"Test Event\" on 2025-06-10");
  }


}
