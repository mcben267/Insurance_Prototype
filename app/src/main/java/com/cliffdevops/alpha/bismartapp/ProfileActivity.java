package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private EditText name,surname,mobile,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        ImageView back = findViewById(R.id.btnBack);
        name =findViewById(R.id.txtName);
        surname =findViewById(R.id.txtSurname);
        mobile =findViewById(R.id.txtMobile);
        email =findViewById(R.id.txtEmail);

        name.setText(pref.getString("name",""));
        surname.setText(pref.getString("surname",""));
        mobile.setText(pref.getString("mobile",""));
        email.setText(pref.getString("email",""));


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
}