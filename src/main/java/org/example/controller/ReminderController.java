package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.model.Reminder;
import org.example.service.ReminderService;
import org.example.service.ReminderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ReminderController {

    private static final Logger logger = LoggerFactory.getLogger(ReminderController.class);

    private final ReminderService reminderService = new ReminderServiceImpl();

    @FXML private TableView<Reminder> reminderTable;
    @FXML private TableColumn<Reminder, String> titleColumn;
    @FXML private TableColumn<Reminder, String> descriptionColumn;
    @FXML private TableColumn<Reminder, String> startDateColumn;
    @FXML private TableColumn<Reminder, String> endDateColumn;
    @FXML private TableColumn<Reminder, String> completedColumn;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private CheckBox allDayCheckBox;
    @FXML private Button saveButton;
    @FXML private Button markCompletedButton;
    @FXML private Label statusLabel;

    private ObservableList<Reminder> reminderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle()));

        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        startDateColumn.setCellValueFactory(cellData -> {
            Reminder reminder = cellData.getValue();
            String dateStr = (reminder.getStartDatetime() != null)
                    ? reminder.getStartDatetime().toLocalDate().toString()
                    : "";
            return new SimpleStringProperty(dateStr);
        });

        endDateColumn.setCellValueFactory(cellData -> {
            Reminder reminder = cellData.getValue();
            String dateStr = (reminder.getEndDatetime() != null)
                    ? reminder.getEndDatetime().toLocalDate().toString()
                    : "";
            return new SimpleStringProperty(dateStr);
        });

        completedColumn.setCellValueFactory(cellData -> {
            boolean completed = cellData.getValue().isCompleted();
            String status = completed ? "завершено" : "в процессе";
            return new javafx.beans.property.SimpleStringProperty(status);
        });


        loadReminders();

        reminderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillForm(newSelection);
            }
        });

        saveButton.setOnAction(e -> saveReminder());
        markCompletedButton.setOnAction(e -> markSelectedReminderCompleted());
    }

    private void loadReminders() {
        try {
            List<Reminder> reminders = reminderService.getAllReminders();
            reminderList.setAll(reminders);
            reminderTable.setItems(reminderList);
            statusLabel.setText("Загружено " + reminders.size() + " напоминаний");
        } catch (SQLException e) {
            logger.error("Ошибка загрузки напоминаний: {}", e.getMessage(), e);
            statusLabel.setText("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void fillForm(Reminder reminder) {
        titleField.setText(reminder.getTitle());
        descriptionField.setText(reminder.getDescription());
        if (reminder.getStartDatetime() != null) {
            startDatePicker.setValue(reminder.getStartDatetime().toLocalDate());
            startTimeField.setText(reminder.getStartDatetime().toLocalTime().toString());
        } else {
            startDatePicker.setValue(null);
            startTimeField.clear();
        }

        if (reminder.getEndDatetime() != null) {
            endDatePicker.setValue(reminder.getEndDatetime().toLocalDate());
            endTimeField.setText(reminder.getEndDatetime().toLocalTime().toString());
        } else {
            endDatePicker.setValue(null);
            endTimeField.clear();
        }

        allDayCheckBox.setSelected(reminder.isAllDay());
    }

    private void saveReminder() {
        try {
            Reminder selected = reminderTable.getSelectionModel().getSelectedItem();
            Reminder reminder = (selected != null) ? selected : new Reminder();

            reminder.setTitle(titleField.getText());
            reminder.setDescription(descriptionField.getText());

            if (startDatePicker.getValue() != null && !startTimeField.getText().isEmpty()) {
                ZonedDateTime start = ZonedDateTime.of(
                        startDatePicker.getValue(),
                        LocalTime.parse(startTimeField.getText()),
                        ZoneId.systemDefault());
                reminder.setStartDatetime(start);
            } else {
                reminder.setStartDatetime(null);
            }

            if (endDatePicker.getValue() != null && !endTimeField.getText().isEmpty()) {
                ZonedDateTime end = ZonedDateTime.of(
                        endDatePicker.getValue(),
                        LocalTime.parse(endTimeField.getText()),
                        ZoneId.systemDefault());
                reminder.setEndDatetime(end);
            } else {
                reminder.setEndDatetime(null);
            }

            reminder.setAllDay(allDayCheckBox.isSelected());

            if (reminder.getId() == 0) {
                reminderService.addReminder(reminder);
                logger.info("Напоминание добавлено: {}", reminder);
                statusLabel.setText("Напоминание добавлено");
            } else {
                reminderService.updateReminder(reminder);
                logger.info("Напоминание обновлено: {}", reminder);
                statusLabel.setText("Напоминание обновлено");
            }
            loadReminders();
            clearForm();

        } catch (Exception e) {
            logger.error("Ошибка сохранения напоминания: {}", e.getMessage(), e);
            statusLabel.setText("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void markSelectedReminderCompleted() {
        Reminder selected = reminderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите напоминание");
            return;
        }
        try {
            boolean currentStatus = selected.isCompleted();
            reminderService.markAsCompleted(selected.getId(), !currentStatus);
            logger.info("Статус напоминания изменен на выполнено: {}", selected);
            statusLabel.setText("Статус выполнено изменен");
            loadReminders();
        } catch (SQLException e) {
            logger.error("Ошибка изменения статуса напоминания: {}", e.getMessage(), e);
            statusLabel.setText("Ошибка: " + e.getMessage());
        }
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
        allDayCheckBox.setSelected(false);
        reminderTable.getSelectionModel().clearSelection();
    }
}
