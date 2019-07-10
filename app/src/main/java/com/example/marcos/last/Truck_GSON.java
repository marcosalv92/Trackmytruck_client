package com.example.marcos.last;

import java.util.ArrayList;

/**
 * Created by windows7 on 7/7/2019.
 */
public class Truck_GSON {
    private String truck_id;
    private ArrayList<Record> records;

    public String getTruck_id() {
        return truck_id;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
}
