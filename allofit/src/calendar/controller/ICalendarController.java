package calendar.controller;

/**
 * Defines the public API of a calendar controller:
 *  - processing a single command (for tests or headless mode)
 *  - running the interactive loop (for interactive mode)
 */
public interface ICalendarController {
  /**
   * Process exactly one user command string.
   * May print output or errors to stdout.
   *
   * @param command the raw command line
   */
  void processCommand(String command);

  /**
   * Start the interactive read–eval–print loop,
   * reading commands from stdin until "exit".
   */
  void runInteractive();

  void runHeadless(String filePath);
}
