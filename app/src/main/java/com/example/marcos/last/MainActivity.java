package com.example.marcos.last;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcos.last.Dialog.Dialog_NewTrip;
import com.example.marcos.last.database.Point_RecordDbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class MainActivity extends ActionBarActivity implements Dialog_NewTrip.NewTrip_DialogInterface{

    TextView ed_id,ed_truck_id,ed_user_id,ed_name;
    EditText ed_updatetime, ed_updatedist, ed_path, ed_coordinate;
    Button startgps, stopgps,bton_newtrip,btnBackground;
    public String conexion_Stage;
    public String trip_id,user_id,truck_id,ruta_name;
    public String directory;
    public String frec_t,frec_dist;
    ProgressDialog progressDialog = null;
    public View mProgressView;
    public TextView mProgresTextView;
    public View mLoginFormView;
//    MyReceiver myReceiver = new MyReceiver();

    Truck_GSON truck_GSON = new Truck_GSON();
//    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
         super.onCreate(savedInstanceState);
        // Redirección al Login
        SharedPreferences pref = getSharedPreferences("trips",MODE_PRIVATE);
        String token = pref.getString("token","false");
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



        //Almacenar Variables de Entrada  trip_id  truck_id user_id

        SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
        trip_id = preferences.getString("trip_id","false");
        if (trip_id.equals("false")){
            trip_id = "25";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("trip_id",trip_id);
            editor.commit();
        }else{
            //trip_id = preferences.getString("trip_id","25");
            ed_id.setText(trip_id);

        }

        user_id = preferences.getString("user_id","false");
        if (user_id.equals("false")){
            user_id = "3";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id",user_id);
            editor.commit();
        }else{
            //user_id = preferences.getString("user_id","3");
            ed_user_id.setText(user_id);

        }

        truck_id = preferences.getString("truck_id","false");
        if (truck_id.equals("false")){
            truck_id = "3";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("truck_id",truck_id);
            editor.commit();
        }else{
            //truck_id = preferences.getString("truck_id","3");
            ed_truck_id.setText(truck_id);

        }
        ruta_name = preferences.getString("ruta_name","false");
        if (ruta_name.equals("false")){
            ruta_name = "New Trip";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ruta_name",ruta_name);
            editor.commit();
        }else{
            //ruta_name = preferences.getString("ruta_name","New Trip");
            ed_name.setText(ruta_name);

        }


        ////Almacenar Variables de Entrada directory, frec_t, frec_dist

        SharedPreferences preferences_path = getSharedPreferences("path",MODE_PRIVATE);
        directory = preferences_path.getString("path","false");
        frec_dist = preferences_path.getString("frec_dist","false");
        frec_t = preferences_path.getString("frec_t","false");

        if (directory.equals("false")){
            directory = "www.trackmytruck.tk/api/newlocation";
            SharedPreferences.Editor editor_path = preferences_path.edit();
            editor_path.putString("path","www.trackmytruck.tk/api/newlocation");
            editor_path.commit();
        }else{
            //directory = preferences_path.getString("path","www.trackmytruck.tk/api/newlocation");
            ed_path.setText(directory);

        }
        if (frec_dist.equals("false")){
            frec_dist = "10";
            SharedPreferences.Editor editor_dist = preferences_path.edit();
            editor_dist.putString("frec_dist","10");
            editor_dist.commit();
        }else{
            //frec_dist = preferences_path.getString("frec_dist","10");
            ed_updatedist.setText(frec_dist);

        }
        if (frec_t.equals("false")){
            frec_t = "10";
            SharedPreferences.Editor editor_time = preferences_path.edit();
            editor_time.putString("frec_t","10");
            editor_time.commit();
        }else{
            //frec_t = preferences_path.getString("frec_t","10");
            ed_updatetime.setText(frec_t);

        }


        BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // get applicationInfo
                String recibido = intent.getExtras().getString("appInfo");
                ed_coordinate.setText(recibido);
                conexion_Stage = recibido;
                SharedPreferences preferences = getSharedPreferences("conx",MODE_PRIVATE);
                String mconx = preferences.getString("conx","RED NO DISPONIBLE");
                if (conexion_Stage != mconx) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("conx", conexion_Stage);
                    editor.commit();
                }

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("broadcastName"));

        startgps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (conexion_Stage.equals("RED DISPONIBLE CON INTERNET")) {
                    if(isLocationEnabled(getApplicationContext())){
                        updateAllDataActivity(trip_id, user_id, truck_id, ruta_name);

                        if (frec_dist.equals("") || frec_t.equals("")) {
                            createSimpleDialog("Introduzca intervalo requerido\n Time(s) y Dist(m)").show();

                        } else {
                            int intfrecuencia = Integer.parseInt(frec_t);
                            int intDist = Integer.parseInt(frec_dist);

                            Intent intent = new Intent();
                            intent.setAction("com.journaldev.CUSTOM_INTENT");
                            intent.putExtra("time", intfrecuencia);
                            intent.putExtra("dist", intDist);
                            sendBroadcast(intent);

                            bton_newtrip.setVisibility(View.INVISIBLE);
                            startgps.setVisibility(View.INVISIBLE);
                            btnBackground.setVisibility(View.VISIBLE);
                            stopgps.setVisibility(View.VISIBLE);
                        }
                    }else{
                        createSimpleDialog("GPS INACTIVO \n" +
                            "\n" +
                            "ACTIVE GPS!!!").show();

                    }

                }else if (conexion_Stage.equals("RED DISPONIBLE SIN INTERNET")){
                    createSimpleDialog("RED DISPONIBLE SIN INTERNET \n" +
                            "\n" +
                            "VERIFIQUE CONEXIÓN!!!").show();

                }else{
                    createSimpleDialog("RED NO DISPONIBLE\n" +
                            "\n" +
                            "VERIFIQUE CONEXIÓN!!!").show();

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
                System.exit(0);
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

              Dialog_NewTrip newTrip = new Dialog_NewTrip(ruta_name,user_id,truck_id);
                newTrip.show(getSupportFragmentManager(),"DialogNewTrip");

            }
        });


        Intent intent_ini = new Intent();
        intent_ini.setAction("com.journaldev.CUSTOM_INTENT2");
        sendBroadcast(intent_ini);




    }

    private void updateAllDataActivity(String newtrip_id,String newuser_id,String newtruck_id,String newruta_name) {

        //trip_id = ed_id.getText().toString();
        SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
        String client_store = preferences.getString("trip_id","false");
        if (newtrip_id != client_store){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("trip_id",newtrip_id);
            editor.commit();
            trip_id = newtrip_id;
        }
        //user_id = ed_user_id.getText().toString();
        String user_store = preferences.getString("user_id","false");
        if (newuser_id != user_store){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id",newuser_id);
            editor.commit();
            user_id = newuser_id;
        }

        //truck_id = ed_truck_id.getText().toString();
        String truck_store = preferences.getString("truck_id","false");
        if (newtruck_id != truck_store){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("truck_id",newtruck_id);
            editor.commit();
            truck_id = newtruck_id;
        }
        //ruta_name = ed_name.getText().toString();
        String name_store = preferences.getString("ruta_name","false");
        if (newruta_name != name_store){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ruta_name",newruta_name);
            editor.commit();
            ruta_name = newruta_name;
        }

        directory = ed_path.getText().toString();
        SharedPreferences preferences_path = getSharedPreferences("path",MODE_PRIVATE);
        String directory_store = preferences_path.getString("path","www.trackmytruck.tk/api/newlocation");
        if (directory != directory_store){
            SharedPreferences.Editor editor_path = preferences_path.edit();
            editor_path.putString("path",directory);
            editor_path.commit();
        }
        frec_t = ed_updatetime.getText().toString();
        String frectime_store = preferences_path.getString("frec_t","10");
        if (frec_t != frectime_store){
            SharedPreferences.Editor editor_time = preferences_path.edit();
            editor_time.putString("frec_t",frec_t);
            editor_time.commit();
        }



        frec_dist = ed_updatedist.getText().toString();
        String frecdist_store = preferences_path.getString("frec_dist","10");
        if (frec_dist != frecdist_store){
            SharedPreferences.Editor editor_dist = preferences_path.edit();
            editor_dist.putString("frec_dist",frec_dist);
            editor_dist.commit();
        }
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
            if (stopgps.getVisibility() == View.INVISIBLE){
                SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
                String token = preferences.getString("token","false");
                if (!token.equals("false")){
                    mProgresTextView.setText("Logout");
                    showProgress(true);
                    AsyncHttpClient client = new AsyncHttpClient(true,80,443);
                    client.addHeader("Authorization","Bearer " + token);

                    client.post(getApplicationContext(), "https://www.trackmytruck.tk/api/logout",null,new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();


                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
    //
                            SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token","false");
                            editor.commit();
                            showProgress(false);
                            try {
                                String msg = new String(responseBody, "UTF-8");
                                mostrarmensaje(msg);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();


    //                    Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            showProgress(false);
    //                    try {
    //                        String msg = new String(responseBody, "UTF-8");
    //                        mostrarmensaje(msg);
    //                    } catch (UnsupportedEncodingException e) {
    //                        e.printStackTrace();
    //                    }
                            Toast.makeText(getApplicationContext(), "Falló conexion con el servidor...verifique conexión", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }else {
                createSimpleDialog("DETENER ACTUALIZACIONES GPS").show();
//                Toast.makeText(getApplicationContext(), "Detener envío de actualizaciones primero...", Toast.LENGTH_LONG).show();
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void mostrarmensaje(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

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

    public AlertDialog createSimpleDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Información")
                .setMessage(msg)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.onPossitiveButtonClick();
                            }
                        });

        return builder.create();
    }




    @Override
    public void onPossitiveButtonClick(final String name, final String user, final String truck) {

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Verificando new trip...");
//        progressDialog.show();
        mProgresTextView.setText("Creando Nuevo Viaje...");
        showProgress(true);

        if (isOnlineNet()) {

                SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
                String token = preferences.getString("token","false");
                AsyncHttpClient client = new AsyncHttpClient(true,80,443);
                client.addHeader("Authorization","Bearer " + token);
                final JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("name",name);
                    jsonParams.put("user_id",user);
                    jsonParams.put("truck_id",truck);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringEntity bodyjson = null;
                try {
                    bodyjson = new StringEntity(jsonParams.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(getApplicationContext(), "https://www.trackmytruck.tk/api/trips", bodyjson, "application/json", new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


//                        progressDialog.dismiss();
                        showProgress(false);

                        try {
                            JSONObject json = new JSONObject(new String(responseBody));
//                            String new_name = (String) json.get("name");
//                            String new_user_id = (String) json.get("user_id");
//                            String new_truck_id = (String) json.get("truck_id");
//                            String updated_at = (String) json.get("updated_at");
//                            String created_at = (String) json.get("created_at");
                            Integer id = (Integer) json.get("id");

                            trip_id = String.valueOf(id);

                            ed_id.setText(trip_id);
                            ed_user_id.setText(user);
                            ed_truck_id.setText(truck);
                            ed_name.setText(name);


                            updateAllDataActivity(trip_id,user,truck,name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),"Nuevo viaje creado:"+name, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        progressDialog.dismiss();
                        showProgress(false);

                            Toast.makeText(getApplicationContext(), "Falló conexion con el servidor...verifique conexión", Toast.LENGTH_LONG).show();
                            Dialog_NewTrip newTrip = new Dialog_NewTrip(name, user, truck);
                        if (isRunning(getApplicationContext())) {
                            newTrip.show(getSupportFragmentManager(), "DialogTRYNewTrip");
                        }


                    }


                });
        }else{

//            progressDialog.dismiss();
            showProgress(false);

            createSimpleDialog("RED NO DISPONIBLE\n" +
                    "\n" +
                    "VERIFIQUE CONEXIÓN!!!").show();

        }


    }

    @Override
    public void onNegativeButtonClick() {

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
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }
}
