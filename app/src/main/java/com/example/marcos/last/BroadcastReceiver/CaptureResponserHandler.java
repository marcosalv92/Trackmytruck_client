package com.example.marcos.last.BroadcastReceiver;

import android.content.Context;
import android.widget.Toast;

import com.example.marcos.last.database.Point_Record;
import com.example.marcos.last.database.Point_RecordDbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by windows7 on 7/7/2019.
 */
public class CaptureResponserHandler extends AsyncHttpResponseHandler {
    Context mcontext;
    public  ArrayList<Point_Record> array_pointRecord;

    public CaptureResponserHandler(Context context, ArrayList<Point_Record> array_point) {
        mcontext = context;
        this.array_pointRecord = array_point;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {

            String msg = new String(responseBody, "UTF-8");
            mostraMensaje(msg);
            //point_recordDbHelper.deleteAll();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

//        Point_RecordDbHelper records_db = new Point_RecordDbHelper(mcontext);
//
//        for (int i = 0; i < array_pointRecord.size(); i++) {
//            records_db.addPoint_Record(array_pointRecord.get(i));
//        }
//
//        int cant_record = (int) records_db.countAllRecord();
//        Toast.makeText(mcontext.getApplicationContext(),"Fallo el envio de una localizacion...", Toast.LENGTH_LONG).show();

//        ArrayList<Point_Record> loco = array_pointRecord;
//        int cant = loco.size();
        mostraMensaje("Fallo el envio de una localizacion...");
//
//   try {
//            String msg = new String(responseBody, "UTF-8");
//            mostraMensaje(msg);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }
    public void mostraMensaje(String msg){
        Toast.makeText(mcontext,msg,Toast.LENGTH_LONG).show();
    }
}

