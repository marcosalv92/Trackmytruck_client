package com.example.marcos.last.List;

import java.util.UUID;

/**
 * Created by windows7 on 7/7/2019.
 */
public class Record {
    private double latitude;
    private double longitude;
    private String recorded_at;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRecorder_at() {
        return recorded_at;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRecorded_at(String recorder_at) {
        this.recorded_at = recorder_at;
    }
}
