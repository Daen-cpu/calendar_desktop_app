package org.example.DAO;

import org.example.model.Reminder;
import org.example.util.DBConnection;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAOImpl implements ReminderDAO {

    @Override
    public void create(Reminder reminder) throws SQLException {
        String sql = "INSERT INTO reminders (title, description, start_datetime, end_datetime, is_all_day, is_completed, repeat_until) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, reminder.getTitle());
            stmt.setString(2, reminder.getDescription());
            stmt.setTimestamp(3, toTimestamp(reminder.getStartDatetime()));
            stmt.setTimestamp(4, toTimestamp(reminder.getEndDatetime()));
            stmt.setBoolean(5, reminder.isAllDay());
            stmt.setBoolean(6, reminder.isCompleted());
            stmt.setTimestamp(7, toTimestamp(reminder.getRepeatUntil()));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reminder.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Reminder getById(int id) throws SQLException {
        String sql = "SELECT * FROM reminders WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToReminder(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Reminder> getAll() throws SQLException {
        String sql = "SELECT * FROM reminders ORDER BY start_datetime";

        List<Reminder> reminders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reminders.add(mapRowToReminder(rs));
            }
        }
        return reminders;
    }

    @Override
    public void update(Reminder reminder) throws SQLException {
        String sql = "UPDATE reminders SET title = ?, description = ?, start_datetime = ?, end_datetime = ?, is_all_day = ?, is_completed = ?, repeat_until = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reminder.getTitle());
            stmt.setString(2, reminder.getDescription());
            stmt.setTimestamp(3, toTimestamp(reminder.getStartDatetime()));
            stmt.setTimestamp(4, toTimestamp(reminder.getEndDatetime()));
            stmt.setBoolean(5, reminder.isAllDay());
            stmt.setBoolean(6, reminder.isCompleted());
            stmt.setTimestamp(7, toTimestamp(reminder.getRepeatUntil()));
            stmt.setInt(8, reminder.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reminders WHERE id = ?";

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

    private Reminder mapRowToReminder(ResultSet rs) throws SQLException {
        Reminder r = new Reminder();
        r.setId(rs.getInt("id"));
        r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description"));
        r.setStartDatetime(toZonedDateTime(rs.getTimestamp("start_datetime")));
        r.setEndDatetime(toZonedDateTime(rs.getTimestamp("end_datetime")));
        r.setAllDay(rs.getBoolean("is_all_day"));
        r.setCompleted(rs.getBoolean("is_completed"));
        r.setRepeatUntil(toZonedDateTime(rs.getTimestamp("repeat_until")));
        r.setCreatedAt(toZonedDateTime(rs.getTimestamp("created_at")));
        r.setUpdatedAt(toZonedDateTime(rs.getTimestamp("updated_at")));
        return r;
    }
}
