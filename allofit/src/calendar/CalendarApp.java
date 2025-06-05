package calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Calendar app that represents a calendar and has all the functions a calendar has.
 * You can add events and edit.
 */
public class CalendarApp {
  private final CalendarController controller;

  public CalendarApp() {
    CalendarModel model = new CalendarModel();
    CalendarView view = new CalendarView();
    controller = new CalendarController(model, view);
  }

  public static void main(String[] args) {
    CalendarApp app = new CalendarApp();

    if (args.length == 0) {
      app.controller.startInteractiveMode();
      return;
    }

    if (args.length >= 2 && args[0].equalsIgnoreCase("--mode")) {
      String mode = args[1].toLowerCase();
      if (mode.equals("interactive")) {
        app.controller.startInteractiveMode();
      } else if (mode.equals("headless") && args.length == 3) {
        try {
          File file = new File(args[2]);
          Scanner fileScanner = new Scanner(file);
          boolean hasExit = false;
          while (fileScanner.hasNextLine()) {
            String command = fileScanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
              hasExit = true;
              break;
            }
            app.controller.processCommand(command);
          }
          fileScanner.close();
          if (!hasExit) {
            System.out.println("Error: Command file must end with 'exit' command");
          }
        } catch (FileNotFoundException e) {
          System.out.println("Error: Command file not found");
        }
      } else {
        System.out.println("Invalid arguments. Usage:");
        System.out.println("java CalendarApp --mode interactive");
        System.out.println("java CalendarApp --mode headless commands.txt");
      }
    }
  }
}