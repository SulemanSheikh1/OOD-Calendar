package calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class EventSeries {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final Set<DayOfWeek> repeatingDays;
  private final Integer count;
  private final LocalDate lastDate;
  private final List<Event> series;

  /**
   * Constructs a series of events that continues for a count of times.
   *
   * @param subject
   * @param start
   * @param end
   * @param weekdays
   * @param count
   */
  public EventSeries(String subject, LocalDateTime start, LocalDateTime end,
                     Set<DayOfWeek> weekdays, int count) {


    this.subject = subject;
    this.start = start;
    this.end = end;
    this.repeatingDays = new HashSet<>(weekdays);
    this.count = count;
    this.lastDate = null;
    this.series = new ArrayList<>();
    generateByCount();
  }

  /**
   * Constructs a series of events that continues until a certain date.
   *
   * @param subject
   * @param start
   * @param end
   * @param weekdays
   * @param lastDate
   */
  public EventSeries(String subject, LocalDateTime start, LocalDateTime end,
                     Set<DayOfWeek> weekdays, LocalDate lastDate) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.repeatingDays = new HashSet<>(weekdays);
    this.count = null;
    this.lastDate = lastDate;
    this.series = new ArrayList<>();
    generateByDate();
  }

  private void generateByCount() {
    LocalDate currDate = start.toLocalDate();
    int counter = 0;

    while (counter < count) {
      if (repeatingDays.contains(currDate.getDayOfWeek())) {
        LocalDateTime starting = LocalDateTime.of(currDate, start.toLocalTime());
        LocalDateTime ending = LocalDateTime.of(currDate, end.toLocalTime());
        series.add(new Event(subject, starting, ending));
        counter++;
      }
      currDate = currDate.plusDays(1);
    }
  }

  private void generateByDate() {
    LocalDate currDate = start.toLocalDate();

    while (!currDate.isAfter(lastDate)) {
      if (repeatingDays.contains(currDate.getDayOfWeek())) {
        LocalDateTime starting = LocalDateTime.of(currDate, start.toLocalTime());
        LocalDateTime ending = LocalDateTime.of(currDate, end.toLocalTime());
        series.add(new Event(subject, starting, ending));
      }
      currDate = currDate.plusDays(1);
    }
  }

  public List<Event> getEvents() {
    return new ArrayList<>(series);
  }
}
