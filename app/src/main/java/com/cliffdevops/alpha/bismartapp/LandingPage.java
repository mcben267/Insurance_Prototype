package com.cliffdevops.alpha.bismartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        Button signIn = findViewById(R.id.signIN);
        Button signUp = findViewById(R.id.signUP);

        autoLogin();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPage.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPage.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    public void autoLogin(){
        if (pref.getBoolean("logged", false)) {
            Intent i = new Intent(LandingPage.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
}