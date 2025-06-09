package org.example.service;

import org.example.DAO.ReminderDAO;
import org.example.DAO.ReminderDAOImpl;
import org.example.model.Reminder;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;

public class ReminderServiceImpl implements ReminderService {

    private final ReminderDAO reminderDAO;

    public ReminderServiceImpl() {
        this.reminderDAO = new ReminderDAOImpl();
    }

    @Override
    public void addReminder(Reminder reminder) throws SQLException {
        reminderDAO.create(reminder);
    }

    @Override
    public Reminder getReminderById(int id) throws SQLException {
        return reminderDAO.getById(id);
    }

    @Override
    public List<Reminder> getAllReminders() throws SQLException {
        return reminderDAO.getAll();
    }

    @Override
    public void updateReminder(Reminder reminder) throws SQLException {
        reminderDAO.update(reminder);
    }

    @Override
    public void markAsCompleted(int reminderId, boolean completed) throws SQLException {
        Reminder reminder = reminderDAO.getById(reminderId);
        if (reminder == null) {
            throw new IllegalArgumentException("Reminder with id " + reminderId + " not found");
        }

        reminder.setCompleted(completed);
        reminderDAO.update(reminder);

        if (completed) {
            ZonedDateTime now = ZonedDateTime.now();


            if (reminder.getEndDatetime() != null && now.isBefore(reminder.getEndDatetime())) {
                reminder.setRepeatUntil(now);
                reminderDAO.update(reminder);
            }
        }
    }

    @Override
    public void deleteReminder(int id) throws SQLException {
        reminderDAO.delete(id);
    }
}
