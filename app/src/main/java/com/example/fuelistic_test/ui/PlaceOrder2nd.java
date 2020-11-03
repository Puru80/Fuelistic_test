package com.example.fuelistic_test.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelistic_test.Database.SessionManager;
import com.example.fuelistic_test.Common.Common;
import com.example.fuelistic_test.Model.Order;
import com.example.fuelistic_test.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class PlaceOrder2nd extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;

    EditText edit_address;
    EditText edit_comment;
    TextView text_address;
    RadioButton rdb_home_add;
    RadioButton rdb_other_add;
    RadioButton rdb_use_current_add;
    RadioButton rdb_cod;
    RadioButton rdb_braintree;

    double totalPrice = 0;

    String deliveryDate , deliveryMode, fuelType,orderQuantity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order2nd);

        //get data from prev screen
        //Get all the data from Intent
        deliveryDate = getIntent().getStringExtra("deliveryDate");
        deliveryMode = getIntent().getStringExtra("deliveryMode");
//        fuelType = getIntent().getStringExtra("fuelType");
        orderQuantity = getIntent().getStringExtra("orderQuantity");


        edit_address = findViewById(R.id.edit_address);
        edit_comment = findViewById(R.id.edit_comment);
        text_address = findViewById(R.id.text_address_detail);
        rdb_home_add = findViewById(R.id.rdb_home_add);
        rdb_other_add = findViewById(R.id.rdb_other_add);
        rdb_use_current_add = findViewById(R.id.rdb_use_current_add);
        rdb_cod = findViewById(R.id.rdb_cod);
        rdb_braintree = findViewById(R.id.rdb_braintree);



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
                                            edit_address.setText(s);
                                            //text_address.setText(s);
                                            text_address.setVisibility(View.GONE);
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

    @Override
    public void onStop(){
        if(fusedLocationProviderClient!=null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        compositeDisposable.clear();
        super.onStop();
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

    public void place_Order(View view) {
        if(rdb_cod.isChecked())
            paymentCOD(edit_address.getText().toString(), edit_comment.getText().toString());
    }

    private void paymentCOD(String address, String comment) {
        double finalPrice = totalPrice ;
        Order order = new Order();

        //trying to get data from session manager
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        order.setUserName(userDetails.get(SessionManager.KEY_USERNAME));
        order.setUserPhone(userDetails.get(SessionManager.KEY_PHONENUMBER));
        order.setShippingAddress(address);
        order.setComment(comment);

        if(currentLocation != null){
            order.setLat(currentLocation.getLatitude());
            order.setLng(currentLocation.getLongitude());
        }
        else{
            order.setLng(-0.1f);
            order.setLat(-0.1f);
        }

        order.setTotalPayment(totalPrice);
        order.setDeliveryCharge(0);  //laterr
        order.setFinalPayment(finalPrice);
        order.setCod(true);
        order.setTransactionId("Cash On Delivery");
        order.setFuelType(fuelType);
        order.setQuantity(orderQuantity);
        order.setDeliveryDate(deliveryDate);
        order.setDeliveryMode(deliveryMode);

        writeOrderToFirebase(order);
    }

    private void writeOrderToFirebase(Order order) {
        FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(Common.createOrderNumber())
                .setValue(order)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(this, " AADD",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(intent);
            }
        });
    }
}