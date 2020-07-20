package com.cliffdevops.alpha.bismartapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SharedPreferences pref;
    private String number, place;
    private MarkerOptions markerOptions;
    private CircleOptions circleOptions;
    private double latitude, longitude;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        TextView name = findViewById(R.id.txtname_result);
        TextView agency = findViewById(R.id.txtagency_result);
        TextView location = findViewById(R.id.txtlocation_result);
        TextView policies = findViewById(R.id.txtPolicies_result);
        TextView meet = findViewById(R.id.btnMeet);
        TextView call = findViewById(R.id.btnCall);
        final ConstraintLayout mapView = findViewById(R.id.mapView);

        //Display toolbar
        showHeader();

        //Extracting Data from previous activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        name.setText(extras.getString("name"));
        agency.setText(extras.getString("agency"));
        location.setText(extras.getString("location"));
        policies.setText(extras.getString("policies"));
        number = extras.getString("number");
        latitude = extras.getDouble("latitude", 0.0);
        longitude = extras.getDouble("longitude", 0.0);
        place = extras.getString("location");

        //google map activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setVisibility(View.VISIBLE);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //coordinates
        double Lat_coordinate = latitude;
        double Lng_coordinate = longitude;

        //zoom location
        LatLng coordinates = new LatLng(Lat_coordinate, Lng_coordinate);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinates, 16);
        googleMap.animateCamera(location);

        //add location circle
        /*markerOptions = new MarkerOptions().position(coordinates).title(place);
        CircleOptions circleOptions = new CircleOptions()
                .center(markerOptions.getPosition())
                .radius(150)
                .fillColor(0x220000DD);
        googleMap.addCircle(circleOptions);*/

        //view map
        LatLng sydney = new LatLng(Lat_coordinate, Lng_coordinate);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(place));
        googleMap.moveCamera(location);

    }

    public void showHeader() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
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

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
        finish();
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ResultsActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    private void makePhoneCall() {

        if (ContextCompat.checkSelfPermission(ResultsActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ResultsActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                showToast("Permission DENIED");
            }
        }
    }

}
