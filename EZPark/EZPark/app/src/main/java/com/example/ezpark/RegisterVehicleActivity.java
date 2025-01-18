package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterVehicleActivity extends AppCompatActivity {

    private EditText plateNumberInput;
    private EditText modelNameInput;
    private Button registerButton;
    private Button cancelButton;
    private VehicleDatabaseHelper databaseHelper; // Initialize the database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehicle);

        // Initialize the database helper
        databaseHelper = new VehicleDatabaseHelper(this);

        // Initialize UI components
        plateNumberInput = findViewById(R.id.plateNumberInput);
        modelNameInput = findViewById(R.id.modelNameInput);
        registerButton = findViewById(R.id.registerButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Set up the register button click listener
        registerButton.setOnClickListener(v -> handleVehicleRegistration());

        cancelButton.setOnClickListener(v -> cancelAction());
    }

    /**
     * Handles the registration of a new vehicle.
     */
    private void handleVehicleRegistration() {
        // Get user input
        String plateNumber = plateNumberInput.getText().toString().trim().toUpperCase(); // Convert to uppercase
        String modelName = modelNameInput.getText().toString().trim();

        // Validate input fields
        if (plateNumber.isEmpty() || modelName.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the vehicle to the database
        boolean isAdded = databaseHelper.addVehicle(plateNumber, modelName);

        // Show appropriate feedback to the user
        if (isAdded) {
            Toast.makeText(this, "Vehicle added successfully!", Toast.LENGTH_SHORT).show();

            // Start VehicleDetailsActivity after successful registration
            Intent intent = new Intent(RegisterVehicleActivity.this, VehicleDetailsActivity.class);
            startActivity(intent);  // Navigate to the VehicleDetailsActivity
            finish(); // Close RegisterVehicleActivity
        } else {
            Toast.makeText(this, "Failed to add vehicle. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    public void  cancelAction(){
        Toast.makeText(this, "Cancel register vehicle.",Toast.LENGTH_SHORT).show();
        finish();
    }
}

