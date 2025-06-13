package calendar.controller;

/**
 * Defines the public API of a calendar controller.
 * A calendar controller can process single commands (for tests or headless mode)
 * and run an interactive loop (for interactive user input).
 */
public interface ICalendarController {
  /**
   * Processes exactly one user command string.
   * May print output or errors to stdout.
   *
   * @param command the raw command line
   */
  void processCommand(String command);

  /**
   * Starts the interactive read–eval–print loop.
   * Reads commands from stdin until "exit" is received.
   */
  void runInteractive();

  /**
   * Executes commands from a file in headless mode.
   * Stops when "exit" is encountered or file ends.
   *
   * @param filePath the path to the script file containing commands
   */
  void runHeadless(String filePath);
}
