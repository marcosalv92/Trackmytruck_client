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

import cz.msebera.android.httpclient.entity.StringEntity;

public class  MyReceiver extends BroadcastReceiver {
    double current_lat, current_lng;
    public LocationManager mlocManager;
    LocationListener mlocListener;
    Context mcontext;
    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Point_Record> point_records = new ArrayList<>();
    Truck_GSON truck_gson = new Truck_GSON();
    private final static String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";
    public String client_http, directory_api;
    public int count_record_offline = 0;
    Point_RecordDbHelper point_recordDb;


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


                /*LocationManager*/ mlocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                /*LocationListener*/ mlocListener = new MyLocationListener(context.getApplicationContext());
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,time*1000, dist, mlocListener);

                Toast.makeText(context.getApplicationContext(), "GPS Working", Toast.LENGTH_LONG).show();

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

            if (loc != null)
            {


                current_lat = loc.getLatitude();
                current_lng = loc.getLongitude();

                SharedPreferences preferences = mcontext.getSharedPreferences("truck_id",mcontext.MODE_PRIVATE);
                client_http = preferences.getString("truck_id","false");

                SharedPreferences preferences_path = mcontext.getSharedPreferences("path",mcontext.MODE_PRIVATE);
                directory_api = preferences_path.getString("path","false");

//                SharedPreferences preferences_conx = mcontext.getSharedPreferences("conx",mcontext.MODE_PRIVATE);
//                String conx =preferences_conx.getString("conx","RED NO DISPONIBLE");



                if (isOnlineNet()){
                    //Envio Https
                    AsyncHttpClient client = new AsyncHttpClient();

                    // PArte con Base de DAtos
                    point_recordDb.addPoint_Record(new Point_Record(client_http,String.valueOf(current_lat),String.valueOf(current_lng),fechaHoraActual()));
                    point_records = point_recordDb.getAllPoint_Record();

                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("trip_id",client_http);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray pushrecordsArray = new JSONArray();
                    for(int i = point_records.size()-1; i >=0 ; i--)
                    {
                        JSONObject record_new = new JSONObject();
                        try {
                            record_new.put("latitude", point_records.get(i).getLatitude());
                            record_new.put("longitude", point_records.get(i).getLongitude());
                            record_new.put("recorded_at", point_records.get(i).getRecorder_at());
                            pushrecordsArray.put(record_new);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    try {
                        jsonParams.put("records", pushrecordsArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringEntity bodyjson = null;
                    try {
                        bodyjson = new StringEntity(jsonParams.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    client.post(mcontext, "https://"+ directory_api, bodyjson, "application/json",
                            new CaptureResponserHandler(mcontext));
                    point_recordDb.deleteAll();
                    point_records.clear();
                    count_record_offline = 0;



                    //EJEEMPLO DARIO
//                        JSONObject jsonParams = new JSONObject();
//                        // Param1
//                        jsonParams.put("truck_id",client_http);
//
//                        JSONArray recordsArray = new JSONArray();
//                        JSONObject record1 = new JSONObject();
//                        record1.put("latitude",String.valueOf(current_lat));
//                        record1.put("longitude",String.valueOf(current_lng));
//                        record1.put("recorded_at",fechaHoraActual());
//                        recordsArray.put(record1);
//
//
////                        JSONObject record2 = new JSONObject();
////                        record2.put("latitude","22.6");
////                        record2.put("longitude","-83.6");
////                        record2.put("recorded_at","2019-07-08 11:45:45");
////                        recordsArray.put(record2);
//
//                        //Param2
//                        jsonParams.put("records", recordsArray);
//
//
//                        StringEntity eee = new StringEntity(jsonParams.toString());
//
//                        client.post(mcontext, "https://www.trackmytruck.tk/api/locations", eee, "application/json",
//                                new CaptureResponserHandler(mcontext));


                    //Sin DATABASE

//                    Record new_record = new Record();
//                    new_record.setLatitude(current_lat);
//                    new_record.setLongitude(current_lng);
//                    new_record.setRecorded_at(fechaHoraActual());
//                    records.add(new_record);
//
//                        //AsyncHttpClient client2 = new AsyncHttpClient();
//                        JSONObject jsonParams = new JSONObject();
//                        try {
//                            jsonParams.put("trip_id",client_http);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        JSONArray pushrecordsArray = new JSONArray();
//                        for(int i = records.size()-1; i >=0 ; i--)
//                        {
//                            JSONObject record_new = new JSONObject();
//                            try {
//                                record_new.put("latitude", String.valueOf(records.get(i).getLatitude()));
//                                record_new.put("longitude", String.valueOf(records.get(i).getLongitude()));
//                                record_new.put("recorded_at", records.get(i).getRecorder_at());
//                                pushrecordsArray.put(record_new);
//                            }catch (JSONException e) {
//                            e.printStackTrace();
//                            }
//
//
//                        }
//                        try {
//                            jsonParams.put("records", pushrecordsArray);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        StringEntity bodyjson = null;
//                        try {
//                            bodyjson = new StringEntity(jsonParams.toString());
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        client.post(mcontext, "https://"+ directory_api, bodyjson, "application/json",
//                                new CaptureResponserHandler(mcontext));
//                        records.clear();
//                        count_record_offline = 0;

                }else{
                    //almacenar variable location;

//                      Sin DATABASE
//                    truck_gson.setTruck_id(client_http);
//                    Record record = new Record();
//                    record.setLatitude(current_lat);
//                    record.setLongitude(current_lng);
//                    record.setRecorded_at(fechaHoraActual());
//                      records.add(record);
//                      truck_gson.setRecords(records);


                    // CON Database
                    Point_Record new_point = new Point_Record(client_http,String.valueOf(current_lat),String.valueOf(current_lng),fechaHoraActual());
                    point_recordDb.addPoint_Record(new_point);
                    int cant  = (int) point_recordDb.countAllRecord();
                    Toast.makeText(mcontext,"Record store: "+ String.valueOf(cant), Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                String text = "Localizacion no encontrada";

                Toast.makeText(mcontext, text, Toast.LENGTH_LONG).show();
//                mlocManager.removeUpdates(mlocListener);
            }
        }

        public String fechaHoraActual(){
            Date date = new Date();
            //date.setHours(date.getHours() + 8);
            //System.out.println(date);
            SimpleDateFormat simpDate;
            simpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //System.out.println(simpDate.format(date));
            return simpDate.format(date);

            //SimpleDateFormat( "yyyy-MM-dd hh:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
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
