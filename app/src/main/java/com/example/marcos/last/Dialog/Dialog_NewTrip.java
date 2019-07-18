package com.example.marcos.last.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.marcos.last.R;

/**
 * Created by windows7 on 7/16/2019.
 */
public class Dialog_NewTrip extends DialogFragment {



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

        builder.setView(view);

        Button button_crear = (Button) view.findViewById(R.id.button_crear);
        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);
        final EditText et_name = (EditText) view.findViewById(R.id.newtrip_name);
        final EditText et_user_id = (EditText) view.findViewById(R.id.newtrip_user_id);
        final EditText et_truck_id = (EditText) view.findViewById(R.id.newtrip_truck_id);
        et_name.setText(ruta_name);
        et_user_id.setText(user_id);
        et_user_id.setText(truck_id);

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
