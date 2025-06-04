package calendar;

import java.util.Scanner;

/**
 * Represents a controller.
 */
public class CalendarApp {
  private final CalendarModel model;
  private final CalendarView view;

  /**
   * Constructs the calendar app with model and view.
   */
  public CalendarApp() {
    model = new CalendarModel();
    view = new CalendarView();
  }


}