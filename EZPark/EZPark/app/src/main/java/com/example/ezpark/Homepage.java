package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Homepage extends AppCompatActivity {

    private static final int STATE_SELECT_REQUEST = 1; // Define a constant for the request code
    private TextView stateTextView, User_Name, reloadButton;
    private ImageView profile;
    private FloatingActionButton addVehicleButton;
    private ImageView parkNpayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize stateTextView
        stateTextView = findViewById(R.id.stateTextView);
        parkNpayButton = findViewById(R.id.parknpay);
        User_Name = findViewById(R.id.User_Name);
        profile = findViewById(R.id.user_profile);
        addVehicleButton = findViewById(R.id.add_vehicle);
        reloadButton = findViewById(R.id.reloadButton);
        ImageView settingsButton = findViewById(R.id.button_settings);
        ImageButton selectStateButton = findViewById(R.id.select_state);


        // Retrieve the username passed from the previous activity
        String username = getIntent().getStringExtra("username");

        if (username != null && !username.isEmpty()) {
            User_Name.setText(username); // Display username
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
        }

        // Add functionality for the reload button
        reloadButton.setOnClickListener(v -> {
            if (username != null && !username.isEmpty()) {
                Intent intent = new Intent(Homepage.this, ReloadActivity.class);
                intent.putExtra("username", username); // Pass username to ReloadActivity
                startActivity(intent);
            } else {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Add functionality for the profile icon
        if (profile != null) {
            profile.setOnClickListener(view -> {
                if (username != null && !username.isEmpty()) {
                    Intent intent = new Intent(Homepage.this, ProfileActivity.class);
                    intent.putExtra("username", username); // Pass username to ProfileActivity
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Add functionality for the Settings Button
        if (settingsButton != null) {
            settingsButton.setOnClickListener(view -> {
                Intent intent = new Intent(Homepage.this, AppSettingsActivity.class);
                startActivity(intent);
            });
        }

        /*
        // Add functionality for add vehicle button
        if (addVehicleButton != null) {
            addVehicleButton.setOnClickListener(view -> {
                Intent intent = new Intent(Homepage.this, VehicleDetailsActivity.class);
                startActivity(intent);
            });
        }

         */

        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MyVehicle Activity
                Intent intent = new Intent(Homepage.this, VehicleDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Add functionality for selecting a state
        if (selectStateButton != null) {
            selectStateButton.setOnClickListener(view -> {
                Intent intent = new Intent(Homepage.this, StateSelectActivity.class);
                startActivityForResult(intent, STATE_SELECT_REQUEST); // Start activity for result
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the state selection activity
        if (requestCode == STATE_SELECT_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected state from the result
            String selectedState = data.getStringExtra("SELECTED_STATE");

            // Update the TextView with the selected state
            if (selectedState != null) {
                stateTextView.setText(selectedState);

                // Pass the selected state to ParkNPayActivity

                parkNpayButton.setOnClickListener(v -> {
                    Intent intent = new Intent(Homepage.this, ParkNPayActivity.class);
                    intent.putExtra("SELECTED_STATE", selectedState);  // Add the selected state to the Intent
                    startActivity(intent);  // Start ParkNPayActivity
                });

            }
        }
    }

}
