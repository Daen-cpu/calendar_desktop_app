package org.example.controller;

import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.model.Event;
import org.example.service.EventService;
import org.example.service.EventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService = new EventServiceImpl();
    private final ObservableList<Event> eventList = FXCollections.observableArrayList();

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, String> descriptionColumn;
    @FXML private TableColumn<Event, String> startDateColumn;
    @FXML private TableColumn<Event, String> endDateColumn;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private CheckBox allDayCheckBox;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle()));

        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        startDateColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            String dateStr = (event.getStartDatetime() != null)
                    ? event.getStartDatetime().toLocalDate().toString()
                    : "";
            return new SimpleStringProperty(dateStr);
        });

        endDateColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            String dateStr = (event.getEndDatetime() != null)
                    ? event.getEndDatetime().toLocalDate().toString()
                    : "";
            return new SimpleStringProperty(dateStr);
        });


        loadEvents();

        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillForm(newVal);
        });

        saveButton.setOnAction(e -> saveEvent());
        deleteButton.setOnAction(e -> deleteSelectedEvent());
    }

    private void loadEvents() {
        try {
            List<Event> events = eventService.getAllEvents();
            eventList.setAll(events);
            eventTable.setItems(eventList);
            statusLabel.setText("Загружено " + events.size() + " событий");
        } catch (SQLException e) {
            logger.error("Ошибка загрузки событий: {}", e.getMessage(), e);
            statusLabel.setText("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void fillForm(Event event) {
        titleField.setText(event.getTitle());
        descriptionField.setText(event.getDescription());

        if (event.getStartDatetime() != null) {
            startDatePicker.setValue(event.getStartDatetime().toLocalDate());
            startTimeField.setText(event.getStartDatetime().toLocalTime().toString());
        }

        if (event.getEndDatetime() != null) {
            endDatePicker.setValue(event.getEndDatetime().toLocalDate());
            endTimeField.setText(event.getEndDatetime().toLocalTime().toString());
        }



        allDayCheckBox.setSelected(event.isAllDay());
    }

    private void saveEvent() {
        try {
            Event selected = eventTable.getSelectionModel().getSelectedItem();
            Event event = (selected != null) ? selected : new Event();

            event.setTitle(titleField.getText());
            event.setDescription(descriptionField.getText());

            event.setStartDatetime(parseDateTime(startDatePicker, startTimeField));
            event.setEndDatetime(parseDateTime(endDatePicker, endTimeField));
            event.setAllDay(allDayCheckBox.isSelected());

            if (event.getId() == 0) {
                eventService.addEvent(event);
                logger.info("Событие добавлено: {}", event);
                statusLabel.setText("Событие добавлено");
            } else {
                eventService.updateEvent(event);
                logger.info("Событие обновлено: {}", event);
                statusLabel.setText("Событие обновлено");
            }

            clearForm();
            loadEvents();

        } catch (Exception e) {
            logger.error("Ошибка сохранения события: {}", e.getMessage(), e);
            statusLabel.setText("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void deleteSelectedEvent() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите событие для удаления");
            return;
        }

        try {
            eventService.deleteEvent(selected.getId());
            logger.info("Событие удалено: {}", selected);
            statusLabel.setText("Событие удалено");
            loadEvents();
            clearForm();
        } catch (SQLException e) {
            logger.error("Ошибка удаления события: {}", e.getMessage(), e);
            statusLabel.setText("Ошибка удаления: " + e.getMessage());
        }
    }

    private ZonedDateTime parseDateTime(DatePicker datePicker, TextField timeField) {
        if (datePicker.getValue() != null && !timeField.getText().isEmpty()) {
            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeField.getText());
            return ZonedDateTime.of(date, time, ZoneId.systemDefault());
        }
        return null;
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
        allDayCheckBox.setSelected(false);
        eventTable.getSelectionModel().clearSelection();
    }
}
