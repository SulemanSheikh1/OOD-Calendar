# Calendar Application (Assignment 4, Part 1)

## 1. How to Run

### Interactive Mode
Run in interactive mode:

java -cp bin:.:path/to/junit-4.12.jar calendar.CalendarApp --mode interactive
You can now type commands like create event "Meeting" on 2025-06-10, print events on 2025-06-10
until you enter exit.

Headless Mode

### Headless Mode
Run headless mode:
java -cp bin:.:path/to/junit-4.12.jar calendar.CalendarApp --mode headless res/valid_commands.txt

1. Features Implemented
   Below is a summary of which features were fully implemented.

Create a Single Calendar Event

✅ All-day events (create event "Subject" on YYYY-MM-DD)

✅ Timed events (create event "Subject" from YYYY-MM-DDThh:mm to YYYY-MM-DDThh:mm)

✅ Auto‐default end time at 5 PM for all-day events.

Create an Event Series

✅ Count-based recurring events on specific weekdays for N occurrences.

✅ Until-based recurring events on specific weekdays (inclusive).

✅ All-day series and timed‐series both supported.

✅ Prevents duplicates (no two events with the same subject, start, or end).

Edit Calendar Events

✅  Rename a single event or a single occurrence in a series.

✅ Modify “this and future” within a series.

✅ Modify “all in series” (past & future).

✅ Change subject, start, end, location, description, status.

Query Calendar

✅ print events on YYYY-MM-DD → lists bullets with subject, times, and location.

✅ print events from YYYY-MM-DDThh:mm to YYYY-MM-DDThh:mm → lists all events in that interval.

✅ show status on YYYY-MM-DDThh:mm → “busy” / “available”.

Command Syntax & Error Handling

✅ Unknown commands (any unrecognized keyword) prints an error.

✅ Invalid date/time formats throw a clear IllegalArgumentException.

✅ Missing exit in headless mode → prints an error & quits.

✅ Both modes (interactive & headless) supported.

3. Team Contributions

Suleman Sheikh (50%)

Designed and implemented the Event and EventSeries classes (data model).

Wrote unit tests for Event and EventSeries.

Brady Cai (50%)

Developed CalendarModel (conflict detection, querying, adding/removing).

Wrote unit tests for CalendarModel.

4. Notes for Graders
   We chose to represent weekdays as a Set<DayOfWeek> internally.
String parsing uses single‐letter codes (M, T, W, R, F, S, U).

All‐day events default to 08:00–17:00 EST.
If the user omits to <time> on a timed event, an IllegalArgumentException is thrown.
Edge cases (e.g. editing a non‐existent event, ambiguous edits) produce a descriptive
error and do not crash the program.
In res/invalid_commands.txt, we show examples of commands that fail and their expected
error messages.

In res/no_exit_commands.txt, we demonstrate a headless file without an exit → the program
prints “Error: missing exit” and quits.