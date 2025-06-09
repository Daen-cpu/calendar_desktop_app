package org.example.model;

import java.time.ZonedDateTime;

public class Reminder {

    private int id;
    private String title;
    private String description;
    private ZonedDateTime startDatetime;
    private ZonedDateTime endDatetime;
    private boolean isAllDay;
    private boolean isCompleted;
    private ZonedDateTime repeatUntil;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Reminder() {
    }

    public Reminder(int id, String title, String description, ZonedDateTime startDatetime,
                    ZonedDateTime endDatetime, boolean isAllDay, boolean isCompleted,
                    ZonedDateTime repeatUntil, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.isAllDay = isAllDay;
        this.isCompleted = isCompleted;
        this.repeatUntil = repeatUntil;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ZonedDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(ZonedDateTime startDatetime) { this.startDatetime = startDatetime; }

    public ZonedDateTime getEndDatetime() { return endDatetime; }
    public void setEndDatetime(ZonedDateTime endDatetime) { this.endDatetime = endDatetime; }

    public boolean isAllDay() { return isAllDay; }
    public void setAllDay(boolean allDay) { isAllDay = allDay; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public ZonedDateTime getRepeatUntil() { return repeatUntil; }
    public void setRepeatUntil(ZonedDateTime repeatUntil) { this.repeatUntil = repeatUntil; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
