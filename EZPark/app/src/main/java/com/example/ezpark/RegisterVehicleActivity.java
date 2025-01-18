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
    private VehicleDatabaseHelper databaseHelper; // We are using VehicleDatabaseHelper for vehicle-related operations
    private int currentUserId;  // This will hold the current user's ID, assuming the user is logged in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehicle);

        // Initialize the database helper for vehicle-related operations
        databaseHelper = new VehicleDatabaseHelper(this);

        // Get the logged-in user's ID from the intent
        currentUserId = getIntent().getIntExtra("user_id", -1); // Retrieve userId passed from VehicleDetailsActivity

        // If userId is invalid (i.e., -1), show an error and stop the registration process
        if (currentUserId == -1) {
            Toast.makeText(this, "Invalid user. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity as user is not valid
            return;
        }

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
        // Get vehicle input
        String plateNumber = plateNumberInput.getText().toString().trim().toUpperCase(); // Convert to uppercase
        String modelName = modelNameInput.getText().toString().trim();

        // Validate input fields
        if (plateNumber.isEmpty() || modelName.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure that we have a valid user ID (i.e., the user is logged in)
        if (currentUserId == -1) {
            Toast.makeText(this, "Invalid user. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the vehicle to the database under the current user
        boolean isAdded = databaseHelper.addVehicle(plateNumber, modelName, currentUserId); // Pass the userId along with plate number and model name

        if (isAdded) {
            Toast.makeText(this, "Vehicle added successfully!", Toast.LENGTH_SHORT).show();

            // Pass the userId back to VehicleDetailsActivity
            Intent intent = new Intent(RegisterVehicleActivity.this, VehicleDetailsActivity.class);
            intent.putExtra("userId", currentUserId);  // Pass the current userId
            startActivity(intent);  // Navigate to VehicleDetailsActivity
            finish(); // Close RegisterVehicleActivity
        } else {
            Toast.makeText(this, "Failed to add vehicle. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cancels the vehicle registration.
     */
    public void cancelAction() {
        Toast.makeText(this, "Cancel vehicle registration.", Toast.LENGTH_SHORT).show();
        finish();  // Close the activity without saving
    }

}
