package com.example.marcos.last.database;

import android.content.ContentValues;

import java.util.UUID;

/**
 * Created by windows7 on 7/12/2019.
 */
public class Point_Record {
    private String id;
    private String trip_id;
    private String latitude;
    private String longitude;
    private String recorded_at;


    public Point_Record(String trip,String lat,String lon,String recorded){
        this.id = UUID.randomUUID().toString();
        this.trip_id = trip;
        this.latitude = lat;
        this.longitude = lon;
        this.recorded_at = recorded;


    }

    public String getId() {
        return id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getRecorder_at() {
        return recorded_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRecorded_at(String recorder_at) {
        this.recorded_at = recorder_at;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Point_RecordContract.Point_RecordEntry.ID, id);
        values.put(Point_RecordContract.Point_RecordEntry.TRIP_ID, trip_id);
        values.put(Point_RecordContract.Point_RecordEntry.LATITUD, latitude);
        values.put(Point_RecordContract.Point_RecordEntry.LONGITUD, longitude);
        values.put(Point_RecordContract.Point_RecordEntry.RECORDED_AT, recorded_at);
        return values;
    }
}

