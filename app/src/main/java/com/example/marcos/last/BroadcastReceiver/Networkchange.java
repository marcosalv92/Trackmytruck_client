package com.example.marcos.last.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.marcos.last.R;

import org.w3c.dom.Text;


public class Networkchange extends BroadcastReceiver {

    Context mcontext;
    ApplicationInfo ai;

    public Networkchange() {
    }

    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        final PackageManager pm = context.getPackageManager();

//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo ni = manager.getActiveNetworkInfo();
//        onNetworkChange(ni);
        if (isNetDisponible()){
            if (isOnlineNet()){
                //Toast.makeText(mcontext.getApplicationContext(), "RED DISPONIBLE CON INTERNET", Toast.LENGTH_LONG).show();
                try {
                    //ai = pm.getApplicationInfo(intent.getData().getSchemeSpecificPart(), 0);
                    Intent i = new Intent("broadcastName");
                    // Data pass to activity
                    i.putExtra("appInfo", mcontext.getString(R.string.text_state_network_internet_enable));
                    mcontext.sendBroadcast(i);

                } catch (Exception e){
                    Toast.makeText(mcontext.getApplicationContext(),"Error actualizando estado conexion", Toast.LENGTH_LONG).show();
                }


            }else{
                //Toast.makeText(mcontext.getApplicationContext(), "RED DISPONIBLE SIN INTERNET", Toast.LENGTH_LONG).show();
                try {
                    //ai = pm.getApplicationInfo(intent.getData().getSchemeSpecificPart(), 0);
                    Intent i = new Intent("broadcastName");
                    // Data pass to activity
                    i.putExtra("appInfo", mcontext.getString(R.string.text_state_network_available_without_internet));
                    mcontext.sendBroadcast(i);

                } catch (Exception e){
                    Toast.makeText(mcontext.getApplicationContext(),"Error actualizando estado conexion", Toast.LENGTH_LONG).show();
                }
                }


        }else{
            //Toast.makeText(mcontext.getApplicationContext(), "RED NO DISPONIBLE", Toast.LENGTH_LONG).show();
            try {
                //ai = pm.getApplicationInfo(intent.getData().getSchemeSpecificPart(), 0);
                Intent i = new Intent("broadcastName");
                // Data pass to activity
                i.putExtra("appInfo", mcontext.getString(R.string.text_state_network_disable));
                mcontext.sendBroadcast(i);

            } catch (Exception e){
                Toast.makeText(mcontext.getApplicationContext(),"Error actualizando estado conexion", Toast.LENGTH_LONG).show();
            }
        }

    }

//    public void onNetworkChange(NetworkInfo networkInfo) {
//        if (networkInfo != null) {
//            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
//                Log.d("MenuActivity", "CONNECTED");
//                Toast.makeText(mcontext, "Connected", Toast.LENGTH_LONG).show();
//            } else {
//                Log.d("MenuActivity", "DISCONNECTED");
//                Toast.makeText(mcontext, "Disconnected", Toast.LENGTH_LONG).show();
//            }
//
//        } else {
//            Log.d("MenuActivity", "DISCONNECTED");
//            Toast.makeText(mcontext, "Disconnected", Toast.LENGTH_LONG).show();
//        }
//    }
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
    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
}
