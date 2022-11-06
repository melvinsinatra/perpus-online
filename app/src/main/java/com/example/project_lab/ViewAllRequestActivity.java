package com.example.project_lab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project_lab.adapter.RequestAdapter;
import com.example.project_lab.database.DatabaseHelper;
import com.example.project_lab.model.Requests;

import java.util.ArrayList;

public class ViewAllRequestActivity extends AppCompatActivity {

    private TextView tvEmpty;
    private RecyclerView rvRequests;

    private DatabaseHelper db;
    private RequestAdapter requestAdapter;
    private ArrayList<Requests> requestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_request);
        db = new DatabaseHelper(ViewAllRequestActivity.this);
        initWidgets();

        if(!requestsList.isEmpty()) {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void initWidgets() {
        rvRequests = findViewById(R.id.viewAllRequest_rv_requests);

        requestsList = new ArrayList<>();
        requestsList = db.getRequestsData();
        requestAdapter = new RequestAdapter(ViewAllRequestActivity.this, requestsList);
        rvRequests.setAdapter(requestAdapter);
        rvRequests.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tvEmpty = findViewById(R.id.viewAllRequest_tv_empty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int chosenItem = item.getItemId();

        switch(chosenItem) {
            case R.id.menuMain_home:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.menuMain_viewAllRequest:
                Intent intent2 = new Intent(this, ViewAllRequestActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menuMain_logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                startActivity(intent3);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}