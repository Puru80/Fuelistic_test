package com.example.fuelistic_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fuelistic_test.Database.SessionManager;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;

public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        TextView textView = findViewById(R.id.textView2);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);
        String phooneNo = userDetails.get(SessionManager.KEY_PHONENUMBER);

        textView.setText(fullName+"\n" + phooneNo);
    }


}