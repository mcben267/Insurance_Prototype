package com.cliffdevops.alpha.bismartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.AgentViewAdapter;
import model.AgentItem;

public class SearchActivity extends AppCompatActivity implements AgentViewAdapter.OnLoanListener,
        AdapterView.OnItemSelectedListener {

    private SharedPreferences pref;
    private AgentViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<AgentItem> AgentList = new ArrayList<>();
    private Context context;
    TextView call;
    private Spinner spinner,zoneSpinner;
    private static final int REQUEST_CALL = 1;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        spinner = findViewById(R.id.spinnerLocation);
        zoneSpinner = findViewById(R.id.spinnerZone);
        recyclerView = findViewById(R.id.agentsList);
        call = findViewById(R.id.btnCall);
        final Button search = findViewById(R.id.btnSearch);

        showHeader();
        updateLocationSpinner();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent i = new Intent(SearchActivity.this, ResultsActivity.class);
                startActivity(i);*/
                intiRecycleView();
            }
        });
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

    public  void updateLocationSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String City = parent.getItemAtPosition(position).toString();
                updateZoneSpinner(City);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void intiRecycleView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AgentViewAdapter(this,AgentList, this);
        recyclerView.setAdapter(mAdapter);
        populateData();
    }

    private void populateData() {

        int[] insuranceIcon = {
                R.drawable.logo,
                R.drawable.ic_account,
        };

        /*uniqueID, name, agency, score, location, mobile, pin_location;*/

        AgentItem item = new AgentItem("1234567", "Alpha Maina", "Cliff and Associates", "4.5",
                "CBD GPO", "+254711545036",22 ,5.56, insuranceIcon[0]);
        AgentList.add(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(String position) {
       /* Intent i = new Intent(SearchActivity.this, ResultsActivity.class);
        startActivity(i);*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void showHeader(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    public void updateZoneSpinner(String city) {

        if(city.equals("Nairobi")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Nairobi, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zoneSpinner.setAdapter(adapter);
            zoneSpinner.setOnItemSelectedListener(this);
        }else if(city.equals("Mombasa")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Mombasa, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zoneSpinner.setAdapter(adapter);
            zoneSpinner.setOnItemSelectedListener(this);
        }
    }

}