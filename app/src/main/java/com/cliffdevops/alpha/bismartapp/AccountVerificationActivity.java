package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountVerificationActivity extends AppCompatActivity {

    private String email,pin,currentDate;
    private SharedPreferences pref;
    private TextView user_email;
    private Button verify;
    private ProgressBar progressBar;
    private EditText input1, input2, input3, input4, input5, input6;
    private String digit1,digit2,digit3,digit4,digit5,digit6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        user_email = findViewById(R.id.txtEmail_prefix);
        progressBar = findViewById(R.id.progressBar);
        verify = findViewById(R.id.btnVerify);

        input1 = findViewById(R.id.otp_edit_text1);
        input2 = findViewById(R.id.otp_edit_text2);
        input3 = findViewById(R.id.otp_edit_text3);
        input4 = findViewById(R.id.otp_edit_text4);
        input5 = findViewById(R.id.otp_edit_text5);
        input6 = findViewById(R.id.otp_edit_text6);

        email = pref.getString("email", "");
        user_email.setText("sent to: "+email);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if(validateDigits()) {
                    new AccountVerification().execute(randomCode(),email,pin,currentDate);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountVerificationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean validateDigits(){

        validateDigit1();
        validateDigit2();
        validateDigit3();
        validateDigit4();
        validateDigit5();
        validateDigit6();

        if(validateDigit1() && validateDigit2() && validateDigit3()
                && validateDigit4() && validateDigit5() && validateDigit6()){

            digit1 = input1.getText().toString().trim();
            digit2 = input2.getText().toString().trim();
            digit3 = input3.getText().toString().trim();
            digit4 = input4.getText().toString().trim();
            digit5 = input5.getText().toString().trim();
            digit6 = input6.getText().toString().trim();

            pin = digit1+""+digit2+""+digit3+""+digit4+""+digit5+""+digit6;

            return true;
        }else{
            return false;
        }
    }

    public boolean validateDigit1() {
        String val = input1.getEditableText().toString().trim();

        if (val.isEmpty()) {
            input1.setError("Field cannot be empty");
            return false;
        } else {
            input1.setError(null);
            return true;
        }
    }

    public boolean validateDigit2() {
        String value = input2.getEditableText().toString().trim();

        if (value.isEmpty()) {
            input2.setError("Field cannot be empty");
            return false;
        } else {
            input2.setError(null);
            return true;
        }
    }

    public boolean validateDigit3() {
        String value = input3.getEditableText().toString().trim();

        if (value.isEmpty()) {
            input3.setError("Field cannot be empty");
            return false;
        } else {
            input3.setError(null);
            return true;
        }
    }

    public boolean validateDigit4() {
        String value = input4.getEditableText().toString().trim();

        if (value.isEmpty()) {
            input4.setError("Field cannot be empty");
            return false;
        } else {
            input4.setError(null);
            return true;
        }
    }

    public boolean validateDigit5() {
        String value = input5.getEditableText().toString().trim();

        if (value.isEmpty()) {
            input5.setError("Field cannot be empty");
            return false;
        } else {
            input5.setError(null);
            return true;
        }
    }

    public boolean validateDigit6() {
        String value = input5.getEditableText().toString().trim();

        if (value.isEmpty()) {
            input6.setError("Field cannot be empty");
            return false;
        } else {
            input6.setError(null);
            return true;
        }
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AccountVerificationActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected String randomCode() {
        int length = 10;
        return RandomStringUtils.random(length, false, true);
    }

    public class AccountVerification extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            verify.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            verify.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String url_Register = "https://myloanapp.000webhostapp.com/bismartverify.php";
            String Id = strings[0];
            String Email = strings[1];
            String OTP = strings[2];
            String date = strings[3];

            String finalURL = url_Register +
                    "?&id=" + Id + "&email=" + Email + "&otp=" + OTP + "&date=" + date;

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(finalURL)
                        .build();
                Response response = null;

                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        if (result.equalsIgnoreCase("Verified")) {

                            showToast("Account Verified");
                            pref.edit().putBoolean("logged", true).apply();
                            Intent i = new Intent(AccountVerificationActivity.this,
                                    HomeActivity.class);
                            startActivity(i);
                            finish();

                        } else if (result.equalsIgnoreCase("Invalid")) {
                            showToast("OTP code already used");
                        } else if (result.equalsIgnoreCase("Failed")) {
                        showToast("Error: Request failed");
                        }else {
                            showToast("Fatal Error");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Error: Check Internet");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Fatal Error !");
            }
            return null;
        }

    }

}