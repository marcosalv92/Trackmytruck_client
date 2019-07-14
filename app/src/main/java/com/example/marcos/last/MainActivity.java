package com.example.marcos.last;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcos.last.database.Point_RecordDbHelper;


public class MainActivity extends ActionBarActivity {
    EditText ed_id, ed_updatetime, ed_updatedist, ed_path, ed_coordinate;
    Button startgps, stopgps;
    public String conexion_Stage;
    public String client_http;
    public String directory;
    public String frec_t,frec_dist;

    Truck_GSON truck_GSON = new Truck_GSON();
//    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finish();
        ed_id = (EditText) findViewById(R.id.editText_id);
        ed_updatetime = (EditText) findViewById(R.id.editTextTime);
        ed_updatedist = (EditText) findViewById(R.id.editTextDist);
        ed_path = (EditText) findViewById(R.id.editTextpaht);
        ed_coordinate = (EditText) findViewById(R.id.editText);

        startgps = (Button) findViewById(R.id.buttonStart);
        stopgps = (Button) findViewById(R.id.buttonStop);


        //Almacenar Variables de Entrada truck id

        SharedPreferences preferences = getSharedPreferences("truck_id",MODE_PRIVATE);
        client_http = preferences.getString("truck_id","false");
        if (client_http.equals("false")){
            client_http = "25";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("truck_id","25");
            editor.commit();
        }else{
            client_http = preferences.getString("truck_id","25");
            ed_id.setText(client_http);

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
            directory = preferences_path.getString("path","www.trackmytruck.tk/api/newlocation");
            ed_path.setText(directory);

        }
        if (frec_dist.equals("false")){
            frec_dist = "10";
            SharedPreferences.Editor editor_dist = preferences_path.edit();
            editor_dist.putString("frec_dist","10");
            editor_dist.commit();
        }else{
            frec_dist = preferences_path.getString("frec_dist","10");
            ed_updatedist.setText(frec_dist);

        }
        if (frec_t.equals("false")){
            frec_t = "10";
            SharedPreferences.Editor editor_time = preferences_path.edit();
            editor_time.putString("frec_t","10");
            editor_time.commit();
        }else{
            frec_t = preferences_path.getString("frec_t","10");
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


//                if (conexion_Stage.equals("RED DISPONIBLE CON INTERNET")) {
                if(true){
                    //Toast.makeText(getApplicationContext(), "RED DISPONIBLE CON INTERNET", Toast.LENGTH_LONG).show();
                    client_http = ed_id.getText().toString();
                    SharedPreferences preferences = getSharedPreferences("truck_id",MODE_PRIVATE);
                    String client_store = preferences.getString("truck_id","false");
                    if (client_http != client_store){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("truck_id",client_http);
                        editor.commit();
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

                    if (frec_dist.equals("") || frec_t.equals("")) {
                        Toast.makeText(getApplicationContext(), "Introduzca intervalo requerido", Toast.LENGTH_LONG).show();
                    } else {
                        int intfrecuencia = Integer.parseInt(frec_t);
                        int intDist = Integer.parseInt(frec_dist);
                        //                  Intent intent = new Intent();
                        Intent intent = new Intent();
                        intent.setAction("com.journaldev.CUSTOM_INTENT");
                        intent.putExtra("time", intfrecuencia);
                        intent.putExtra("dist", intDist);
                        sendBroadcast(intent);
//                        Intent intent = new Intent();
//                        intent.putExtra("time", intfrecuencia);
//                        intent.putExtra("dist", intDist);
//                        intent.setAction("com.journaldev.CUSTOM_SERVICE");
//                        startService(intent);
//                        startService(new Intent(getApplicationContext(), GPS_Service.class));
                    }
                    startgps.setVisibility(View.INVISIBLE);
                    stopgps.setVisibility(View.VISIBLE);
                }else if (conexion_Stage.equals("RED DISPONIBLE SIN INTERNET")){
                    Toast.makeText(getApplicationContext(), "RED DISPONIBLE SIN INTERNET", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), "RED NO DISPONIBLE", Toast.LENGTH_LONG).show();
                }



            }
        });

        stopgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startgps.setVisibility(View.VISIBLE);
                stopgps.setVisibility(View.INVISIBLE);
//                Intent intent = new Intent();
//                intent.setAction("com.journaldev.CUSTOM_SERVICE");
//                stopService(intent);
                Point_RecordDbHelper prDb = new Point_RecordDbHelper(getApplicationContext());
                //prDb.deleteAll();
                System.exit(0);
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

        return super.onOptionsItemSelected(item);
    }

//    public Boolean isOnlineNet() {
//
//        try {
//            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
//
//            int val           = p.waitFor();
//            boolean reachable = (val == 0);
//            return reachable;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }
//    private boolean isNetDisponible() {
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
//
//        return (actNetInfo != null && actNetInfo.isConnected());
//    }


}
