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
public class CalendarGUIView extends JFrame {
  private final JTextField dateField;
  private final JTextArea outputArea;
  private final ICalendarController controller;

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Constructs the GUI view and links it to the controller.
   *
   * @param controller the controller
   */
  public CalendarGUIView(ICalendarController controller) {
    this.controller = controller;

    setTitle("Calendar Application");
    setSize(600, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout());

    topPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
    dateField = new JTextField(10);
    topPanel.add(dateField);

    JButton viewButton = new JButton("View Schedule");
    viewButton.addActionListener(this::handleViewSchedule);
    topPanel.add(viewButton);

    JButton createButton = new JButton("Create Event");
    createButton.addActionListener(this::handleCreateEvent);
    topPanel.add(createButton);

    add(topPanel, BorderLayout.NORTH);

    outputArea = new JTextArea();
    outputArea.setEditable(false);
    add(new JScrollPane(outputArea), BorderLayout.CENTER);
  }

  /**
   * Handles the "View Schedule" button.
   *
   * @param e the action event by the button click
   */
  private void handleViewSchedule(ActionEvent e) {
    try {
      LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMAT);
      controller.processCommand("print events on " + date);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Handles the "Create Event" button.
   *
   * @param e the action event by the button click
   */
  private void handleCreateEvent(ActionEvent e) {
    String subject = JOptionPane.showInputDialog(this, "Enter event subject:");
    if (subject == null || subject.isBlank()) return;

    String start = JOptionPane.showInputDialog(this, "Enter start datetime (yyyy-MM-dd'T'HH:mm):");
    String end = JOptionPane.showInputDialog(this, "Enter end datetime (yyyy-MM-dd'T'HH:mm):");
    if (start == null || end == null) return;

    try {
      LocalDateTime.parse(start);
      LocalDateTime.parse(end);
      controller.processCommand("create event \"" + subject + "\" from " + start + " to " + end);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Invalid datetime format.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Displays the given events in the output area.
   *
   * @param events the list of events
   */
  public void displayEvents(List<Event> events) {
    outputArea.setText("");
    if (events.isEmpty()) {
      outputArea.append("No events found.\n");
    } else {
      for (Event e : events) {
        outputArea.append(e.toString() + "\n\n");
      }
    }
  }
}