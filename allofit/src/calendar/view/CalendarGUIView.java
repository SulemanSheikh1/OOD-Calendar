package calendar.view;

import calendar.controller.ICalendarController;
import calendar.model.Event;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A Swing-based view for the calendar app.
 * Provides UI for viewing and creating events.
 */
public class CalendarGUIView extends JFrame implements ICalendarView {
  private final JTextField dateField;
  private final JTextField subjectField;
  private final JTextField startField;
  private final JTextField endField;
  private final JTextArea outputArea;
  private final ICalendarController controller;
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private void clearFields() {
    dateField.setText("");
    subjectField.setText("");
    startField.setText("");
    endField.setText("");
  }

  /**
   * Constructs the GUI view and links it to the controller.
   *
   * @param controller the controller
   */
  public CalendarGUIView(ICalendarController controller) {
    this.controller = controller;

    setTitle("Calendar Application");
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    // Top panel: date field and View button
    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.add(new JLabel("Date (yyyy-MM-dd):"));
    dateField = new JTextField(10);
    topPanel.add(dateField);
    JButton viewButton = new JButton("View Schedule");
    viewButton.addActionListener(this::handleViewSchedule);
    topPanel.add(viewButton);

    // Middle panel: subject/start/end + Create button
    JPanel createPanel = new JPanel(new GridLayout(5, 2, 10, 10));
    createPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    createPanel.add(new JLabel("Subject:"));
    subjectField = new JTextField(20);
    createPanel.add(subjectField);
    createPanel.add(new JLabel("Start (yyyy-MM-dd'T'HH:mm):"));
    startField = new JTextField(20);
    createPanel.add(startField);
    createPanel.add(new JLabel("End (yyyy-MM-dd'T'HH:mm):"));
    endField = new JTextField(20);
    createPanel.add(endField);
    JButton createButton = new JButton("Create Event");
    createButton.addActionListener(this::handleCreateEvent);
    JButton clearButton = new JButton("Clear Fields");
    clearButton.addActionListener(e -> clearFields());
    createPanel.add(createButton);
    createPanel.add(clearButton);

    outputArea = new JTextArea();
    outputArea.setEditable(false);
    outputArea.setRows(15);
    JScrollPane scrollPane = new JScrollPane(outputArea);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    add(topPanel, BorderLayout.NORTH);
    add(createPanel, BorderLayout.CENTER);
    add(scrollPane, BorderLayout.SOUTH);
  }

  /**
   * Handles the "View Schedule" button.
   *
   * @param e the action event by the button click
   */
  private void handleViewSchedule(ActionEvent e) {
    try {
      LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMAT);
      List<Event> events = controller.getEventsOnDate(date);
      displayEvents(events);
    } catch (Exception ex) {
      outputArea.append("Invalid date format. Use yyyy-MM-dd.\n");
    }
  }


  /**
   * Handles the "Create Event" button.
   *
   * @param e the action event by the button click
   */
  private void handleCreateEvent(ActionEvent e) {
    String subject = subjectField.getText().trim();
    String start = startField.getText().trim();
    String end = endField.getText().trim();

    if (subject.isEmpty() || start.isEmpty() || end.isEmpty()) {
      outputArea.append("Error: All fields must be filled.\n");
      return;
    }

    String command = String.format("create event \"%s\" from %s to %s", subject, start, end);
    try {
      controller.processCommand(command);
      controller.processCommand("print events on " + start.split("T")[0]);
    } catch (IllegalArgumentException ex) {
      outputArea.append("Error: " + ex.getMessage() + "\n");
    }
  }

  /**
   * Displays the welcome message.
   * Doesn't do anything in this GUI view.
   */
  @Override
  public void displayWelcomeMessage() {

  }

  /**
   * Displays a list of all commands for creating, editing, and managing events.
   * Doesn't do anything in this GUI view.
   */
  @Override
  public void displayHelp() {

  }

  /**
   * Displays the given events in the output area.
   *
   * @param events the list of events
   */
  public void displayEvents(List<Event> events) {
    clearFields();
    outputArea.setText("");
    if (events.isEmpty()) {
      outputArea.append("No events found.\n");
    } else {
      outputArea.append("=== Events ===\n\n");
      for (Event e : events) {
        outputArea.append(e.toString());
        outputArea.append("\n");
        outputArea.append("-----------------\n");
      }
    }
  }

  /**
   * Displays a single event.
   * Doesn't do anything in this GUI view.
   *
   * @param event the event.
   */
  @Override
  public void displayEvent(Event event) {

  }

  /**
   * Formats a LocalDateTime into a string.
   * Doesn't do anything in this GUI view.
   *
   * @param dateTime the date and time
   * @return empty string
   */
  @Override
  public String formatDateTime(LocalDateTime dateTime) {
    return "";
  }
}
