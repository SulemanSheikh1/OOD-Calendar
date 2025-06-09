package calendar;

/**
 * Calendar app that represents a calendar and has all the functions a calendar has.
 * You can add events and edit.
 */
public class CalendarApp {
  private final CalendarController controller;

  /**
   * Constructs a CalendarApp with a new library, view, and controller.
   */
  public CalendarApp() {
    CalendarLibrary library = new CalendarLibrary();
    CalendarView view = new CalendarView();
    controller = new CalendarController(library, view);
  }

  /**
   * The main entry point of the application.
   *
   * @param args Command-line arguments for mode selection
   */
  public static void main(String[] args) {
    CalendarApp app = new CalendarApp();
    app.controller.runInteractive();
  }
}