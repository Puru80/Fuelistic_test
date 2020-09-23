package com.example.fuelistic_test.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.fuelistic_test.Login;
import com.example.fuelistic_test.R;

public class AppStartupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_app_startup_screen);
    }

    public void callLoginScreen(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    public void callSignUpScreen(View view){
        startActivity(new Intent(getApplicationContext(), SignUp.class));
    }


}