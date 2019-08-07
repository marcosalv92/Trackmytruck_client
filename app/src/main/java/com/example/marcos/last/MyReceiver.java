package com.example.marcos.last;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.marcos.last.database.Point_Record;
import com.example.marcos.last.database.Point_RecordDbHelper;
import com.loopj.android.http.AsyncHttpClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.entity.StringEntity;

public class  MyReceiver extends BroadcastReceiver {
    double current_lat, current_lng;
    long time_gps;
    public LocationManager mlocManager;
    LocationListener mlocListener;
    Context mcontext;
//    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Point_Record> point_records = new ArrayList<>();
//    Truck_GSON truck_gson = new Truck_GSON();
    private final static String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";
    public String trip_id, directory_api;
    Point_RecordDbHelper point_recordDb;
    AsyncHttpClient client;
    JSONObject jsonParams;
    JSONArray pushrecordsArray;
//    public String action;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        point_recordDb = new Point_RecordDbHelper(context);

        try
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                    int time = (int) bundle.get("time");
                    int dist = (int) bundle.get("dist");

                /*LocationManager*/
                    mlocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                /*LocationListener*/
                    mlocListener = new MyLocationListener(context.getApplicationContext());
                    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time * 1000, dist, mlocListener);

                    Toast.makeText(context.getApplicationContext(), "GPS Working", Toast.LENGTH_LONG).show();
//                }
            }

        }
        catch(Exception ex)
        {
                Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (Throwable throwable) {
                throwable.printStackTrace();
        }
        // throw new UnsupportedOperationException("Not yet implemented");
    }
    public class MyLocationListener implements LocationListener {

        public MyLocationListener(Context context){
        mcontext = context;

        }

        public void onLocationChanged(Location loc) {

            loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (loc != null) {

                    current_lat = loc.getLatitude();
                    current_lng = loc.getLongitude();
                    time_gps = loc.getTime();
                    String time_string = String.valueOf(time_gps);

                    SharedPreferences preferences = mcontext.getSharedPreferences("trips", mcontext.MODE_PRIVATE);
                    trip_id = preferences.getString("trip_id", "false");

                    SharedPreferences preferences_path = mcontext.getSharedPreferences("path", mcontext.MODE_PRIVATE);
                    directory_api = preferences_path.getString("path", "false");


                    if (isOnlineNet()) {

                        // PArte con Base de DAtos
                        String time_new = fechaHoraActual(time_gps);
                        point_recordDb.addPoint_Record(new Point_Record(trip_id, String.valueOf(current_lat), String.valueOf(current_lng),time_new));
                        point_records = point_recordDb.getAllPoint_Record();
                        int loopTripId = Integer.parseInt(point_records.get(0).getTrip_id());
                        jsonParams = new JSONObject();
                        try {
                            jsonParams.put("trip_id", point_records.get(0).getTrip_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pushrecordsArray = new JSONArray();

                        for (int i = 0; i < point_records.size(); i++) {

                            int compare_id = Integer.parseInt(point_records.get(i).getTrip_id());
                            if (loopTripId != compare_id){
                                loopTripId = Integer.parseInt(point_records.get(i).getTrip_id());
                                sendJson();
                                jsonParams = new JSONObject();
                                try {
                                    jsonParams.put("trip_id", point_records.get(i).getTrip_id());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pushrecordsArray = new JSONArray();
                            }

                            JSONObject record_new = new JSONObject();
                            try {
                                record_new.put("latitude", point_records.get(i).getLatitude());
                                record_new.put("longitude", point_records.get(i).getLongitude());
                                record_new.put("recorded_at", point_records.get(i).getRecorder_at());
                                pushrecordsArray.put(record_new);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        sendJson();
                        point_recordDb.deleteAll();
                        point_records.clear();

                    } else {
                        // CON Database
                        //String time_new = fechaHoraActual(time_gps);
                        Point_Record new_point = new Point_Record(trip_id, String.valueOf(current_lat), String.valueOf(current_lng),fechaHoraActual(time_gps));
                        point_recordDb.addPoint_Record(new_point);
                        int cant = (int) point_recordDb.countAllRecord();
                        Toast.makeText(mcontext, "Record store: " + String.valueOf(cant), Toast.LENGTH_LONG).show();
                    }

                } else {
                    String text = "Localizacion no encontrada";

                    Toast.makeText(mcontext, text, Toast.LENGTH_LONG).show();
                    //                mlocManager.removeUpdates(mlocListener);
                }

        }

        public void sendJson(){

            client = new AsyncHttpClient(true,80,443);

            try {
                jsonParams.put("locations", pushrecordsArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringEntity bodyjson = null;
            try {
                bodyjson = new StringEntity(jsonParams.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                client.post(mcontext, "https://" + directory_api, bodyjson, "application/json",
                        new CaptureResponserHandler(mcontext,point_records));
            }catch (Error e){
                Toast.makeText(mcontext, "Exception client post", Toast.LENGTH_LONG).show();
            }

        }

        public String fechaHoraActual(long time){
            Date date = new Date(time);
            //date.setHours(date.getHours() + 8);
            //System.out.println(date);
            SimpleDateFormat simpDate;
            simpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //System.out.println(simpDate.format(date));
            return simpDate.format(date);


        }
        public Boolean isOnlineNet() {

            try {
                Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

                int val           = p.waitFor();
                boolean reachable = (val == 0);
                return reachable;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }






}
