package com.example.marcos.last;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by windows7 on 7/7/2019.
 */
public class CaptureResponserHandler extends AsyncHttpResponseHandler {
    Context mcontext;
    public CaptureResponserHandler(Context context) {
        mcontext = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String msg = new String(responseBody, "UTF-8");
            mostraMensaje(msg);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Toast.makeText(mcontext.getApplicationContext(),"Fallo el envio de una localizacion", Toast.LENGTH_LONG).show();
//        try {
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

