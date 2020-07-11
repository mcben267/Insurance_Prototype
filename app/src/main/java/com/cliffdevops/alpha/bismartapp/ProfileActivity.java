package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private EditText name, surname, mobile, email;
    private EditText oldPassword, newPassword, confirmPassword;
    private Button changePassword;
    private String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        ImageView back = findViewById(R.id.btnBack);
        name = findViewById(R.id.txtName);
        surname = findViewById(R.id.txtSurname);
        mobile = findViewById(R.id.txtMobile);
        email = findViewById(R.id.txtEmail);
        oldPassword = findViewById(R.id.txtOldPassword);
        newPassword = findViewById(R.id.txtNewPassword);
        confirmPassword = findViewById(R.id.txtConfirmNewPassword);
        changePassword = findViewById(R.id.btnChangePassword);

        name.setText(pref.getString("name", ""));
        surname.setText(pref.getString("surname", ""));
        mobile.setText(pref.getString("mobile", ""));
        email.setText(pref.getString("email", ""));

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePassword()){
                    showToast("TODO");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProfileActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean validatePassword() {
        String inputA = newPassword.getEditableText().toString().trim();
        String inputB = confirmPassword.getEditableText().toString().trim();
        String inputC = oldPassword.getEditableText().toString().trim();
        String passwordPattern = "^" +
                "(?=.*[A-Za-z])" +  //any letter
                "(?=.*[0-9])" +     //at least 1 digit
                "(?=\\S+$)" +       //no whitespace
                ".{6,}" +            //at least 6 characters
                "$";

        if (inputA.isEmpty() && inputB.isEmpty() && inputC.isEmpty()) {
            oldPassword.setError("Field cannot be empty");
            newPassword.setError("Field cannot be empty");
            confirmPassword.setError("Field cannot be empty");
            return false;
        }

        if (!inputA.equals(inputB)) {
            newPassword.setError("password do not match");
            confirmPassword.setError("password do not match");
            return false;
        }



        if (inputA.equals(inputB)) {
            if (inputA.matches(passwordPattern) && inputB.matches(passwordPattern)) {
                Password = newPassword.getText().toString().trim();
                newPassword.setError(null);
                confirmPassword.setError(null);
                return true;
            } else {
                String error_msg = "Password requirements: letter, digit, at least 6 characters";
                newPassword.setError(error_msg);
                confirmPassword.setError(error_msg);
                return false;
            }
        } else {
            return false;
        }
    }
}