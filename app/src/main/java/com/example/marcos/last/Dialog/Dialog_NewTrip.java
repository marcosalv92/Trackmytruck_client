package com.example.marcos.last.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.marcos.last.R;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by windows7 on 7/16/2019.
 */
public class Dialog_NewTrip extends DialogFragment implements AdapterView.OnItemSelectedListener {


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String select = (String) parent.getAdapter().getItem(position);
//        Log.println(Log.INFO,"LOG",select);
//        final EditText et_truck_id = (EditText) view.findViewById(R.id.newtrip_truck_id);
//        et_truck_id.setText(select);


//


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public interface NewTrip_DialogInterface {
        void onPossitiveButtonClick(String name,String user,String truck_id);// Eventos Botón Positivo
        void onNegativeButtonClick();// Eventos Botón Negativo
    }
    NewTrip_DialogInterface listener;
    String ruta_name,user_id,truck_id;
    public Dialog_NewTrip(String name,String user,String truck){
        this.ruta_name = name;
        this.user_id = user;
        this.truck_id = truck;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NewTrip_DialogInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialog_NewTrip();
    }

    public AlertDialog createDialog_NewTrip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_new_trip, null);

        List<String> list = new ArrayList<String>();
        list.add("Carro 1");
        list.add("Carro 2");
        list.add("Carro 3");
//        list.add("Material 4");
//        list.add("Material 5");
//        list.add("Material 6");
//        list.add("Material 1");
//        list.add("Material 2");
//        list.add("Material 3");
//        list.add("Material 4");
//        list.add("Material 5");
//        list.add("Material 6");
//        list.add("Material 1");
//        list.add("Material 2");
//        list.add("Material 3");
//        list.add("Material 4");
//        list.add("Material 5");
//        list.add("Material 6");

        builder.setView(view);

        Spinner spin;
        spin = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);


        Button button_crear = (Button) view.findViewById(R.id.button_crear);
        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);
        final EditText et_name = (EditText) view.findViewById(R.id.newtrip_name);
        final EditText et_user_id = (EditText) view.findViewById(R.id.newtrip_user_id);
        final EditText et_truck_id = (EditText) view.findViewById(R.id.newtrip_truck_id);
        et_name.setText(ruta_name);
        et_user_id.setText(user_id);
        et_truck_id.setText(truck_id);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = (String) parent.getAdapter().getItem(position);
                Log.println(Log.INFO,"LOG",select);
                et_truck_id.setText(String.valueOf(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button_crear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...

                        String name = et_name.getText().toString();
                        String user_id = et_user_id.getText().toString();
                        String truck_id = et_truck_id.getText().toString();
                        dismiss();
                        listener.onPossitiveButtonClick(name,user_id,truck_id);

                    }
                }
        );

        button_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        dismiss();
                    }
                }

        );

        return builder.create();
    }
}
