package org.example.service;

import org.example.model.Reminder;

import java.sql.SQLException;
import java.util.List;

public interface ReminderService {
    void addReminder(Reminder reminder) throws SQLException;
    Reminder getReminderById(int id) throws SQLException;
    List<Reminder> getAllReminders() throws SQLException;
    void updateReminder(Reminder reminder) throws SQLException;


    void markAsCompleted(int reminderId, boolean completed) throws SQLException;

    void deleteReminder(int id) throws SQLException;
}
