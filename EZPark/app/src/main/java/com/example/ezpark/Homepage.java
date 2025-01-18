package com.example.ezpark;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Homepage extends AppCompatActivity {

    private static final int STATE_SELECT_REQUEST = 1;
    private TextView stateTextView, User_Name, buttonReload, walletBalanceView;
    private ImageView profile;
    private UserDatabaseHelper dbHelper;
    private FloatingActionButton addVehicleButton;
    private ImageView parkNpay, digitalReceipt;
    private String username; // Declare username as class-level
    private int userId;      // Declare userId as class-level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize views
        stateTextView = findViewById(R.id.stateTextView);
        User_Name = findViewById(R.id.User_Name);
        profile = findViewById(R.id.user_profile);
        addVehicleButton = findViewById(R.id.add_vehicle);
        buttonReload = findViewById(R.id.buttonReload);
        ImageView settingsButton = findViewById(R.id.button_settings);
        ImageButton selectStateButton = findViewById(R.id.select_state);
        walletBalanceView = findViewById(R.id.walletBalanceView);
        parkNpay = findViewById(R.id.parknpay);
        digitalReceipt = findViewById(R.id.invoice);

        dbHelper = new UserDatabaseHelper(this);

        // Retrieve data from the intent
        username = getIntent().getStringExtra("username");
        userId = getIntent().getIntExtra("userId", -1);


        Log.d("Homepage", "Received Data - Username: " + username + ", UserId: " + userId);

        // Set username if available
        if (username != null && !username.isEmpty()) {
            User_Name.setText(username);
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
        }

        // Check if userId is valid
        if (userId == -1) {
            Toast.makeText(this, "Invalid user data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reload button
        buttonReload.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ReloadActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Settings button
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, AppSettingsActivity.class);
            intent.putExtra("username", username);  // Pass username
            intent.putExtra("userId", userId);  // Pass userId
            startActivity(intent);
        });


        // Profile button
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ProfileActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Navigate to ParkNPayActivity
        parkNpay.setOnClickListener(v -> checkAndNavigate(userId, ParkNPayActivity.class));


        // Navigate to DigitalReceiptPaymentActivity
        digitalReceipt.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, DigitalReceiptPaymentActivity.class);
            intent.putExtra("userId", userId); // Pass userId to the next activity
            startActivity(intent);
        });



        // Vehicle details
        addVehicleButton.setOnClickListener(v -> checkAndNavigate(userId, VehicleDetailsActivity.class));

        // Select State
        selectStateButton.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, StateSelectActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, STATE_SELECT_REQUEST);
        });


        // Initialize views
        TextView vehicleNumberView = findViewById(R.id.vehicle_number);
        TextView hoursView = findViewById(R.id.hours);
        TextView minutesView = findViewById(R.id.minutes);
        TextView secondsView = findViewById(R.id.seconds);

        // Retrieve passed data
        String selectedVehicleNumber = getIntent().getStringExtra("vehicle_number");
        String selectedHour = getIntent().getStringExtra("selected_hour");

        // Set the retrieved values to the TextViews
        if (selectedVehicleNumber != null) {
            vehicleNumberView.setText(selectedVehicleNumber);
        }

        if (selectedHour != null) {
            // Extract hour value from the format (e.g., "1 Hour" -> "1")
            String hourValue = selectedHour.split(" ")[0];
            hoursView.setText(hourValue);

            // Convert hour to milliseconds and start timer
            int hourInMillis = Integer.parseInt(hourValue) * 60 * 60 * 1000; // Convert hour to milliseconds
            startTimer(hourInMillis, hoursView, minutesView, secondsView);
        }
    }

    private void loadUserData(String username) {
        Cursor cursor = dbHelper.getUserDetails(username);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    double walletBalance = cursor.getDouble(cursor.getColumnIndexOrThrow("wallet_balance"));
                    walletBalanceView.setText(String.format(Locale.getDefault(), "RM%.2f", walletBalance));
                } else {
                    Log.e("Homepage", "Cursor is empty. No data found for username: " + username);
                    walletBalanceView.setText("RM0.00");
                }
            } finally {
                cursor.close(); // Always close the cursor
            }
        } else {
            Log.e("Homepage", "Cursor is null. Unable to fetch user details for username: " + username);
            walletBalanceView.setText("RM0.00");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (username != null && !username.isEmpty()) {
            loadUserData(username);
        }
    }

    private void checkAndNavigate(int userId, Class<?> targetActivity) {
        if (userId == -1) {
            Toast.makeText(this, "Invalid user data", Toast.LENGTH_SHORT).show();
            return;
        }

        String state = stateTextView.getText().toString();
        if (state == null || state.isEmpty()) {
            Toast.makeText(this, "Please select a state", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Homepage.this, targetActivity);
        intent.putExtra("username", username);
        intent.putExtra("userId", userId);
        intent.putExtra("SELECTED_STATE", state);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STATE_SELECT_REQUEST && resultCode == RESULT_OK && data != null) {
            String selectedState = data.getStringExtra("SELECTED_STATE");
            if (selectedState != null) {
                stateTextView.setText(selectedState);
            }
        }
    }

    private void startTimer(long durationInMillis, TextView hoursView, TextView minutesView, TextView secondsView) {
        new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
                int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                // Update the TextViews
                hoursView.setText(String.valueOf(hours));
                minutesView.setText(String.valueOf(minutes));
                secondsView.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                // Timer finished
                hoursView.setText("0");
                minutesView.setText("0");
                secondsView.setText("0");

                Toast.makeText(Homepage.this, "Parking session ended!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

}
