package calendar.gui;

import calendar.model.IEvent;

public interface ICalendarGUIController {
  void createEvent(String subject, String start, String end);
  void loadEventsFromDate(String date);
  void editEvent(IEvent event, String newSubject, String newStart, String newEnd);
}
