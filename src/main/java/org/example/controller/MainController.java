package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainController {

    private Stage primaryStage;

    @FXML
    private AnchorPane formPane;

    @FXML
    private Label statusLabel;

    // Инициализация контроллера
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Обработчик для выхода
    @FXML
    public void handleExit() {
        if (primaryStage != null) {
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    // Обработчик для информации о приложении
    @FXML
    public void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Ваш персональный календарь");
        alert.setContentText("Это приложение для создания и управления вашим временем.\nВерсия 1.0");
        alert.showAndWait();
    }

    // Метод для загрузки формы для напоминания
    @FXML
    public void loadReminderForm() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/reminder_form.fxml"));
            AnchorPane reminderForm = loader.load();

            formPane.getChildren().clear();
            formPane.getChildren().add(reminderForm);


            statusLabel.setText("Загружена форма напоминания");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка загрузки формы напоминания");
        }
    }


    @FXML
    public void loadEventForm() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/event_form.fxml"));
            AnchorPane eventForm = loader.load();


            formPane.getChildren().clear();
            formPane.getChildren().add(eventForm);


            statusLabel.setText("Загружена форма события");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка загрузки формы события");
        }
    }

    @FXML
    public void initialize() {
        statusLabel.setText("Готово к работе");
        System.out.println("MainController инициализирован.");
    }
}