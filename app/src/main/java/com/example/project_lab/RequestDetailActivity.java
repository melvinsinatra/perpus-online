package com.example.project_lab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_lab.model.Book;
import com.example.project_lab.database.DatabaseHelper;
import com.example.project_lab.model.Requests;
import com.example.project_lab.model.User;

import java.util.ArrayList;
import java.util.Map;

public class RequestDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private MapsFragment mapsFragment;
    private FragmentManager fm;
    private FrameLayout container;

    private LinearLayout llSendSMS;
    private TextView tvRequesterEmail, tvReceiverEmail, tvBookTitle, tvBookAuthor, tvBookSynopsis, tvLatitude, tvLongitude, tvSendSMS;
    private ImageView imgCover;
    private Button btnAcceptRequest;

    private DatabaseHelper db;
    private Requests requests;
    private Book book;

    private int loggedUserId = 0;
    private ArrayList<Requests> requestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        initWidgets();

        db = new DatabaseHelper(RequestDetailActivity.this);
        loggedUserId = getLoggedUserId();
        requestsList = db.getRequestsData();

        requests = getIntent().getParcelableExtra("Chosen Requests");
        initMap();
        book = getIntent().getParcelableExtra("Chosen Book");

        int requesterId = requests.getRequester_id();
        User requester = db.getUserData(requesterId);
        tvRequesterEmail.setText(requester.getEmail());

        if(requests.getReceiver_id()==0) {
            tvReceiverEmail.setText("-");
        } else {
            int receiverId = requests.getReceiver_id();
            User receiver = db.getUserData(receiverId);
            tvReceiverEmail.setText(receiver.getEmail());
        }

        tvBookTitle.setText(book.getName());
        tvBookAuthor.setText(book.getAuthor());
        tvBookSynopsis.setText(book.getSynopsis());
        tvLatitude.setText(String.valueOf(requests.getLatitude()));
        tvLongitude.setText(String.valueOf(requests.getLongitude()));


        String imageURL = getIntent().getStringExtra("Chosen Book Cover");
        Glide.with(RequestDetailActivity.this).load(imageURL).into(imgCover);

        if (requests.getReceiver_id() != 0) {
            btnAcceptRequest.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
        }

        if(requests.getRequester_id() == loggedUserId) {
            llSendSMS.setVisibility(View.GONE);
        }

        if(loggedUserId != requests.getRequester_id()) {
            btnAcceptRequest.setOnClickListener(this);
        } else {
            btnAcceptRequest.setVisibility(View.GONE);
        }

    }

    private void initMap() {
        mapsFragment = new MapsFragment();

        Bundle args = new Bundle();
        args.putDouble("Latitude", requests.getLatitude());
        args.putDouble("Longitude", requests.getLongitude());
        mapsFragment.setArguments(args);

        container = findViewById(R.id.requestDetail_container);
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.requestDetail_container, mapsFragment).commit();
    }

    private void initWidgets() {
        tvRequesterEmail = findViewById(R.id.requestDetail_tv_requesterEmail);
        tvReceiverEmail = findViewById(R.id.requestDetail_tv_receiverEmail);
        tvBookTitle = findViewById(R.id.requestDetail_tv_bookTitle);
        tvBookAuthor = findViewById(R.id.requestDetail_tv_bookAuthor);
        tvBookSynopsis = findViewById(R.id.requestDetail_tv_bookSynopsis);
        imgCover = findViewById(R.id.requestDetail_img_bookCover);
        tvLatitude = findViewById(R.id.requestDetail_tv_latitude);
        tvLongitude = findViewById(R.id.requestDetail_tv_longitude);
        btnAcceptRequest = findViewById(R.id.requestDetail_btn_acceptRequest);
        llSendSMS = findViewById(R.id.requestDetail_ll_sendSMS);
        tvSendSMS = findViewById(R.id.requestDetail_tv_sendSMS);
        tvSendSMS.setOnClickListener(this);
    }

    private int getLoggedUserId() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        loggedUserId = sp.getInt("userId", 0);
        return loggedUserId;
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

    @Override
    public void onClick(View v) {

        if (v==btnAcceptRequest) {
            requests.setReceiver_id(loggedUserId);
            Toast.makeText(this, "Requests Accepted!", Toast.LENGTH_SHORT).show();
            db.updateRequestData(requests.getId(), loggedUserId);
            requestsList = db.getRequestsData();

            Intent i = new Intent(this, ViewAllRequestActivity.class);
            startActivity(i);
        }

        else if (v==tvSendSMS) {
            Intent i = new Intent(this, MessageActivity.class);
            i.putExtra("Requester Id", requests.getRequester_id());
            startActivity(i);
        }

    }
}