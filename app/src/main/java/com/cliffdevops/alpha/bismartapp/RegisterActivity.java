package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name,surname,email,mobile,password,confirmPassword;
    private Button register;
    private String Name,Surname,Mobile,Email,Password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.progressBar);

        name = findViewById(R.id.txtName);
        surname = findViewById(R.id.txtSurname);
        email = findViewById(R.id.txtEmail);
        mobile = findViewById(R.id.txtMobile);
        password = findViewById(R.id.txtPassword);
        confirmPassword = findViewById(R.id.txtConfimPassword);
        register = findViewById(R.id.btnSignup);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString().trim();
                Surname = surname.getText().toString().trim();
                Mobile = mobile.getText().toString().trim();
                Email = email.getText().toString().trim();

                if(validateForm().equals(true)) {
                    register.setVisibility(View.INVISIBLE);
                    new RegisterUser().execute(shortUUID(),Name,Surname,Mobile,Email,Password);
                }
            }
        });

    }

    private Boolean validateForm() {
        validateName();
        validateSurname();
        validateEmail();
        validateMobile();
        validatePassword();

        if (validateName().equals(true)
                && validateSurname().equals(true)
                && validateEmail().equals(true)
                && validateMobile().equals(true)
                && validatePassword().equals(true)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean validateName() {
        String val = name.getEditableText().toString().trim();
        if (val.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            // fullName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateSurname() {
        String val = surname.getEditableText().toString().trim();
        if (val.isEmpty()) {
            surname.setError("Field cannot be empty");
            return false;
        } else {
            surname.setError(null);
            return true;
        }
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

    private Boolean validateMobile() {
        String val = mobile.getEditableText().toString().trim();
        if (val.isEmpty()) {
            mobile.setError("Field cannot be empty");
            return false;
        } else {
            mobile.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditableText().toString().trim();
        String val1 = confirmPassword.getEditableText().toString().trim();
        String passwordPattern = "^" +
                "(?=.*[A-Za-z])" +  //any letter
                "(?=.*[0-9])" +     //at least 1 digit
                "(?=\\S+$)" +       //no whitespace
                ".{6,}" +            //at least 6 characters
                "$";

        if (val.isEmpty() && val1.isEmpty()) {
            password.setError("Field cannot be empty");
            confirmPassword.setError("Field cannot be empty");
            return false;
        }

        if (!val.equals(val1)) {
            password.setError("password do not match");
            confirmPassword.setError("password do not match");
            return false;
        }
        if (val.equals(val1)) {
            if (val.matches(passwordPattern) && val1.matches(passwordPattern)) {
                Password = password.getText().toString().trim();
                password.setError(null);
                confirmPassword.setError(null);
                return true;
            } else {
                String error_msg= "Password requirements: letter, digit, at least 6 characters" ;
                password.setError(error_msg);
                confirmPassword.setError(error_msg);
                return false;
            }
        } else {
            return false;
        }
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String url_Register="https://myloanapp.000webhostapp.com/bismartregister.php";
            String Client_ID = strings[0];
            String FirstName = strings[1];
            String LastName = strings[2];
            String Mobile = strings[3];
            String Email = strings[4];
            String Password = strings[5];

            String finalURL = url_Register +
                    "?user_id=" + Client_ID +
                    "&firstname=" + FirstName +
                    "&lastname=" + LastName +
                    "&mobile=" + Mobile +
                    "&email=" + Email +
                    "&password=" + Password;

            Log.d("RegisterActivity", "Params " + finalURL);

            try {
                OkHttpClient okHttpClient= new OkHttpClient();
                Request request = new Request.Builder()
                        .url(finalURL)
                        .build();
                Response response = null;

                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        if (result.equalsIgnoreCase("User registered successfully")) {
                            showToast("Register successful");
                            Intent i = new Intent(RegisterActivity.this,
                                    LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else if (result.equalsIgnoreCase("User already exists")) {
                            showToast("User already exists");
                        } else {
                            showToast("Error: Registration failed");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Error: Check Internet");
                }
            }catch (Exception e){
                e.printStackTrace();
                showToast("Fatal Error !");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            register.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}