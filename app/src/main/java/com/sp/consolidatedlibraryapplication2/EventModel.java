package com.sp.consolidatedlibraryapplication2;

public class EventModel {
    private String event_name;
    private String event_type;
    private String event_description;
    private String event_date;
    private String event_time;

    // Constructor
    public EventModel(String event_name, String event_type, String event_description, String event_date, String event_time) {
        this.event_name = event_name;
        this.event_type = event_type;
        this.event_description = event_description;
        this.event_date = event_date;
        this.event_time = event_time;
    }

    // Getters
    public String getEventName() { return event_name; }
    public String getEventType() { return event_type; }
    public String getEventDescription() { return event_description; }
    public String getEventDate() { return event_date; }
    public String getEventTime() { return event_time; }
}
