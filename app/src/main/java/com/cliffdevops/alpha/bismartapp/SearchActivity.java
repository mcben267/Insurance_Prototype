package com.cliffdevops.alpha.bismartapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.AgentViewAdapter;
import model.AgentItem;

public class SearchActivity extends AppCompatActivity implements AgentViewAdapter.OnAgentListener,
        AdapterView.OnItemSelectedListener {

    private SharedPreferences pref;
    private AgentViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<AgentItem> AgentList = new ArrayList<>();
    private Context context;
    private Button search;
    private LinearLayoutManager layoutManager;
    private Spinner locationSpinner, zoneSpinner;
    private String UserID, City, Zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        TextView policy = findViewById(R.id.txtPolicy);
        locationSpinner = findViewById(R.id.spinnerLocation);
        zoneSpinner = findViewById(R.id.spinnerZone);
        recyclerView = findViewById(R.id.agentsList);
        progressBar = findViewById(R.id.progressBar);
        search = findViewById(R.id.btnSearch);

        UserID = pref.getString("user_id", "");
        policy.setText(pref.getString("policy", ""));

        showHeader();
        updateLocationSpinner();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.btnHelp:
                i = new Intent(this, HelpActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.btnLogout:
                pref.edit().putBoolean("logged", false).apply();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateLocationSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
                City = city;
                updateZoneSpinner(city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateZoneSpinner(String city) {

        if (city.equals("Nairobi")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Nairobi, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zoneSpinner.setAdapter(adapter);
            zoneSpinner.setOnItemSelectedListener(this);

        } else if (city.equals("Mombasa")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Mombasa, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zoneSpinner.setAdapter(adapter);
            zoneSpinner.setOnItemSelectedListener(this);
        }

        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String zone = parent.getItemAtPosition(position).toString();
                if (zone.equals("Select")) {

                } else {
                    Zone = zone;
                    search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("NewApi")
    private void intiRecycleView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AgentViewAdapter(AgentList, this);
        recyclerView.setAdapter(mAdapter);
        //populateData();
        //new ViewAgents().execute(UserID,City,Zone);

        ViewAgents();

    }

    /*private void populateData() {

        int[] insuranceIcon = {
                R.drawable.logo,
                R.drawable.ic_account,
        };

        *//* params: uniqueID, name, agency, score, location, mobile, latitude, longitude *//*

        AgentItem item = new AgentItem("1234567", "Alpha Maina", "Cliff and Associates", "4.5",
                "CBD GPO", "+254711545036", 22, "5.56", "Education");
        AgentList.add(item);
        mAdapter.notifyDataSetChanged();

    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void showHeader() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SearchActivity.this,
                        Text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(String Name, String Agency, String Location, String Mobile, double Latitude,
                              double Longitude, String Policies) {

        Bundle extras = new Bundle();
        extras.putString("name", Name);
        extras.putString("agency", Agency);
        extras.putString("number", Mobile);
        extras.putString("location", Location);
        extras.putString("policies", Policies);
        extras.putDouble("latitude", Latitude);
        extras.putDouble("longitude", Longitude);

        showToast(Name);

        Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
        intent.putExtras(extras);
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void ViewAgents() {
        String URL = "https://myloanapp.000webhostapp.com/bismart_agentsinfo.php";
        StringRequest stringRequest;

        progressBar.setVisibility(View.VISIBLE);

        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int cartSize = response.length();

                try {
                    if (cartSize > 2) {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            AgentItem item = new AgentItem(
                                    object.getString("uniqueID"),
                                    object.getString("name"),
                                    object.getString("agency"),
                                    object.getString("score") + " / 5",
                                    object.getString("location"),
                                    object.getString("mobile"),
                                    object.getString("policies"),
                                    object.getDouble("Lat_coordinate"),
                                    object.getDouble("Lng_coordinate")
                            );

                            progressBar.setVisibility(View.GONE);
                            AgentList.add(item);
                            mAdapter.notifyDataSetChanged();

                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

                } catch (JSONException ex) {
                    progressBar.setVisibility(View.GONE);
                    ex.printStackTrace();
                    showToast("No records found");
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        showToast("Request failed try again");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", UserID);
                params.put("city", City);
                params.put("zone", Zone);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}