package calendar.controller;

import calendar.model.CalendarLibrary;
import calendar.model.ICalendarLibrary;
import calendar.view.CalendarView;
import calendar.view.ICalendarView;

/**
 * The entry point for the Calendar application.
 * This class sets up the controller and supports both interactive and headless modes.
 * Users can add, edit, copy, and query calendar events across multiple named calendars.
 */
public class CalendarApp {
  private final ICalendarController controller;

  /**
   * Constructs a new CalendarApp.
   * Initializes the calendar library, view, and controller components.
   */
  public CalendarApp() {
    ICalendarLibrary library = new CalendarLibrary();
    ICalendarView view = new CalendarView();
    this.controller = new CalendarController((CalendarLibrary) library, (CalendarView) view);
    this.controller.processCommand("create calendar \"Default\" America/New_York");
    this.controller.processCommand("switch calendar \"Default\"");
  }


  /**
   * Starts the calendar application.
   * Accepts command-line arguments to choose between interactive and headless mode.
   *
   * @param args command-line arguments specifying mode and optional script file
   */
  public static void main(String[] args) {
    CalendarApp app = new CalendarApp();

    if (args.length == 0) {
      app.controller.runGUI();
    } else if (args.length >= 2 && args[0].equalsIgnoreCase("--mode")) {
      if (args[1].equalsIgnoreCase("interactive")) {
        app.controller.runInteractive();
      } else if (args[1].equalsIgnoreCase("headless") && args.length >= 3) {
        app.controller.runHeadless(args[2]);
      } else {
        System.out.println("Error: Invalid mode or missing file path.");
      }
    } else {
      System.out.println("Error: Must specify mode as --mode interactive or --mode headless <file>");
    }
  }

}
