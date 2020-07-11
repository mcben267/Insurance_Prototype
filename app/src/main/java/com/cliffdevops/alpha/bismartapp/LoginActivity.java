package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private ProgressBar progressBar;
    private EditText email, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.txtUserEmail);
        password = findViewById(R.id.txtUserPassword);
        login = findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (validateForm().equals(true)) {
                    if(isOnline()){
                        new LoginUser().execute(Email, Password);
                    }else{
                        showToast("Error:\tCheck Internet Connection");
                    }
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, LandingPage.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    private Boolean validateForm() {

        validateEmail();
        validatePassword();

        return validateEmail().equals(true) && validatePassword().equals(true);
    }

    private Boolean validateEmail() {
        String val = email.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            //emailId.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditableText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];
            String url_user = "https://myloanapp.000webhostapp.com/bismartlogin.php";

            String finalURL = url_user +
                    "?&email=" + Email + "&password=" + Password;

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(finalURL).build();
                Response response;

                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        if (result.equals("error")) {
                            showToast("Email or Password mismatched!");
                        } else {

                            JSONObject root = new JSONObject(result);
                            String UserID = root.getString("user_id");
                            String Name = root.getString("firstname");
                            String Surname = root.getString("lastname");
                            String UserEmail = root.getString("email");
                            String Mobile = root.getString("mobile");
                            String Status = root.getString("status");

                            pref.edit().putString("name", Name).apply();
                            pref.edit().putString("userID", UserID).apply();
                            pref.edit().putString("surname", Surname).apply();
                            pref.edit().putString("email", UserEmail).apply();
                            pref.edit().putString("mobile", Mobile).apply();
                            pref.edit().putString("status", Status).apply();

                            if(Status.equals("incomplete")) {
                                Intent i = new Intent(LoginActivity.this,AccountVerificationActivity.class);
                                startActivity(i);
                                finish();
                            }else if(Status.equals("verified")){
                                pref.edit().putBoolean("logged", true).apply();
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Fatal Error: Check Internet");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showToast("Fatal Error !");
            }
            return null;
        }
    }

}