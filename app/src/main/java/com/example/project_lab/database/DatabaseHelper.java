package com.example.project_lab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_lab.model.Book;
import com.example.project_lab.model.Requests;
import com.example.project_lab.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String SQL_CREATE_USERS_TABLE
            = "CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, email VARCHAR(255), password VARCHAR(255), phone_number VARCHAR(50), date_of_birth DATE)";
    public static final String SQL_CREATE_REQUESTS_TABLE
            = "CREATE TABLE IF NOT EXISTS requests(id INTEGER PRIMARY KEY AUTOINCREMENT, book_id INTEGER, requester_id INTEGER, receiver_id INTEGER, latitude DECIMAL(10,5), longitude DECIMAL(10,5))";
    public static final String SQL_CREATE_BOOKS_TABLE
            = "CREATE TABLE IF NOT EXISTS books(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), author VARCHAR(255), cover VARCHAR(255), synopsis TEXT)";

    public static final String SQL_DROP_USERS_TABLE
            = "DROP TABLE IF EXISTS users";
    public static final String SQL_DROP_REQUESTS_TABLE
            = "DROP TABLE IF EXISTS requests";
    public static final String SQL_DROP_BOOKS_TABLE
            = "DROP TABLE IF EXISTS books";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Perpus Online", null, 4);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_REQUESTS_TABLE);
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        insertBooks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_USERS_TABLE);
        db.execSQL(SQL_DROP_REQUESTS_TABLE);
        db.execSQL(SQL_DROP_BOOKS_TABLE);
        onCreate(db);
    }

    public void registerNewUser(String email, String password, String phone_number, Date date_of_birth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("email", email);
        cv.put("password", password);
        cv.put("phone_number", phone_number);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String date = sdf.format(date_of_birth);
        cv.put("date_of_birth", date);

        long result = db.insert("users", null, cv);

        if(result>0) {
            Toast.makeText(context, "Successfully registered!", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<User> getAllUserData() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String email = cursor.getString(1);
            String password = cursor.getString(2);
            String phone_number = cursor.getString(3);
            Date date_of_birth = new Date(cursor.getLong(4));

            User user = new User(id, email, password, phone_number, date_of_birth);
            userList.add(user);
        }
        return userList;
    }

    public User getUserData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(id)});

        User user = null;
        while (cursor.moveToNext()) {
            int userId = cursor.getInt(0);
            String email = cursor.getString(1);
            String password = cursor.getString(2);
            String phone_number = cursor.getString(3);
            Date date_of_birth = new Date(cursor.getLong(4));

            user = new User(userId, email, password, phone_number, date_of_birth);
        }
        return user;
    }

    private void insertBooks(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        String URL = "https://isys6203-perpus-online.herokuapp.com/";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(URL,
                response -> {
                    for (int i=0; i<response.length(); i++) {
                        JSONObject book;
                            try {
                                book = response.getJSONObject(i);
                                cv.put("name", book.getString("name"));
                                cv.put("author", book.getString("author"));
                                cv.put("cover", book.getString("cover"));
                                cv.put("synopsis", book.getString("synopsis"));
                                db.insert("books", null, cv);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                    },
                        error -> { //Error
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        });
        queue.add(arrayRequest);
    }

    public ArrayList<Book> getBookData() {
        ArrayList<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String author = cursor.getString(2);
            String cover = cursor.getString(3);
            String synopsis = cursor.getString(4);

            Book book = new Book(id, name, author, cover, synopsis);
            bookList.add(book);
        }
        return bookList;
    }

    public void insertNewRequest(int book_id, int requester_id, int receiver_id, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("book_id", book_id);
        cv.put("requester_id", requester_id);
        cv.put("receiver_id", receiver_id);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);

        long result = db.insert("requests", null, cv);
        if (result>0) {
            Toast.makeText(context, "Successfully made a new request!", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Requests> getRequestsData() {
        ArrayList<Requests> requestList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM requests", null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int book_id = cursor.getInt(1);
            int requester_id = cursor.getInt(2);
            int receiver_id = cursor.getInt(3);
            double latitude = cursor.getDouble(4);
            double longitude = cursor.getDouble(5);

            Requests request = new Requests(id, book_id, requester_id, receiver_id, latitude, longitude);
            requestList.add(request);
        }
        return requestList;
    }

    public void updateRequestData(int id, int receiver_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("receiver_id", receiver_id);
        db.update("requests", cv, "id = ?", new String[]{String.valueOf(id)});
    }

}
