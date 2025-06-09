Calendar Application (Assignment 4, Part 1)

1. How to Run

Interactive Mode
Run in interactive mode:

--mode interactive

Headless Mode

### Headless Mode
Run headless mode:
--mode headless res/valid_commands.txt

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

## 3. Team Contributions

- **Suleman Sheikh (50%)**
   - Designed and implemented the core data‐model classes:
      - `Event` (validation, default end‐time logic, setters/getters)
      - `EventSeries` (count- and until-based recurrence generation)
      - `IEvent` and `SingleEvent` (wrapper for single occurrences)
   - Wrote unit tests for the data model:
      - `EventTest.java`
      - `EventSeriesTest.java`
      - `SingleEventTest.java`
   - Created all `res/` files:
     - `valid_commands.txt`
     - `invalid_commands.txt`
     - `no_exit_commands.txt`
   - Implemented part of `CalendarController` (command parsing, interactive/headless orchestration).
   - Wrote controller tests focusing on “edit” behaviors:
      - `CalendarControllerEditTest.java`

- **Brady Cai (50%)**
   - Developed `CalendarModel` (conflict detection, adding/removing events, querying).
   - Designed and implemented `EventSeries` (count- and until-based recurrence generation)
   - Implemented part of `CalendarController` (command parsing, interactive/headless orchestration).
   - Wrote unit tests for `CalendarModel`:
      - `CalendarModelTest.java`
      - `CalendarAppTest.java`
   - Implemented `CalendarView` (console output formatting) and the `CalendarViewInterface`.
   - Drafted and finalized `README.md` (instructions on how to run, error handling notes).


4. Notes for Graders
   
We chose to represent weekdays as a Set<DayOfWeek> internally.
String parsing uses single‐letter codes (M, T, W, R, F, S, U).

All‐day events default to 08:00–17:00 EST.
If the user omits to <time> on a timed event, an IllegalArgumentException is thrown.
Edge cases (editing a non‐existent event, ambiguous edits) produce a descriptive
error and do not crash the program.
In res/invalid_commands.txt, we show examples of commands that fail and their expected
error messages.
In res/no_exit_commands.txt, we demonstrate a headless file without an exit → the program
prints “Error: missing exit” and quits.