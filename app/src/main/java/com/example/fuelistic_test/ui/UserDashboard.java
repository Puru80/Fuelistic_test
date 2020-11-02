package com.example.fuelistic_test.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.fuelistic_test.Database.SessionManager;
import com.example.fuelistic_test.LoginSignup.Login;
import com.example.fuelistic_test.LoginSignup.SignUp;
import com.example.fuelistic_test.R;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    ImageView homeMenuIcon;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Hooks
        homeMenuIcon = findViewById(R.id.home_menu_icon);


        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer__layout);
        navigationView = findViewById(R.id.navigation__view);

        navigationDrawer();


    }

    private void navigationDrawer() {

        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav__home);

        homeMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    public void callPlaceOrder(View view) {
        Intent intent = new Intent(getApplicationContext(), PlaceOrderr.class);
        startActivity(intent);

    }
}