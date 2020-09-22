package com.example.fuelistic_test.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fuelistic_test.Login;
import com.example.fuelistic_test.R;

public class SignUp3rdScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3rd_screen);
    }

    public void callLoginScreen(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }
}