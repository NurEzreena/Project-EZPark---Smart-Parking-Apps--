package com.example.ezpark;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParkNPayActivity extends AppCompatActivity {
    private ParkingDatabaseHelper parkingDatabaseHelper;
    private VehicleDatabaseHelper vehicleDatabaseHelper;
    private Spinner spinnerSelectVehicle;
    private TextView dateChosen, hourChosen;
    private ImageButton Calendar, backButton;
    private Button Button1Hour, Button2Hour, Button3Hour, Button4Hour, PayButton;
    private String selectedState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_n_pay);

        // Initialize helpers
        parkingDatabaseHelper = new ParkingDatabaseHelper(this);
        vehicleDatabaseHelper = new VehicleDatabaseHelper(this);

        // Initialize UI components
        spinnerSelectVehicle = findViewById(R.id.spinner_selectVehicle);
        dateChosen = findViewById(R.id.tv_date_chosen);
        hourChosen = findViewById(R.id.hourChosenInput);
        Calendar = findViewById(R.id.date_calendar);
        Button1Hour = findViewById(R.id.button_1_hour);
        Button2Hour = findViewById(R.id.button_2_hour);
        Button3Hour = findViewById(R.id.button_3_hour);
        Button4Hour = findViewById(R.id.button_4_hour);
        PayButton = findViewById(R.id.ParkNPay_pay_button);

        // Back button functionality
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> backAction());


        // Inside PayButton's onClickListener in ParkNPayActivity
        PayButton.setOnClickListener(v -> {
            // Check if a valid vehicle is selected
            Vehicle selectedVehicle = (Vehicle) spinnerSelectVehicle.getSelectedItem();
            if (selectedVehicle == null || selectedVehicle.getId() == -1) {
                // No vehicle selected, redirect to RegisterVehicleActivity
                Intent intent = new Intent(ParkNPayActivity.this, RegisterVehicleActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent the user from coming back to it
            } else {
                // Valid vehicle selected, proceed with payment
                Intent intent = new Intent(ParkNPayActivity.this, DigitalReceiptPaymentActivity.class);
                intent.putExtra("SELECTED_STATE", selectedState);
                startActivity(intent);
                handleParkingSession();
            }
        });


        // Set up DatePicker for Calendar button
        Calendar.setOnClickListener(v -> showDatePicker());

        // Set up hour buttons
        setupHourButtons();

        // Populate spinner and set listeners
        populateSpinner(spinnerSelectVehicle);
        setSpinnerItemSelectedListener(spinnerSelectVehicle);

        // Retrieve the selected state from the Intent and avoid reinitializing it globally and locally
        selectedState = getIntent().getStringExtra("SELECTED_STATE");
        if (selectedState != null) {
            Toast.makeText(this, "Selected State: " + selectedState, Toast.LENGTH_SHORT).show();
        }

    }


    // Method to set up hour button listeners
    private void setupHourButtons() {
        Button1Hour.setOnClickListener(v -> updateHourChosen("1 Hour"));
        Button2Hour.setOnClickListener(v -> updateHourChosen("2 Hours"));
        Button3Hour.setOnClickListener(v -> updateHourChosen("3 Hours"));
        Button4Hour.setOnClickListener(v -> updateHourChosen("4 Hours"));
    }

    // Method to update hourChosen TextView
    private void updateHourChosen(String selectedHour) {
        hourChosen.setText(selectedHour);
        Toast.makeText(this, "Selected: " + selectedHour, Toast.LENGTH_SHORT).show();
    }

    // Method to show DatePickerDialog using Date
    private void showDatePicker() {
        // Get the current date
        Date currentDate = new Date();
        // Use Calendar to get the current date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH); // Month is 0-based (January = 0)
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date
                    Date selectedDate = new Date(selectedYear - 1900, selectedMonth, selectedDay);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate);

                    // Display the date in the TextView
                    dateChosen.setText(formattedDate);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Method to populate the Spinner with vehicle data from SQLite
    private void populateSpinner(Spinner spinner) {
        // Fetch the data from SQLite Database
        List<Vehicle> vehicleList = vehicleDatabaseHelper.getAllVehicles();

        // Add a placeholder as the first item
        vehicleList.add(0, new Vehicle(-1, "Select Vehicle", "")); // Dummy ID for placeholder

        // Create an ArrayAdapter using the vehicle list
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, vehicleList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    // Method to handle Spinner item selection
    private void setSpinnerItemSelectedListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                // Get the selected Vehicle object
                Vehicle selectedVehicle = (Vehicle) parentView.getItemAtPosition(position);

                // Check if placeholder ("Select Vehicle") is selected
                if (selectedVehicle.getId() == -1) {
                    Toast.makeText(ParkNPayActivity.this, "Please select a valid vehicle.", Toast.LENGTH_SHORT).show();
                } else {
                    // Display selected vehicle details
                    Toast.makeText(ParkNPayActivity.this,
                            "Selected Vehicle: " + selectedVehicle.getPlateNumber(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when no item is selected
            }
        });
    }

    // Inside handleParkingSession() method of ParkNPayActivity
    private void handleParkingSession() {
        // Get selected vehicle from the spinner
        Vehicle selectedVehicle = (Vehicle) spinnerSelectVehicle.getSelectedItem();

        // Check if a valid vehicle is selected
        if (selectedVehicle.getId() == -1) {
            Toast.makeText(ParkNPayActivity.this, "Please select a valid vehicle.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected parking date
        String selectedDate = dateChosen.getText().toString();
        if (selectedDate.isEmpty()) {
            Toast.makeText(ParkNPayActivity.this, "Please select a valid date.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected parking hour
        String selectedHour = hourChosen.getText().toString();
        if (selectedHour.isEmpty()) {
            Toast.makeText(ParkNPayActivity.this, "Please select a parking duration.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Example: Calculate total cost based on selected hour
        double totalCost = 0.0;
        switch (selectedHour) {
            case "1 Hour":
                totalCost = 5.0; // Example cost for 1 hour
                break;
            case "2 Hours":
                totalCost = 9.0; // Example cost for 2 hours
                break;
            case "3 Hours":
                totalCost = 12.0; // Example cost for 3 hours
                break;
            case "4 Hours":
                totalCost = 15.0; // Example cost for 4 hours
                break;
        }

        // Add the parking session to the database
        boolean isParkingSessionAdded = parkingDatabaseHelper.addParking(
                selectedVehicle.getId(),
                totalCost,
                selectedHour,
                selectedDate,
                selectedState
        );
        if (isParkingSessionAdded) {
            Toast.makeText(ParkNPayActivity.this, "Parking session started!", Toast.LENGTH_SHORT).show();

            // Retrieve the last inserted parking session ID
            long parkingId = parkingDatabaseHelper.getLastInsertedParkingId();
            // Start the DigitalReceiptPaymentActivity
            Intent intent = new Intent(ParkNPayActivity.this, DigitalReceiptPaymentActivity.class);
            intent.putExtra("PARKING_ID", parkingId);
            startActivity(intent);
        } else {
            Toast.makeText(ParkNPayActivity.this, "Failed to start parking session. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void backAction() {
        // Navigate back to previous screen
        onBackPressed();
    }
}
