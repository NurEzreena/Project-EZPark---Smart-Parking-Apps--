package com.example.ezpark;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private UserDatabaseHelper userDatabaseHelper;
    private Spinner spinnerSelectVehicle;
    private TextView dateChosen, hourChosen;
    private ImageView calendarView;
    private Button Button1Hour, Button2Hour, Button3Hour, Button4Hour, PayButton;
    private ImageView backButton;
    private String selectedState;
    private int userId;

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
        calendarView = findViewById(R.id.date_calendar);
        Button1Hour = findViewById(R.id.button_1_hour);
        Button2Hour = findViewById(R.id.button_2_hour);
        Button3Hour = findViewById(R.id.button_3_hour);
        Button4Hour = findViewById(R.id.button_4_hour);
        PayButton = findViewById(R.id.ParkNPay_pay_button);
        backButton = findViewById(R.id.back_button);

        Log.d("ViewClass", "Class of date_calendar: " + calendarView.getClass().getName());


        int userId = getIntent().getIntExtra("userId", -1);
        String selectedState = getIntent().getStringExtra("SELECTED_STATE");

        Log.d("ParkNPayActivity", "Received Data - UserId: " + userId + ", SelectedState: " + selectedState);

        if (userId == -1 || selectedState == null || selectedState.isEmpty()) {
            Toast.makeText(this, "Invalid data received. Exiting.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Populate spinner and set listeners
        populateSpinner(spinnerSelectVehicle);
        setSpinnerItemSelectedListener(spinnerSelectVehicle);

        // Date picker interaction
        calendarView.setOnClickListener(v -> showDatePicker());

        // Hour selection buttons
        setupHourButtons();

        backButton.setOnClickListener(v -> onBackPressed());

        // Handle PayButton interaction
        PayButton.setOnClickListener(v -> {
            Log.d("PayButton", "Pay button clicked.");
            handleParkingSession();
        });

        if (selectedState != null) {
            Toast.makeText(this, "Selected State: " + selectedState, Toast.LENGTH_SHORT).show();
        }


    }

    private void setupHourButtons() {
        Button1Hour.setOnClickListener(v -> updateHourChosen("1 Hour"));
        Button2Hour.setOnClickListener(v -> updateHourChosen("2 Hours"));
        Button3Hour.setOnClickListener(v -> updateHourChosen("3 Hours"));
        Button4Hour.setOnClickListener(v -> updateHourChosen("4 Hours"));
    }

    private void updateHourChosen(String selectedHour) {
        hourChosen.setText(selectedHour);
        Toast.makeText(this, "Selected: " + selectedHour, Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Month is 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                            selectedDay, selectedMonth + 1, selectedYear);
                    dateChosen.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void populateSpinner(Spinner spinner) {
        // Fetch vehicles for the specific user
        List<Vehicle> vehicleList = vehicleDatabaseHelper.getVehiclesByUserId(userId);

        // Log fetched vehicles to ensure they are coming through
        Log.d("ParkNPayActivity", "Fetched vehicles: " + vehicleList);

        // Add placeholder as the first item
        vehicleList.add(0, new Vehicle(-1, "Select Vehicle", "", -1));

        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, vehicleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



    private void setSpinnerItemSelectedListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                Vehicle selectedVehicle = (Vehicle) parentView.getItemAtPosition(position);
                if (selectedVehicle.getId() == -1) {
                    Toast.makeText(ParkNPayActivity.this, "Please select a valid vehicle.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ParkNPayActivity.this, "Selected Vehicle: " + selectedVehicle.getPlateNumber(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });
        /*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
        Vehicle selectedVehicle = (Vehicle) parentView.getItemAtPosition(position);
        if (selectedVehicle.getId() == -1) {
            Toast.makeText(ParkNPayActivity.this, "Please select a valid vehicle.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ParkNPayActivity.this, "Selected Vehicle: " + selectedVehicle.getPlateNumber(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        // No action needed
    }
});

         */
    }

    private void handleParkingSession() {
        Vehicle selectedVehicle = (Vehicle) spinnerSelectVehicle.getSelectedItem();
        if (selectedVehicle == null || selectedVehicle.getId() == -1) {
            Toast.makeText(this, "Please select a valid vehicle.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedDate = dateChosen.getText().toString();
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a valid date.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedHour = hourChosen.getText().toString();
        if (selectedHour.isEmpty()) {
            Toast.makeText(this, "Please select a parking duration.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalCost;
        switch (selectedHour) {
            case "1 Hour":
                totalCost = 5.0;
                break;
            case "2 Hours":
                totalCost = 9.0;
                break;
            case "3 Hours":
                totalCost = 12.0;
                break;
            case "4 Hours":
                totalCost = 15.0;
                break;
            default:
                totalCost = 0.0;
        }

        boolean isParkingSessionAdded = parkingDatabaseHelper.addParking(
                selectedVehicle.getId(),
                totalCost,
                selectedHour,
                selectedDate,
                selectedState,
                userId
        );

        if (isParkingSessionAdded) {
            Toast.makeText(this, "Parking session started!", Toast.LENGTH_SHORT).show();

            // Pass selected vehicle number, hour, and total cost to Homepage
            Intent intent = new Intent(ParkNPayActivity.this, Homepage.class);
            intent.putExtra("userId", userId);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("vehicle_number", selectedVehicle.getPlateNumber()); // Pass vehicle number
            intent.putExtra("selected_hour", selectedHour); // Pass parking duration
            intent.putExtra("total_cost", totalCost); // Pass the total cost
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to start parking session.", Toast.LENGTH_SHORT).show();
        }
    }


    private void backAction() {
        onBackPressed();
    }
}