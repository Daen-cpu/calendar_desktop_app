package org.example.DAO;

import org.example.model.Event;

import java.sql.SQLException;
import java.util.List;

public interface EventDAO {
    void create(Event event) throws SQLException;
    Event getById(int id) throws SQLException;
    List<Event> getAll() throws SQLException;
    void update(Event event) throws SQLException;
    void delete(int id) throws SQLException;
}
