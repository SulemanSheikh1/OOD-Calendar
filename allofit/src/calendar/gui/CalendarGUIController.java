package calendar.gui;

import calendar.model.*;
import calendar.view.CalendarView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CalendarGUIController implements ICalendarGUIController {
  private final CalendarLibrary library;
  private final ICalendarGUIView view;

  public CalendarGUIController(CalendarLibrary library, ICalendarGUIView view) {
    this.library = library;
    this.view = view;
    this.view.setController(this);
   }

  @Override
  public void createEvent(String subject, String start, String end) {
    try {
      LocalDateTime startTime = LocalDateTime.parse(start);
      LocalDateTime endTime = LocalDateTime.parse(end);
      Event event = new Event(subject, startTime, endTime);
      library.getActiveCalendar().addEvent(event);
      view.showError("Event created.");
    } catch (Exception e) {
      view.showError("Error creating event: " + e.getMessage());
    }
  }

  @Override
  public void loadEventsFromDate(String date) {
    try {
      LocalDate startDate = LocalDate.parse(date);
      List<IEvent> events = library.getActiveCalendar().getEventsFromDate(startDate);
      view.showEvents(events);
    } catch (Exception e) {
      view.showError("Error loading events: " + e.getMessage());
    }
  }

  @Override
  public void editEvent(IEvent event, String newSubject, String newStart, String newEnd) {
    try {
      LocalDateTime newStartTime = LocalDateTime.parse(newStart);
      LocalDateTime newEndTime = LocalDateTime.parse(newEnd);
      // Remove old event and add new one (simple replacement approach)
      library.getActiveCalendar().removeEvent(event);
      Event updatedEvent = new Event(newSubject, newStartTime, newEndTime);
      library.getActiveCalendar().addEvent(updatedEvent);
      view.showError("Event updated.");
      // Refresh event list after edit
      loadEventsFromDate(newStartTime.toLocalDate().toString());
    } catch (DateTimeParseException e1) {
      view.showError("Invalid date format. Please use yyyy-MM-dd'T'HH:mm");
    } catch (Exception e2) {
      view.showError("Error editing event: " + e2.getMessage());
    }
  }
}
