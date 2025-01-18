package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VehicleDetailsActivity extends AppCompatActivity {

    private VehicleDatabaseHelper databaseHelper;
    private VehicleAdapter vehicleAdapter;
    private RecyclerView recyclerView;
    private ImageButton addVehicleButton;
    private ImageView backButton;

    private Button saveButton;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvehicle);


        databaseHelper = new VehicleDatabaseHelper(this);
        recyclerView = findViewById(R.id.vehicleRecyclerView);
        addVehicleButton = findViewById(R.id.add_vehicle);
        backButton = findViewById(R.id.back_button);
        saveButton =findViewById(R.id.save_button);

        setupRecyclerView();
        loadVehiclesData();

        addVehicleButton.setOnClickListener(v -> {
            // Pass userId when adding a new vehicle
            Intent intent = new Intent(VehicleDetailsActivity.this, RegisterVehicleActivity.class);
            intent.putExtra("user_id", currentUserId);  // Pass currentUserId when navigating to RegisterVehicleActivity
            startActivity(intent);
        });

        saveButton.setOnClickListener(v -> {
            // Get the userId and username passed from the previous activity
            int userId = getIntent().getIntExtra("userId", -1); // Default to -1 if not found
            String username = getIntent().getStringExtra("username");

            if (userId != -1 && username != null) {
                // Pass userId and username to Homepage activity
                Intent intent = new Intent(VehicleDetailsActivity.this, Homepage.class);
                intent.putExtra("userId", userId);  // Pass the userId
                intent.putExtra("username", username);  // Pass the username
                startActivity(intent);
                finish();  // Close the VehicleDetailsActivity
            } else {
                Toast.makeText(VehicleDetailsActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
            }
        });


        // Retrieve the userId passed from the Homepage activity
        int userId = getIntent().getIntExtra("userId", -1); // Default to -1 if not found

        if (userId != -1) {
            // Use the userId for your logic (e.g., display user details, etc.)
            Log.d("VehicleDetailsActivity", "UserId: " + userId);
        }
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vehicleAdapter = new VehicleAdapter(new ArrayList<>(),
                this::deleteVehicle,
                this::showUpdateVehiclesPage
        );
        recyclerView.setAdapter(vehicleAdapter);
    }

    private void loadVehiclesData() {
        // Get vehicles only for the current logged-in user
        List<Vehicle> vehicles = databaseHelper.getVehiclesByUserId(currentUserId);

        if (vehicles.isEmpty()) {
            Toast.makeText(this, "No vehicles found. Please add a vehicle.", Toast.LENGTH_SHORT).show();
        }

        vehicleAdapter.updateVehicles(vehicles);
    }

    private void deleteVehicle(int vehicleId) {
        boolean result = databaseHelper.deleteVehicle(vehicleId);
        if (result) {
            Toast.makeText(this, "Vehicle deleted", Toast.LENGTH_SHORT).show();
            loadVehiclesData();
        } else {
            Toast.makeText(this, "Failed to delete vehicle", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateVehiclesPage(Vehicle vehicle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View updateLayout = inflater.inflate(R.layout.update_vehicle_details, null);
        builder.setView(updateLayout);

        EditText etPlateNumber = updateLayout.findViewById(R.id.editPlateNumberInput);
        EditText etModelName = updateLayout.findViewById(R.id.editModelNameInput);

        // Pre-fill the fields with the current vehicle data
        etPlateNumber.setText(vehicle.getPlateNumber());
        etModelName.setText(vehicle.getModelName());

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatePlateNumber = etPlateNumber.getText().toString().trim().toUpperCase();
            String updateModelName = etModelName.getText().toString().trim();

            if (!updatePlateNumber.isEmpty() && !updateModelName.isEmpty()) {
                boolean result = databaseHelper.updateVehicle(vehicle.getId(), updatePlateNumber, updateModelName, currentUserId);
                if (result) {
                    loadVehiclesData();
                    Toast.makeText(VehicleDetailsActivity.this, "Vehicle updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VehicleDetailsActivity.this, "Failed to update vehicle", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(VehicleDetailsActivity.this, "Invalid input. Please provide valid data.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
