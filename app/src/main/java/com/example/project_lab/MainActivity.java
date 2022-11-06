package com.example.project_lab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project_lab.adapter.BookAdapter;
import com.example.project_lab.model.Book;
import com.example.project_lab.database.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int userId = 0;
    private RecyclerView rvBooks;
    private ArrayList<Book> bookList;
    private BookAdapter bookAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        getSupportActionBar().setTitle(Html.fromHtml("<font color='#332F5E'>" + getString(R.string.app_name) + "</font>"));
        db = new DatabaseHelper(MainActivity.this);
        userId = getLoggedUserId();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rvBooks = findViewById(R.id.main_rv_books);
        bookList = new ArrayList<>();
        bookList = db.getBookData();
        bookAdapter = new BookAdapter(MainActivity.this, bookList);
        rvBooks.setAdapter(bookAdapter);
        rvBooks.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        bookAdapter.replaceData(bookList);
    }

    private int getLoggedUserId() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        userId = sp.getInt("userId", 0);
        return userId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP);
            }
        }
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