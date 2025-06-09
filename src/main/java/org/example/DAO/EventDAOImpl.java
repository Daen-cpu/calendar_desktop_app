package org.example.DAO;

import org.example.model.Event;
import org.example.util.DBConnection;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {

    @Override
    public void create(Event event) throws SQLException {
        String sql = "INSERT INTO events (title, description, start_datetime, end_datetime, is_all_day) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, toTimestamp(event.getStartDatetime()));
            stmt.setTimestamp(4, toTimestamp(event.getEndDatetime()));
            stmt.setBoolean(5, event.isAllDay());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Event getById(int id) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEvent(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Event> getAll() throws SQLException {
        String sql = "SELECT * FROM events ORDER BY start_datetime";

        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public void update(Event event) throws SQLException {
        String sql = "UPDATE events SET title = ?, description = ?, start_datetime = ?, end_datetime = ?, is_all_day = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, toTimestamp(event.getStartDatetime()));
            stmt.setTimestamp(4, toTimestamp(event.getEndDatetime()));
            stmt.setBoolean(5, event.isAllDay());
            stmt.setInt(6, event.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Timestamp toTimestamp(ZonedDateTime zdt) {
        if (zdt == null) return null;
        return Timestamp.from(zdt.toInstant());
    }

    private ZonedDateTime toZonedDateTime(Timestamp ts) {
        if (ts == null) return null;
        return ts.toInstant().atZone(ZoneId.systemDefault());
    }

    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        Event e = new Event();
        e.setId(rs.getInt("id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setStartDatetime(toZonedDateTime(rs.getTimestamp("start_datetime")));
        e.setEndDatetime(toZonedDateTime(rs.getTimestamp("end_datetime")));
        e.setAllDay(rs.getBoolean("is_all_day"));
        e.setCreatedAt(toZonedDateTime(rs.getTimestamp("created_at")));
        e.setUpdatedAt(toZonedDateTime(rs.getTimestamp("updated_at")));
        return e;
    }
}
