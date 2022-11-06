package com.example.project_lab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project_lab.database.DatabaseHelper;
import com.example.project_lab.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout registerActivity;
    EditText etEmail, etPassword, etPhoneNumber;
    TextView tvdoB, tvDialogErrors, tvLogin;
    CheckBox cbAgreement;
    Button btnRegister, btnDialogConfirm;
    DatePickerDialog datePickerDialog;
    String date, doB, errors;
    int yearOfBirth, currYear;
    DatabaseHelper db;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        initWidgets();
        initDatePicker();
        db = new DatabaseHelper(RegisterActivity.this);
    }

    private void initWidgets() {
        registerActivity = findViewById(R.id.registerLayout);
        etEmail = findViewById(R.id.register_et_email);
        etPassword = findViewById(R.id.register_et_password);
        etPhoneNumber = findViewById(R.id.register_et_phoneNumber);
        tvdoB = findViewById(R.id.register_tv_dateOfBirth);
        tvdoB.setOnClickListener(this);
        cbAgreement = findViewById(R.id.register_checkBox_agreement);
        btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(this);
        tvLogin = findViewById(R.id.register_tv_login);
        tvLogin.setOnClickListener(this);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvDialogErrors = dialog.findViewById(R.id.dialog_tv_errors);
        btnDialogConfirm = dialog.findViewById(R.id.dialog_button_confirm);
        btnDialogConfirm.setOnClickListener(this);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = day + "-" + month + "-" + year;
                tvdoB.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 1, 0, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, day, month, year);

    }

    private boolean noEmailDuplicates() {
        String email = etEmail.getText().toString().trim();

        for(User u: db.getAllUserData()) {
            if (email.equals(u.getEmail())) {
                return false;
            }
        }
        return true;
    }

    private boolean noPhoneDuplicates() {
        String phoneNum = etPhoneNumber.getText().toString().trim();

        for (User u: db.getAllUserData()) {
            if(phoneNum.equals(u.getPhoneNumber())) {
                return false;
            }
        }
        return true;
    }

    private boolean emailValid() {
        // [email]@[provider].[domain]
        int count1 = 0; // Count '@'
        int count2 = 0; // Count '.'
        boolean a = false; // Validation for Email must contain exactly one '@'
        boolean b = false; // Validation for Email must contain exactly one '.'
        boolean c = false; // Validation for Character '@' must not be next to '.'
        String email = etEmail.getText().toString().trim();

        // Character '@' must not be next to '.'
        try {
            if (email.charAt(email.indexOf('.') - 1) != '@' && email.charAt(email.indexOf('.') + 1) != '@') {
                c = true;
            }
        } catch (Exception e) {
            return false;
        }

        // Must contain exactly one ‘@’.
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@')
                count1++;
        }

        if (count1 == 1) {
            a = true;
        }

        // Must contain exactly one '.' after '@'
        for (int i = email.indexOf("@") + 2; i < email.length(); i++) {
            if (email.charAt(i) == '.')
                count2++;
        }

        if (count2 == 1) {
            b = true;
        }

        try {
            if (!email.startsWith("@") && !email.startsWith(".") && !email.endsWith("@") && !email.endsWith(".")
                    && a == true && b == true && c == true) {
                return true;
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean passwordValid() {
        String password = etPassword.getText().toString();
        if (password.length()>7 && isAlphaNumeric(password)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean ageValid() {
        doB = tvdoB.getText().toString();
        try {
            yearOfBirth = Integer.parseInt(doB.substring(doB.length() - 4));
        } catch (NumberFormatException e) {
            return false;
        }

        currYear = Calendar.getInstance().get(Calendar.YEAR);

        int age = currYear - yearOfBirth;

        if(age>=13) {
            return true;
        } else if (doB.equals("Click here to set your Date of Birth")) {
            return false;
        } else {
            return false;
        }
    }

    private boolean phoneNumberValid() {

        String phoneNumber = etPhoneNumber.getText().toString();

        if(phoneNumber.startsWith("+62") && phoneNumber.length()>11 && phoneNumber.length()<15) {
            return true;
        } else {
            return false;
        }
    }

    private boolean agreementValid() {

        if(cbAgreement.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAlphaNumeric(String str) {
        int alpha = 0;
        int num = 0;

        for (int z = 0; z < str.length(); z++) {
            if (Character.isLetter(str.charAt(z))) {
                alpha++;
            } else if (Character.isDigit(str.charAt(z))) {
                num++;
            }

            if (alpha > 0 && num > 0) {
                return true;
            }
        }
        return false;
    }

    private String initErrors() {
        String error1 = "";
        String error2 = "";
        String error3 = "";
        String error4 = "";
        String error5 = "";
        String error6 = "";
        String error7 = "";
        String error8 = "";

        if (!emailValid()) {
            error1 = "- Invalid Email address (Use format: example@mail.com),\n\n";
        } if (!passwordValid()) {
            error2 = "- Password must have a min. of 8 chars and must be alphanumeric,\n\n";
        } if (!phoneNumberValid()) {
            error3 = "- Phone Number must start with '+62', min. 10 digit, max 12 digit ('+62' is 1 digit),\n\n";
        } if (!ageValid()) {
            error5 = "- Age must be at least 13 years,\n\n";
        } if (!agreementValid()) {
            error6 = "- You must agree to the terms and conditions,\n\n";
        } if (!noEmailDuplicates()) {
            error7 = "- That email has already been taken,\n\n";
        } if (!noPhoneDuplicates()) {
            error8 = "- That phone number has already been registered,\n\n";
        }

        errors = error1 + error2 + error3 + error4
                + error5 + error6 + error7 + error8;
        return errors.substring(0, errors.length()-3);
    }

    @Override
    public void onClick(View v) {
        if (v == tvdoB) {
            datePickerDialog.show();
        }

        else if(v == btnRegister) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone_number = etPhoneNumber.getText().toString().trim();

//            if (email.isEmpty() ||)

            if(emailValid() && passwordValid() && phoneNumberValid() && ageValid() && agreementValid() && noEmailDuplicates() && noPhoneDuplicates()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String doB = tvdoB.getText().toString().trim();
                Date date_of_birth = null;
                try {
                    date_of_birth = dateFormat.parse(doB);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                db.registerNewUser(email, password, phone_number, date_of_birth);

                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);

            } else {
                dialog.show();
                tvDialogErrors.setText(initErrors());
            }
        }

        else if (v == btnDialogConfirm) {
            dialog.dismiss();
        }

        else if(v == tvLogin) {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}