package com.example.fuelistic_test.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelistic_test.Database.SessionManager;
import com.example.fuelistic_test.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class PlaceOrder2nd extends AppCompatActivity {

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order2nd);


        final EditText edit_address = findViewById(R.id.edit_address);
        EditText edit_comment = findViewById(R.id.edit_comment);
        final TextView text_address = findViewById(R.id.text_address_detail);
        RadioButton rdb_home_add = findViewById(R.id.rdb_home_add);
        RadioButton rdb_other_add = findViewById(R.id.rdb_other_add);
        RadioButton rdb_use_current_add = findViewById(R.id.rdb_use_current_add);
        RadioButton rdb_cod = findViewById(R.id.rdb_cod);
        RadioButton rdb_braintree = findViewById(R.id.rdb_braintree);

        //trying to get data from session manager
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        final String address = userDetails.get(SessionManager.KEY_ADDRESS);

        //Data
        edit_address.setText(address);

        //Event
        rdb_home_add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edit_address.setText(address);
                    text_address.setVisibility(View.GONE);
                }
            }
        });

        rdb_other_add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edit_address.setText(" "); //clear
                    edit_address.setHint("Enter your address..");
                    text_address.setVisibility(View.GONE);
                }
            }
        });

        rdb_use_current_add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fusedLocationProviderClient.getLastLocation()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    text_address.setVisibility(View.GONE);
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    final String coordinates = new StringBuilder()
                                            .append(task.getResult().getLatitude())
                                            .append("/")
                                            .append(task.getResult().getLongitude()).toString();

                                    Single<String> singleAddress = Single.just(getAddressFromLatLng(task.getResult().getLatitude(),
                                            task.getResult().getLongitude()));

                                    Disposable disposable = singleAddress.subscribeWith(new DisposableSingleObserver<String>(){
                                        @Override
                                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull String s) {
                                            edit_address.setText(coordinates);
                                            text_address.setText(s);
                                            text_address.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            edit_address.setText(coordinates);
                                            text_address.setText(e.getMessage());
                                            text_address.setVisibility(View.VISIBLE);
                                        }
                                    });





                                }
                            });
                }
            }
        });

        initLocation();


//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //Implement later
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();

    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this , Locale.getDefault());
        String result = "";
        try{
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            if (addressList !=null && addressList.size()>0){
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder(address.getAddressLine(0));
                result = sb.toString();
            }
            else {
                result = "Address not found!!";
            }

        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
            }
        };

    }

    private void buildLocationRequest() {
        locationRequest= new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }


}