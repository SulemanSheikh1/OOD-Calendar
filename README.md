Calendar Application (Assignment 5, Part 2)

1. How to Run

Interactive Mode:
Run in interactive mode:

```
--mode interactive
```

Headless Mode:
Run headless mode with a command script:

```
--mode headless res/valid_commands.txt
```

2. Features Implemented

### Multi-Calendar Support

✅	Create multiple calendars with unique names and timezones
✅	Switch between calendars (`switch calendar <name>`)
✅	Edit calendar name or timezone
✅	Delete calendars, list existing calendars

### Timezone Handling

✅	Each calendar has its own `ZoneId` timezone
✅	Times are interpreted in that calendar’s timezone
✅	Timezone conversion applied during event copying

### Event Creation

✅	All-day events (default 08:00–17:00)
✅	Timed events with explicit start/end
✅	Recurring events (count- and until-based), with day-of-week control
✅	Conflict detection prevents duplicates

### Editing Events

✅	Edit individual events (subject, start, end, location, description, status)
✅	Edit future events in a series (`edits`)
✅	Edit entire series (`edit series`)

### Querying

✅	`print events on <date>` → bullet list with details
✅	`print events from <start> to <end>` → time range query
✅	`show status on <dateTime>` → busy / available

### Copying Events Across Calendars

✅	`copy event "Subject" on <datetime> --target <calendar> to <datetime>`
✅	`copy events on <date> --target <calendar> to <date>` (timezone converted)
✅	`copy events between <start> and <end> --target <calendar> to <datetime>` (shifts relative timing)
✅	Partial event series are copied with `seriesId` retained
✅	Conflict detection applied in destination calendar

### Command Syntax & Error Handling

✅	Unknown commands report errors
✅	Invalid formats (e.g., date/time, missing keywords) throw clear errors
✅	Missing `exit` in headless mode prints a message and quits
✅	Interactive and headless modes both supported

3. Team Contributions

* **Suleman Sheikh (50%)**

    * Designed and implemented core model classes:
    * Wrote model logic for editing single/future/series events
    * Implemented `CalendarView` and `ICalendarView`, `Event`,
    * `EventSeries`, `IEvent`, `SingleEvent`
       * Created `CalendarLibrary` for managing multiple calendars and timezones
       * Implemented `copyEventToCalendar`, `copyEventsOnDateToCalendar`, `copyEventsBetweenDatesToCalendar`
       * Developed full controller support for event creation, editing, and copy commands
       * Wrote unit tests:

        * `EventTest.java`, `EventSeriesTest.java`, `CalendarControllerTest.java`
       * Authored `res/` files:

        * `valid_commands.txt`, `invalid_commands.txt`, `no_exit_commands.txt`

* **Brady Cai (50%)**

    * Built `CalendarModel` (event storage, query, conflict detection)
    * Created `CalendarLibrary` for managing multiple calendars and timezones
    * Implemented `copyEventToCalendar`, `copyEventsOnDateToCalendar`, `copyEventsBetweenDatesToCalendar`
    * Extended controller parsing for `edit`, `edits`, `edit series`
    * Created JUnit tests:

        * `CalendarModelTest.java`, `CalendarAppTest.java`, `CalendarViewTest.java`
      * Drafted and finalized `README.md`, documentation, and error examples

4. Notes for Graders

* `weekday` codes are parsed using `Set<DayOfWeek>` with single-letter format: M, T, W, R, F, S, U
* Timezone format uses standard IANA strings: `America/New_York`, `Europe/London`, etc.
* All-day events default to 08:00–17:00 in the calendar’s timezone
* Timezone conversion occurs during event copy (via `ZonedDateTime.withZoneSameInstant`)
* Edge cases (missing keywords, duplicate calendars, invalid dates) handled gracefully
* `res/valid_commands.txt` contains a full working script for grading
* `res/invalid_commands.txt` and `res/no_exit_commands.txt` demonstrate error handling and missing-exit behavior
