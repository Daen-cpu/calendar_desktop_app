package org.example.service;

import org.example.model.Event;

import java.sql.SQLException;
import java.util.List;

public interface EventService {
    void addEvent(Event event) throws SQLException;
    Event getEventById(int id) throws SQLException;
    List<Event> getAllEvents() throws SQLException;
    void updateEvent(Event event) throws SQLException;
    void deleteEvent(int id) throws SQLException;
}
