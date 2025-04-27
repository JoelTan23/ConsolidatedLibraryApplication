package com.sp.consolidatedlibraryapplication2;

import java.util.List;

public class RoomBookingResponse {
    private int count;
    private List<RoomBookingModel> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RoomBookingModel> getData() {
        return data;
    }

    public void setData(List<RoomBookingModel> data) {
        this.data = data;
    }
}
