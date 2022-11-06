package com.example.project_lab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_lab.adapter.RequestAdapter;
import com.example.project_lab.model.Book;
import com.example.project_lab.database.DatabaseHelper;
import com.google.android.gms.maps.model.LatLng;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTitle, tvAuthor, tvSynopsis, tvLatitude, tvLongitude, tvDialogErrors;
    private ImageView imgCover;
    private Button btnRequest, btnDialogConfirm;
    private Dialog dialog;

    private Book book;
    private DatabaseHelper db;

    private int userId = 0;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        db = new DatabaseHelper(BookDetailActivity.this);
        initWidgets();

        book = getIntent().getParcelableExtra("Book");
        tvTitle.setText(book.getName());
        tvAuthor.setText(book.getAuthor());
        tvSynopsis.setText(book.getSynopsis());
        String imageURL = getIntent().getStringExtra("Book Cover");
        Glide.with(BookDetailActivity.this).load(imageURL).into(imgCover);

        IntentFilter filter = new IntentFilter("Location Data");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LatLng latLng = intent.getExtras().getParcelable("Latlng");
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                tvLatitude.setText(String.valueOf(latitude));
                tvLongitude.setText(String.valueOf(longitude));
            }
        };
        registerReceiver(receiver, filter);

    }

    private void initWidgets() {
        tvTitle = findViewById(R.id.bookDetail_tv_bookTitle);
        tvAuthor = findViewById(R.id.bookDetail_tv_author);
        tvSynopsis = findViewById(R.id.bookDetail_tv_synopsis);
        tvLatitude = findViewById(R.id.bookDetail_tv_latitude);
        tvLongitude = findViewById(R.id.bookDetail_tv_longitude);
        imgCover = findViewById(R.id.bookDetail_bookCover);
        btnRequest = findViewById(R.id.bookDetail_btn_request);
        btnRequest.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.bookDetail_container, new MapsFragment()).commit();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvDialogErrors = dialog.findViewById(R.id.dialog_tv_errors);
        btnDialogConfirm = dialog.findViewById(R.id.dialog_button_confirm);
        btnDialogConfirm.setOnClickListener(this);

    }

    private int getLoggedUserId() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        userId = sp.getInt("userId", 0);
        return userId;
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
        if (v==btnDialogConfirm) {
            dialog.dismiss();
        }

        else if(v==btnRequest) {
            if(tvLongitude.getText().equals("-") || tvLatitude.getText().equals("-")) {
                dialog.show();
                tvDialogErrors.setText("Please choose a pickup location first!");
                return;
            }
            int book_id = book.getId();
            int requester_id = getLoggedUserId();
            int receiver_id = 0;

            db.insertNewRequest(book_id, requester_id, receiver_id, latitude, longitude);

            Intent i = new Intent(this, RequestAdapter.class);
            i.putExtra("Book ID", book_id);
        }
    }
}