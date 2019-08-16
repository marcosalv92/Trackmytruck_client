package com.example.marcos.last.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Property;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcos.last.List.Record;
import com.example.marcos.last.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * A login screen that offers login via email/password.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world","marcos@test.com:marcos"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView textView_Progress;
    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);



        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
//        Button btonlogout = (Button) findViewById(R.id.buttonlogout);
//        btonlogout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogout();
//            }
//        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        textView_Progress = (TextView)findViewById(R.id.textView_progress);


    }

//    private void attemptLogout() {
//        SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
//        String token = preferences.getString("token","false");
//        if (!token.equals("false")){
//            showProgress(true);
//            client = new AsyncHttpClient(true,80,443);
//            client.addHeader("Authorization","Bearer " + token);
//
//            client.post(getApplicationContext(), "https://www.trackmytruck.tk/api/logout",null,new AsyncHttpResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    super.onStart();
//
//
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
////
//
//                    showProgress(false);
//                    try {
//                        String msg = new String(responseBody, "UTF-8");
//                        mostrarmensaje(msg);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    SharedPreferences preferences = getSharedPreferences("trips",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("token","false");
//                    editor.commit();
//
////                    Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    showProgress(false);
////                    try {
////                        String msg = new String(responseBody, "UTF-8");
////                        mostrarmensaje(msg);
////                    } catch (UnsupportedEncodingException e) {
////                        e.printStackTrace();
////                    }
//                Toast.makeText(getApplicationContext(), "Error..", Toast.LENGTH_LONG).show();
//                }
//            });
//
//
//        }
//
//    }

    private void mostrarmensaje(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        textView_Progress.setText(R.string.message_login);
        showProgress(true);
        client = new AsyncHttpClient(true,80,443);

        final JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("email",email);
            jsonParams.put("password",password);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity bodyjson = null;
        try {
            bodyjson = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(), getString(R.string.url_api_login), bodyjson, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            progressDialog.dismiss();

                SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_user_inf),MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    String token = (String) json.get("access_token");
//                            String new_user_id = (String) json.get("user_id");
//                            String new_truck_id = (String) json.get("truck_id");
//                            String updated_at = (String) json.get("updated_at");
//                            String created_at = (String) json.get("created_at");

                    editor.putString(getString(R.string.token),token);
                    editor.commit();
                    client.addHeader("Authorization","Bearer " + token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                client.get(getApplicationContext(), getString(R.string.url_api_infor_user),new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        textView_Progress.setText(R.string.message_login_user);

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//

//                        showProgress(false);


                        SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_user_inf),MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(new String(responseBody));

                            Integer id = (Integer) json.get(getString(R.string.id));
                            String first_name = (String) json.get(getString(R.string.first_name));
                            String last_name = (String) json.get(getString(R.string.last_name));
                            String email = (String) json.get(getString(R.string.email));
                            String phone = (String) json.get(getString(R.string.phone));
                            Integer active = (Integer) json.get(getString(R.string.active));
                            Integer client_id = (Integer) json.get(getString(R.string.client_id));
                            String profile_image = (String) json.get(getString(R.string.profile_image));
                            String created_at = (String) json.get(getString(R.string.created_at));
                            String updated_at = (String) json.get(getString(R.string.updated_at));
                            String RealProfileImage = (String) json.get(getString(R.string.RealProfileImage));

                            editor.putString(getString(R.string.id),String.valueOf(id));
                            editor.putString(getString(R.string.first_name),first_name);
                            editor.putString(getString(R.string.last_name),last_name);
                            editor.putString(getString(R.string.email),email);
                            editor.putString(getString(R.string.phone),phone);
                            editor.putString(getString(R.string.active),String.valueOf(active));
                            editor.putString(getString(R.string.client_id),String.valueOf(client_id));
                            editor.putString(getString(R.string.profile_image),profile_image);
                            editor.putString(getString(R.string.created_at),created_at);
                            editor.putString(getString(R.string.updated_at),updated_at);
                            editor.putString(getString(R.string.RealProfileImage),RealProfileImage);
                            editor.commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), "Datos de Usuarios recibidos...", Toast.LENGTH_LONG).show();


                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                        Toast.makeText(getApplicationContext(), "Falló recibiendo datos de usuarios..", Toast.LENGTH_LONG).show();
                    }
                });

//                Toast.makeText(getApplicationContext(), "Token Recibido", Toast.LENGTH_LONG).show();





            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);

                Toast.makeText(getApplicationContext(), "Falló autentificación.... revise email y password", Toast.LENGTH_LONG).show();
            }
        });

//        // Check for a valid password, if the user entered one.
//        if (/*!TextUtils.isEmpty(password) &&*/ !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
//        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            textView_Progress.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    textView_Progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            textView_Progress.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(),"Nuevo viaje creado", Toast.LENGTH_LONG).show();
                //finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
// Ejemplo para estraer informacion de JSON
//    String json_string = "{\n" +
//            "\t\"id\" : 46,\n" +
//            "\t\"nombre\" : \"Miguel\",\n" +
//            "\t\"empresa\" : \"Autentia\",\n" +
//            "\t\"vacaciones\":" +
//            "[\n" +
//            "\t\t\t\t\t{\n" +
//            "\t\t\t\t\t\t\"inicio\" : \"Holaaaaa\",\n" +
//            "\t\t\t\t\t\t\"fin\" : \"Pedrooooo\",\n" +
//            "\t\t\t\t\t\t\"d\" : 5\n" +
//            "\t\t\t\t\t},\n" +
//            "\t\t\t\t\t{\n" +
//            "\t\t\t\t\t\t\"inicio\" : \"23/08/2012\",\n" +
//            "\t\t\t\t\t\t\"fin\" : \"29/08/2012\",\n" +
//            "\t\t\t\t\t\t\"d\" : 88888888\n" +
//            "\t\t\t\t\t}\n" +
//            "\t\t\t\t]\n" +
//            "}";
//try {
//final Gson gson = new Gson();
//        JSONObject new_json = new JSONObject(json_string);
//        JSONArray child_json = (JSONArray) new_json.get("vacaciones");
//        String nam = (String) child_json.getJSONObject(0).get("inicio");
//        String nam1 = (String) child_json.getJSONObject(0).get("fin");
//        Integer nam2 = (Integer) child_json.getJSONObject(1).get("d");
//        List<String> newList = new ArrayList<>();
//        newList.add(nam);
//        newList.add(nam1);
//        newList.add(String.valueOf(nam2));
//        if (newList.size() == 1){
//        SharedPreferences preferences = getSharedPreferences(getString(R.string.name_preference_user_inf),MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("vac",nam);
//        editor.putString("inicio",nam1);
//        editor.putString("d",String.valueOf(nam2));
//        }
//
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
