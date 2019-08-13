package com.example.marcos.last.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcos.last.BroadcastReceiver.CaptureResponserHandler;
import com.example.marcos.last.Dialog.Dialog_NewTrip;
import com.example.marcos.last.R;
import com.example.marcos.last.database.Point_Record;
import com.example.marcos.last.database.Point_RecordDbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class MainActivity extends AppCompatActivity implements Dialog_NewTrip.NewTrip_DialogInterface {

    TextView ed_id, ed_truck_id, ed_user_id, ed_name,ed_user_data;
    EditText ed_updatetime, ed_updatedist, ed_path, ed_coordinate;
    Button startgps, stopgps, bton_newtrip, btnBackground;
    public String conexion_Stage = "";
    public String trip_id, user_id, truck_id, ruta_name;
    public String directory;
    public String frec_t, frec_dist;
    public View mProgressView;
    public TextView mProgresTextView;
    public View mLoginFormView;
    //--Prueba variables en el activity gps
    ArrayList<Point_Record> point_records = new ArrayList<>();
    public String token;
    Point_RecordDbHelper point_recordDb;
    AsyncHttpClient client;
    JSONObject jsonParams;
    JSONArray pushrecordsArray;
    double current_lat, current_lng;
    long time_gps;
    public LocationManager mlocManager;
    LocationListener mlocListener;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // Redirección al Login
        SharedPreferences pref = getSharedPreferences(getString(R.string.name_preference_user_inf), MODE_PRIVATE);
        token = pref.getString(getString(R.string.token), "false");
        if (token.equals("false")) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        //finish();
        ed_id = (TextView) findViewById(R.id.textView_id);
        ed_user_id = (TextView) findViewById(R.id.textViewUser_id);
        ed_truck_id = (TextView) findViewById(R.id.textViewTruck_id);
        ed_name = (TextView) findViewById(R.id.textView_name);
        ed_user_data = (TextView)findViewById(R.id.textView_user);

        ed_updatetime = (EditText) findViewById(R.id.editTextTime);
        ed_updatedist = (EditText) findViewById(R.id.editTextDist);
        ed_path = (EditText) findViewById(R.id.editTextpaht);
        ed_coordinate = (EditText) findViewById(R.id.editText);




        startgps = (Button) findViewById(R.id.buttonStart);
        stopgps = (Button) findViewById(R.id.buttonStop);
        bton_newtrip = (Button) findViewById(R.id.button_newtrip);
        btnBackground = (Button) findViewById(R.id.buttonBackground);

        mLoginFormView = findViewById(R.id.login_form2);
        mProgressView = findViewById(R.id.login_progress);
        mProgresTextView = (TextView) findViewById(R.id.textView_progress2);

        SharedPreferences preferences_user = getSharedPreferences(getString(R.string.name_preference_user_inf), MODE_PRIVATE);
        SharedPreferences.Editor editor_user = preferences_user.edit();
        String name_user = preferences_user.getString(getString(R.string.first_name), "Error");
        String last_name_user = preferences_user.getString(getString(R.string.last_name), "Error");
        ed_user_data.setText("Hola "+name_user+" "+last_name_user);



        //Almacenar Variables de Entrada  trip_id  truck_id user_id

        user_id = preferences_user.getString(getString(R.string.id), "false");
        if (user_id.equals("false")) {
            user_id = "3";
            editor_user.putString(getString(R.string.id), user_id);
            editor_user.commit();
        } else {
            ed_user_id.setText(user_id);

        }


        SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_trips), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        trip_id = preferences.getString(getString(R.string.trip_id), "false");
        if (trip_id.equals("false")) {
            trip_id = "25";
            editor.putString(getString(R.string.trip_id), trip_id);
            editor.commit();
        } else {
            ed_id.setText(trip_id);

        }



        truck_id = preferences.getString(getString(R.string.truck_id), "false");
        if (truck_id.equals("false")) {
            truck_id = "3";
            editor.putString(getString(R.string.truck_id), truck_id);
            editor.commit();
        } else {
            ed_truck_id.setText(truck_id);

        }
        ruta_name = preferences.getString(getString(R.string.ruta_name), "false");
        if (ruta_name.equals("false")) {
            ruta_name = "New Trip";
            editor.putString(getString(R.string.ruta_name), ruta_name);
            editor.commit();
        } else {
            ed_name.setText(ruta_name);

        }


        ////Almacenar Variables de Entrada directory, frec_t, frec_dist


        directory = preferences.getString(getString(R.string.path), "false");
        frec_dist = preferences.getString(getString(R.string.frec_dist), "false");
        frec_t = preferences.getString(getString(R.string.frec_t), "false");

        if (directory.equals("false")) {
            directory = getString(R.string.url_api_send_location_multiple);
            editor.putString(getString(R.string.path), getString(R.string.url_api_send_location_multiple));
            editor.commit();
        } else {
            ed_path.setText(directory);

        }
        if (frec_dist.equals("false")) {
            frec_dist = "10";
            editor.putString(getString(R.string.frec_dist), "10");
            editor.commit();
        } else {
            ed_updatedist.setText(frec_dist);

        }
        if (frec_t.equals("false")) {
            frec_t = "10";
            editor.putString(getString(R.string.frec_t), "10");
            editor.commit();
        } else {
            ed_updatetime.setText(frec_t);

        }

        registerReceiver(broadcastReceiver, new IntentFilter("NetworkChange"));

        startgps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (conexion_Stage.equals(getString(R.string.text_state_network_internet_enable))) {
                    if (isLocationEnabled(getApplicationContext())) {
                        updateAllDataActivity(trip_id, user_id, truck_id, ruta_name);

                        if (frec_dist.equals("") || frec_t.equals("")) {
                            createSimpleDialog(getString(R.string.message_update_time_dist_gps)).show();

                        } else {
                            int intfrecuencia = Integer.parseInt(frec_t);
                            int intDist = Integer.parseInt(frec_dist);
//
//                            Intent intent = new Intent();
//                            intent.setAction("com.journaldev.CUSTOM_INTENT");
//                            intent.putExtra("time", intfrecuencia);
//                            intent.putExtra("dist", intDist);
//                            sendBroadcast(intent);
//
                            ed_updatetime.setFocusable(false);
                            ed_updatedist.setFocusable(false);
                            ed_path.setFocusable(false);
                            bton_newtrip.setVisibility(View.INVISIBLE);
                            startgps.setVisibility(View.INVISIBLE);
                            btnBackground.setVisibility(View.VISIBLE);
                            stopgps.setVisibility(View.VISIBLE);

                            point_recordDb = new Point_RecordDbHelper(getApplicationContext());
                            try {
                                SharedPreferences pref = getSharedPreferences(getString(R.string.name_preference_user_inf), MODE_PRIVATE);
                                token = pref.getString(getString(R.string.token), "false");
                                client = new AsyncHttpClient(true, 80, 443);
                                client.addHeader("Authorization", "Bearer " + token);
                                /*LocationManager*/
                                mlocManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                                /*LocationListener*/
                                mlocListener = new MyLocationListener();
                                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intfrecuencia * 1000, intDist, mlocListener);

                                Toast.makeText(getApplicationContext(), "GPS Working", Toast.LENGTH_LONG).show();

                            } catch (Exception ex) {
                                showMessage(ex.getMessage());
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    } else {
                        showAlertGPSorNetwork_disable("Location","Su ubicación esta desactivada.\npor favor active su ubicación..");
                        //createSimpleDialog(getString(R.string.message_gps_disable)).show();

                    }

                } else if (conexion_Stage.equals(getString(R.string.text_state_network_available_without_internet))) {
                    createSimpleDialog(getString(R.string.message_network_active_without_internet)).show();

                } else {
                    showAlertGPSorNetwork_disable("Data Mobile","Sus datos móviles están desactivados.\npor favor activelos..");
                    //createSimpleDialog(getString(R.string.message_network_disable)).show();

                }

            }
        });

        stopgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bton_newtrip.setVisibility(View.VISIBLE);
                startgps.setVisibility(View.VISIBLE);
                stopgps.setVisibility(View.INVISIBLE);
                btnBackground.setVisibility(View.INVISIBLE);
                //System.exit(0);
                mlocManager.removeUpdates(mlocListener);
                ed_updatetime.setFocusableInTouchMode(true);
                ed_updatedist.setFocusableInTouchMode(true);
                ed_path.setFocusableInTouchMode(true);

            }

        });
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            }
        });

        bton_newtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_NewTrip newTrip = new Dialog_NewTrip(ruta_name, user_id, truck_id);
                newTrip.show(getSupportFragmentManager(), "DialogNewTrip");

            }
        });


        Intent intent_ini = new Intent();
        intent_ini.setAction("com.journaldev.CUSTOM_INTENT2");
        sendBroadcast(intent_ini);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            if (stopgps.getVisibility() == View.INVISIBLE) {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_user_inf), MODE_PRIVATE);
                String token = preferences.getString(getString(R.string.token), "false");
                if (!token.equals("false")) {
                    mProgresTextView.setText(getString(R.string.action_sign_out));
                    showProgress(true);
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    client.addHeader("Authorization", "Bearer " + token);

                    client.post(getApplicationContext(), getString(R.string.url_api_logout), null, new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();


                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            //
                            SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_user_inf), MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(getString(R.string.token), "false");
                            editor.commit();
                            //showProgress(false);
                            try {
                                String msg = new String(responseBody, "UTF-8");
                                showMessage(msg);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            unregisterReceiver(broadcastReceiver);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            showProgress(false);
                            //                    try {
                            //                        String msg = new String(responseBody, "UTF-8");
                            //                        showMessage(msg);
                            //                    } catch (UnsupportedEncodingException e) {
                            //                        e.printStackTrace();
                            //                    }
                            showMessage("Falló conexion con el servidor...verifique conexión");

                        }
                    });


                }
            } else {
                createSimpleDialog(getString(R.string.message_stop_update_gps)).show();

            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // -- Mostar mensajes en Toast
    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    //-- Function for create progress cicle
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mProgresTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgresTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mProgresTextView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgresTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    //-- Positive Button of Create Trips
    @Override
    public void onPossitiveButtonClick(final String name, final String user, final String truck) {

        mProgresTextView.setText(R.string.text_progress_newtrip);
        showProgress(true);

        if (isOnlineNet()) {

            SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_user_inf), MODE_PRIVATE);
            String token = preferences.getString("token", "false");
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.addHeader("Authorization", "Bearer " + token);
            jsonParams = new JSONObject();
            try {
                jsonParams.put("name", name);
                jsonParams.put(getString(R.string.user_id), user);
                jsonParams.put(getString(R.string.truck_id), truck);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity bodyjson = null;
            try {
                bodyjson = new StringEntity(jsonParams.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            client.post(getApplicationContext(), getString(R.string.url_api_newtrip), bodyjson, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                    showProgress(false);

                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
//                            String new_name = (String) json.get("name");
//                            String new_user_id = (String) json.get(getString(R.string.user_id));
//                            String new_truck_id = (String) json.get(getString(R.string.truck_id));
//                            String updated_at = (String) json.get("updated_at");
//                            String created_at = (String) json.get("created_at");
                        Integer id = (Integer) json.get("id");

                        trip_id = String.valueOf(id);

                        ed_id.setText(trip_id);
                        ed_user_id.setText(user);
                        ed_truck_id.setText(truck);
                        ed_name.setText(name);


                        updateAllDataActivity(trip_id, user, truck, name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showMessage("Nuevo viaje creado:" + name);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    showProgress(false);
                    showMessage("Falló conexion con el servidor...verifique conexión");
                    Dialog_NewTrip newTrip = new Dialog_NewTrip(name, user, truck);
                    if (isRunning(getApplicationContext())) {
                        newTrip.show(getSupportFragmentManager(), "DialogTRYNewTrip");
                    }


                }


            });
        } else {


            showProgress(false);

            createSimpleDialog(getString(R.string.message_network_disable)).show();

        }


    }

    @Override
    public void onNegativeButtonClick() {

    }

    // -- AlertDialog of Information
    public AlertDialog createSimpleDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.setTitle_AlertDialog_Information))
                .setMessage(msg)
                .setPositiveButton(getString(R.string.setPositiveButton_AlertDialog),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.onPossitiveButtonClick();
                            }
                        });

        return builder.create();
    }

    // -- If have conection to innternet
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    // --If GPS is enable
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    // -- If Activity is running in first plane
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }


    //  -- Listener of GPS
    public class MyLocationListener implements LocationListener {

        public MyLocationListener() {
        }

        public void onLocationChanged(Location loc) {

            loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (loc != null) {

                current_lat = loc.getLatitude();
                current_lng = loc.getLongitude();
                time_gps = loc.getTime();
                String time_new = fechaHoraActual(time_gps);

                if (isOnlineNet()) {

                    // PArte con Base de DAtos
                    long cant_record = point_recordDb.countAllRecord();
                    jsonParams = new JSONObject();
                    pushrecordsArray = new JSONArray();
                    if (cant_record == 0) {
                        try {
                            jsonParams.put(getString(R.string.trip_id), trip_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject record_new = new JSONObject();
                        try {
                            record_new.put("latitude", current_lat);
                            record_new.put("longitude", current_lng);
                            record_new.put("recorded_at", time_new);
                            pushrecordsArray.put(record_new);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        point_recordDb.addPoint_Record(new Point_Record(trip_id, String.valueOf(current_lat), String.valueOf(current_lng), time_new));
                        point_records = point_recordDb.getAllPoint_Record();
                        int loopTripId = Integer.parseInt(point_records.get(0).getTrip_id());
                        try {
                            jsonParams.put(getString(R.string.trip_id), point_records.get(0).getTrip_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for (int i = 0; i < point_records.size(); i++) {

                            int compare_id = Integer.parseInt(point_records.get(i).getTrip_id());
                            if (loopTripId != compare_id) {
                                loopTripId = Integer.parseInt(point_records.get(i).getTrip_id());
                                sendJson();
                                jsonParams = new JSONObject();
                                try {
                                    jsonParams.put(getString(R.string.trip_id), point_records.get(i).getTrip_id());
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
                    }
                    sendJson();
                    point_recordDb.deleteAll();
                    point_records.clear();

                } else {
                    // CON Database
                    Point_Record new_point = new Point_Record(trip_id, String.valueOf(current_lat), String.valueOf(current_lng), time_new);
                    point_recordDb.addPoint_Record(new_point);
                    int cant = (int) point_recordDb.countAllRecord();
                    Toast.makeText(getApplicationContext(), "Record store: " + String.valueOf(cant), Toast.LENGTH_LONG).show();
                }

            } else {
                String text = "Localizacion no encontrada";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }

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


    //  -- function that send post whit location
    public void sendJson() {

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
            client.post(getApplicationContext(), "https://" + directory, bodyjson, "application/json",
                    new CaptureResponserHandler(getApplicationContext(), point_records));
        } catch (Error e) {
            showMessage("Exception client post location");
        }

    }

    //-- funtion that return la fecha
    public String fechaHoraActual(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpDate;
        simpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpDate.format(date);
    }


    private void updateAllDataActivity(String newtrip_id, String newuser_id, String newtruck_id, String newruta_name) {

        //trip_id = ed_id.getText().toString();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_trips), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String client_store = preferences.getString(getString(R.string.trip_id), "false");
        if (!newtrip_id.equals(client_store)) {
            editor.putString(getString(R.string.trip_id), newtrip_id);
            editor.commit();
            trip_id = newtrip_id;
        }
        //user_id = ed_user_id.getText().toString();
//        String user_store = preferences.getString(getString(R.string.user_id), "false");
//        if (!newuser_id.equals(user_store)) {
//            editor.putString(getString(R.string.user_id), newuser_id);
//            editor.commit();
//            user_id = newuser_id;
//        }

        //truck_id = ed_truck_id.getText().toString();
        String truck_store = preferences.getString(getString(R.string.truck_id), "false");
        if (!newtruck_id.equals(truck_store)) {
            editor.putString(getString(R.string.truck_id), newtruck_id);
            editor.commit();
            truck_id = newtruck_id;
        }
        //ruta_name = ed_name.getText().toString();
        String name_store = preferences.getString(getString(R.string.ruta_name), "false");
        if (!newruta_name.equals(name_store)) {
            editor.putString(getString(R.string.ruta_name), newruta_name);
            editor.commit();
            ruta_name = newruta_name;
        }

        directory = ed_path.getText().toString();

        String directory_store = preferences.getString(getString(R.string.path), getString(R.string.url_api_send_location_multiple));
        if (!directory.equals(directory_store)) {
            editor.putString(getString(R.string.path), directory);
            editor.commit();
        }
        frec_t = ed_updatetime.getText().toString();
        String frectime_store = preferences.getString(getString(R.string.frec_t), "10");
        if (!frec_t.equals(frectime_store)) {
            editor.putString(getString(R.string.frec_t), frec_t);
            editor.commit();
        }


        frec_dist = ed_updatedist.getText().toString();
        String frecdist_store = preferences.getString(getString(R.string.frec_dist), "10");
        if (!frec_dist.equals(frecdist_store)) {
            editor.putString(getString(R.string.frec_dist), frec_dist);
            editor.commit();
        }
    }
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // get applicationInfo
            String recibido = intent.getExtras().getString("appInfo");
            ed_coordinate.setText(recibido);
            conexion_Stage = recibido;
//            SharedPreferences preferences = getSharedPreferences("conx", MODE_PRIVATE);
//            String mconx = preferences.getString("conx", getString(R.string.text_state_network_disable));
//            if (!conexion_Stage.equals(mconx)) {
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("conx", conexion_Stage);
//                editor.commit();
//            }

        }
    };
    private void showAlertGPSorNetwork_disable(final String isLocation, String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable "+isLocation)
                .setMessage(msg)
                .setPositiveButton("Config de "+isLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (isLocation.equals("Location")) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setComponent(new ComponentName("com.android.settings",
                                    "com.android.settings.Settings$DataUsageSummaryActivity"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
}
