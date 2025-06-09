package org.example.model;

import java.time.ZonedDateTime;

public class Event {

    private int id;
    private String title;
    private String description;
    private ZonedDateTime startDatetime;
    private ZonedDateTime endDatetime;
    private boolean isAllDay;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Event() {
    }

    public Event(int id, String title, String description, ZonedDateTime startDatetime,
                 ZonedDateTime endDatetime, boolean isAllDay,
                 ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.isAllDay = isAllDay;
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

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
