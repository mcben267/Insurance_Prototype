package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    private Button login;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar);
        final EditText email = findViewById(R.id.txtUserEmail);
        final EditText password = findViewById(R.id.txtUserPassword);
        login = findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    if (email.getText().toString().trim().matches(emailPattern)) {

                        String Email = email.getText().toString().trim();
                        String Password = password.getText().toString().trim();

                        progressBar.setVisibility(View.VISIBLE);
                        login.setVisibility(View.INVISIBLE);
                        new LoginUser().execute(Email, Password);

                    } else {
                       showToast( "Invalid email address");
                    }
                } else {
                    showToast("Please fill any empty fields...");
                }

            }
        });
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

    @SuppressLint("StaticFieldLeak")
    public class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];
            String url_user = "https://myloanapp.000webhostapp.com/bismartlogin.php";

            String finalURL = url_user +
                    "?&email=" + Email +
                    "&password=" + Password;

            Log.d("LoginActivity", "Params" + finalURL);

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(finalURL).build();
                Response response;

                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        Log.d("LoginActivity", "Response " + result);


                        if (result.equals("error")) {
                            showToast("Email or Password mismatched!");

                        } else {
                            JSONObject root = new JSONObject(result);
                            String UserID = root.getString("user_id");
                            String Name = root.getString("firstname");
                            String Surname = root.getString("lastname");
                            String UserEmail = root.getString("email");
                            String Mobile = root.getString("mobile");


                            pref.edit().putBoolean("logged", true).apply();
                            pref.edit().putString("name", Name).apply();
                            pref.edit().putString("userID", UserID).apply();
                            pref.edit().putString("surname", Surname).apply();
                            pref.edit().putString("email", UserEmail).apply();
                            pref.edit().putString("mobile", Mobile).apply();

                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
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