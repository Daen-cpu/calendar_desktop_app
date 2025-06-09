package org.example.service;

import org.example.DAO.EventDAO;
import org.example.DAO.EventDAOImpl;
import org.example.model.Event;

import java.sql.SQLException;
import java.util.List;

public class EventServiceImpl implements EventService {

    private final EventDAO eventDAO;

    public EventServiceImpl() {
        this.eventDAO = new EventDAOImpl();
    }

    @Override
    public void addEvent(Event event) throws SQLException {
        eventDAO.create(event);
    }

    @Override
    public Event getEventById(int id) throws SQLException {
        return eventDAO.getById(id);
    }

    @Override
    public List<Event> getAllEvents() throws SQLException {
        return eventDAO.getAll();
    }

    @Override
    public void updateEvent(Event event) throws SQLException {
        eventDAO.update(event);
    }

    @Override
    public void deleteEvent(int id) throws SQLException {
        eventDAO.delete(id);
    }
}
