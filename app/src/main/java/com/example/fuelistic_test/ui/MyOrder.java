package com.example.fuelistic_test.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.fuelistic_test.R;
import com.google.android.material.navigation.NavigationView;

public class MyOrder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_order);

        //Menu Hooks
        drawerLayout = findViewById(R.id.my_order_drawer__layout);
        navigationView = findViewById(R.id.my_order_navigation__view);

        //Navigation drawer menu
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav__myOrders);

    }

    public void callPlaceOrderfromMyOrders(View view) {
        Intent intent = new Intent(getApplicationContext(), PlaceOrderr.class);
        startActivity(intent);
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

        switch(item.getItemId()){
            case R.id.nav__home:
                Intent intent = new Intent(MyOrder.this, UserDashboard.class);
                startActivity(intent);
                break;
            case R.id.nav__myOrders:
                break;
//            case R.id.nav__home:
//
//                break;
//            case R.id.nav__home:
//                break;
//            case R.id.nav__home:
//                break;
//            case R.id.nav__home:
//                break;
//            case R.id.nav__home:
//                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}