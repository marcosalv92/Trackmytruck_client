package com.example.marcos.last.database;

import android.provider.BaseColumns;

/**
 * Created by windows7 on 7/12/2019.
 */
public class Point_RecordContract {
    public static abstract class Point_RecordEntry implements BaseColumns {
        public static final String TABLE_NAME ="pointrecord";
        public static final String ID = "id";
        public static final String TRIP_ID = "trip_id";
        public static final String LATITUD = "latitude";
        public static final String LONGITUD = "longitude";
        public static final String RECORDED_AT = "recorded_at";


    }
}
