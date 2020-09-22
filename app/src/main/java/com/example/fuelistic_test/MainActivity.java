package com.example.fuelistic_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.fuelistic_test.LoginSignup.AppStartupScreen;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_SCREEN= 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(MainActivity.this, AppStartupScreen.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN );


    }
}