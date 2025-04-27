package com.sp.consolidatedlibraryapplication2;

public class RoomBookingModel {
    private String booking_date;
    private String user_id;
    private String booking_time;
    private String room_type;

    public RoomBookingModel(String user_id,String booking_date, String booking_time, String room_type) {
        this.user_id = user_id;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.room_type = room_type;
    }

    public String getBookingDate() {
        return booking_date;
    }

    public String getUserId() {
        return user_id;
    }

    public String getBookingTime() {
        return booking_time;
    }

    public String getRoomType() {
        return room_type;
    }


    // **Setters**
    public void setUserId(String user_id) { this.user_id = user_id; }
    public void setBookingTime(String booking_time) { this.booking_time = booking_time; }
    public void setRoomType(String room_type) { this.room_type = room_type; }
}
