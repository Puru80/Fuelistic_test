package com.example.fuelistic_test.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fuelistic_test.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlaceOrderr extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText deliveryDate;
    Spinner deliveryMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_orderr);

        //Hooks
        deliveryDate= (TextInputEditText) findViewById(R.id.deliveryDate);
        // Spinner element
        deliveryMode = (Spinner) findViewById(R.id.deliveryMode);
        // Spinner click listener
        deliveryMode.setOnItemSelectedListener(this);

        // Spinner Drop down elements for delivery mode
        List<String> deliveryMode_categories = new ArrayList<String>();
        deliveryMode_categories.add("Generator");
        deliveryMode_categories.add("Oil Can");
        deliveryMode_categories.add("Drum");
        deliveryMode_categories.add("Machine");
        deliveryMode_categories.add("Non-Mobile Engine");
        deliveryMode_categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> deliveryMode_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deliveryMode_categories);

        // Drop down layout style - list view with radio button
        deliveryMode_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        deliveryMode.setAdapter(deliveryMode_dataAdapter);
    }

    final Calendar myCalendar = Calendar.getInstance();
    long minDate  = myCalendar.getTimeInMillis();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void selectDate(View view) {
        DatePickerDialog dpd = new DatePickerDialog(PlaceOrderr.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        dpd.getDatePicker().setMinDate(minDate);
        dpd.show();
    } ;

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        deliveryDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // String item = parent.getItemAtPosition(position).toString();
        // item above is the deliveryMode selected, we will see later iska kya krna h
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // leaving it empty for now
    }
}