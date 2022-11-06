package com.example.project_lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_lab.database.DatabaseHelper;
import com.example.project_lab.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvRegister;
    Button btnLogin;
    String email, password;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initWidgets();
        db = new DatabaseHelper(LoginActivity.this);

        tvRegister.setOnClickListener(view -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(view -> {

            email = etEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            if(db.getAllUserData().isEmpty()) {
                Toast.makeText(LoginActivity.this, "No user data in record. Please register first.", Toast.LENGTH_SHORT)
                        .show();
            } else if(email.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
            } else {
                int i = 0;
                User user = null;
                for(User tempUser: db.getAllUserData()) {
                    user = tempUser;
                    if (email.equals(tempUser.getEmail()) && password.equals(tempUser.getPassword())){
                        i++;
                        Intent in = new Intent(this, MainActivity.class);
                        setLoggedUserId(user);
                        startActivity(in);
                        Toast.makeText(LoginActivity.this, "Sucessfully logged in!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (i==0) {
                    Toast.makeText(LoginActivity.this, "Wrong Email/Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initWidgets() {
        etEmail = findViewById(R.id.login_et_email);
        etPassword = findViewById(R.id.login_et_password);
        btnLogin = findViewById(R.id.main_btn_login);
        tvRegister = findViewById(R.id.login_tv_register);
    }

    private void setLoggedUserId(User user) {
        SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        int userId = user.getId();

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("userId", userId);
        editor.commit();
    }

}