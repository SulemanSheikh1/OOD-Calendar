package calendar;

/**
 * Calendar app that represents a calendar and has all the functions a calendar has.
 * You can add events and edit.
 */
public class CalendarApp {
  private final CalendarController controller;

  /**
   * Constructs a CalendarApp with a new model, view, and controller.
   */
  public CalendarApp() {
    CalendarModel model = new CalendarModel();
    CalendarView view = new CalendarView();
    controller = new CalendarController(model, view);
  }

  /**
   * The main entry point of the application.
   *
   * @param args Command-line arguments for mode selection
   */
  public static void main(String[] args) {
    CalendarApp app = new CalendarApp();

    if (args.length == 0) {
      app.controller.startInteractiveMode();
    }

    if (args.length >= 2 && args[0].equalsIgnoreCase("--mode")) {
      String mode = args[1].toLowerCase();
      if (args.length == 0) {
        app.controller.runInteractive();
      } else if (mode.equals("interactive")) {
        app.controller.runInteractive();
      }
    }
  }
}