package com.example.project_lab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_lab.database.DatabaseHelper;
import com.example.project_lab.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvPhone, tvDialog;
    private EditText etMessage;
    private Button btnSend, btnDialog;
    private Dialog dialog;

    private String phone;
    private DatabaseHelper db;
    private SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidgets();
        db = new DatabaseHelper(this);
        smsManager = smsManager.getDefault();

        int requesterId = getIntent().getExtras().getInt("Requester Id");
        User requester = db.getUserData(requesterId);
        phone = String.valueOf(requester.getPhoneNumber());
        tvPhone.setText(phone);

        checkPermissions();
    }

    private void checkPermissions() {

        int sendPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(sendPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        int receivePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(receivePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }

        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if(readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
        }

    }

    private void initWidgets() {
        tvPhone = findViewById(R.id.message_tv_phone);
        etMessage = findViewById(R.id.message_et_message);
        btnSend = findViewById(R.id.message_button_send);
        btnSend.setOnClickListener(this);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvDialog = dialog.findViewById(R.id.dialog_tv_errors);
        btnDialog = dialog.findViewById(R.id.dialog_button_confirm);
        btnDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v==btnSend) {
            String message = etMessage.getText().toString();
            if(message.isEmpty()) {
                dialog.show();
                tvDialog.setText("Message can't be empty!");
                return;
            }
            smsManager.sendTextMessage(phone, null, message, null, null);
            etMessage.setText("");
            Toast.makeText(this, "Message sent to " + phone, Toast.LENGTH_SHORT).show();
        }

        else if (v==btnDialog) {
            dialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int choice = item.getItemId();

        switch (choice) {
            case android.R.id.home: finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}