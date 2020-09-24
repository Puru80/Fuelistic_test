package com.example.fuelistic_test.LoginSignup;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelistic_test.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber, password;
    RelativeLayout progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNumber = findViewById(R.id.login_phone_number);
        progressbar = findViewById(R.id.login_progress_bar);
        password = findViewById(R.id.login_password);


    }

    //allow login
    public void letUserLogIn(View view) {
        //validate phone number & password
        if (!validateCredentials()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        ////get data
        String _phoneNo = phoneNumber.getEditText().getText().toString().trim();
        final String _password = password.getEditText().getText().toString().trim();

        //Remove first zero if entered!
        if (_phoneNo.charAt(0) == '0') {
            _phoneNo = _phoneNo.substring(1);
        }

        final String _completePhoneNo = "+" + countryCodePicker.getFullNumber() + _phoneNo;

        //Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_completePhoneNo).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _fullname = snapshot.child(_completePhoneNo).child("fullName").getValue(String.class);
                        String _email = snapshot.child(_completePhoneNo).child("email").getValue(String.class);
                        String _phoneNo = snapshot.child(_completePhoneNo).child("phoneNo").getValue(String.class);
                        String _dateOfBirth = snapshot.child(_completePhoneNo).child("dateOfBirth").getValue(String.class);

                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Password does not match!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "User does not exist!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private boolean validateCredentials() {
        String _phoneNummber = phoneNumber.getEditText().getText().toString().trim();

        String _password = password.getEditText().getText().toString().trim();


        if (_phoneNummber.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else {
            if (_password.isEmpty()) {
                password.setError("Field can not be empty");
                return false;
            }
            return true;

        }
    }


    public void callSignUpScreen(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
    }
}