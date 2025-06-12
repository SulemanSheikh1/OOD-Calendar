package calendar.controller;

import calendar.model.CalendarLibrary;
import calendar.model.ICalendarLibrary;
import calendar.view.CalendarView;
import calendar.view.ICalendarView;

/**
 * Calendar app that represents a calendar and has all the functions a calendar has.
 * You can add events and edit.
 */
public class CalendarApp {
  private final ICalendarController controller;

  public CalendarApp() {
    ICalendarLibrary library = new CalendarLibrary();
    ICalendarView view = new CalendarView();
    this.controller = new CalendarController((CalendarLibrary) library, (CalendarView) view);
  }

  /**
   * Constructs a CalendarApp with a new library, view, and controller.
   */
  public static void main(String[] args) {
    CalendarApp app = new CalendarApp();

    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.out.println("Error: Must specify mode as --mode interactive or --mode headless <file>");
      return;
    }

    String mode = args[1];

    if (mode.equalsIgnoreCase("interactive")) {
      app.controller.runInteractive();
    } else if (mode.equalsIgnoreCase("headless")) {
      if (args.length < 3) {
        System.out.println("Error: Missing filename for headless mode.");
        return;
      }
      app.controller.runHeadless(args[2]);
    } else {
      System.out.println("Error: Unknown mode. Use 'interactive' or 'headless'.");
    }
  }
}
