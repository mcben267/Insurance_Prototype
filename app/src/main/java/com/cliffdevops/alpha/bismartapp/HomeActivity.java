package com.cliffdevops.alpha.bismartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.InsuranceViewAdapter;
import model.InsuranceItem;

public class HomeActivity extends AppCompatActivity implements InsuranceViewAdapter.OnLoanListener {
    private SharedPreferences pref;
    private InsuranceViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<InsuranceItem> InsuranceList = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getSharedPreferences("login", MODE_PRIVATE);

        recyclerView = findViewById(R.id.insuranceList);
        TextView currentUser = findViewById(R.id.txtUsername);
        currentUser.setText("Welcome, " + pref.getString("name",""));

        showHeader();
        intiRecycleView();

    }

    public void showHeader(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
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

    private void intiRecycleView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new InsuranceViewAdapter(InsuranceList, this);
        recyclerView.setAdapter(mAdapter);
        populateData();
    }

    private void populateData() {
        InsuranceItem item;

        int[] insuranceIcon = {
                R.drawable.ic_motor,
                R.drawable.ic_phone,
                R.drawable.ic_health,
                R.drawable.ic_traveller,
                R.drawable.ic_home,
                R.drawable.ic_marine,
                R.drawable.ic_investment,
                R.drawable.ic_accident,
                R.drawable.ic_contractor
        };

         item = new InsuranceItem("Motor", insuranceIcon[0]);
        InsuranceList.add(item);
        item = new InsuranceItem("Phone/Tablet", insuranceIcon[1]);
        InsuranceList.add(item);
        item = new InsuranceItem("Health", insuranceIcon[2]);
        InsuranceList.add(item);
        item = new InsuranceItem("Travel", insuranceIcon[3]);
        InsuranceList.add(item);
        item = new InsuranceItem("Home", insuranceIcon[4]);
        InsuranceList.add(item);
        item = new InsuranceItem("Marine", insuranceIcon[5]);
        InsuranceList.add(item);
        item = new InsuranceItem("Investment", insuranceIcon[6]);
        InsuranceList.add(item);
        item = new InsuranceItem("Personal", insuranceIcon[7]);
        InsuranceList.add(item);
        item = new InsuranceItem("Contractors", insuranceIcon[8]);
        InsuranceList.add(item);

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onNoteClick(String position) {
        Intent i = new Intent(HomeActivity.this,SearchActivity.class);
        startActivity(i);
        finish();
    }
}