package org.example.DAO;

import org.example.model.Reminder;

import java.sql.SQLException;
import java.util.List;

public interface ReminderDAO {
    void create(Reminder reminder) throws SQLException;
    Reminder getById(int id) throws SQLException;
    List<Reminder> getAll() throws SQLException;
    void update(Reminder reminder) throws SQLException;
    void delete(int id) throws SQLException;
}
