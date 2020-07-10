package com.cliffdevops.alpha.bismartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SharedPreferences pref;
    private String place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        TextView name = findViewById(R.id.txtname_result);
        TextView agency = findViewById(R.id.txtagency_result);
        TextView location = findViewById(R.id.txtlocation_result);

        showHeader();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        name.setText(extras.getString("name"));
        agency.setText(extras.getString("agency"));
        location.setText(extras.getString("location"));
        place =extras.getString("location");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        double Lat_coordinate =-1.285988;
        double Lng_coordinate =36.818495;

        LatLng sydney = new LatLng(Lat_coordinate,Lng_coordinate);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(place));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void showHeader(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.btnProfile:
                i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btnHelp:
                i = new Intent(this, HelpActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btnLogout:
                pref.edit().putBoolean("logged", false).apply();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
                break;

            default:  break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
        finish();
    }
}