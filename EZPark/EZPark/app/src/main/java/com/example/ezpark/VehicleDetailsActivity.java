package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvehicle); // Set the layout

        databaseHelper = new VehicleDatabaseHelper(this);
        recyclerView = findViewById(R.id.vehicleRecyclerView);
        addVehicleButton = findViewById(R.id.add_vehicle);
        backButton = findViewById(R.id.back_button);
        saveButton= findViewById(R.id.save_button);

        setupRecyclerView();  // Initialize RecyclerView
        loadVehiclesData();   // Load and display vehicle data

        // Navigate to RegisterVehicleActivity to add a new vehicle
        addVehicleButton.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleDetailsActivity.this, RegisterVehicleActivity.class);
            startActivity(intent); //
        });

        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleDetailsActivity.this, Homepage.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> { backAction(); });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set vertical layout
        vehicleAdapter = new VehicleAdapter(new ArrayList<>(),
                this::deleteVehicle, // Delete action
                this::showUpdateVehiclesPage // Update action
        );
        recyclerView.setAdapter(vehicleAdapter); // Attach the adapter
    }

    private void loadVehiclesData() {
        List<Vehicle> vehicles = databaseHelper.getAllVehicles();

        if (vehicles.isEmpty()) {
            Toast.makeText(this, "No vehicles found. Please add a vehicle.", Toast.LENGTH_SHORT).show();
        }

        vehicleAdapter.updateVehicles(vehicles); // Refresh the adapter with the latest data
    }

    private void deleteVehicle(int vehicleId) {
        boolean result = databaseHelper.deleteVehicles(vehicleId);
        if (result) {
            Toast.makeText(this, "Vehicle deleted", Toast.LENGTH_SHORT).show();
            loadVehiclesData(); // Refresh data
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
            String updatePlateNumber = etPlateNumber.getText().toString().trim().toUpperCase(); // Convert to uppercase
            String updateModelName = etModelName.getText().toString().trim();

            // Validate input
            if (!updatePlateNumber.isEmpty() && !updateModelName.isEmpty()) {
                boolean result = databaseHelper.updateVehicles(vehicle.getId(), updatePlateNumber, updateModelName);
                if (result) {
                    loadVehiclesData(); // Refresh the vehicle list after update
                    Toast.makeText(VehicleDetailsActivity.this, "Vehicle updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VehicleDetailsActivity.this, "Failed to update vehicle", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(VehicleDetailsActivity.this, "Invalid input. Please provide valid data.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        // Optionally, add a "Cancel" button to dismiss the dialog
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss(); // Just dismiss the dialog
        });

        builder.show(); // Display the alert dialog
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadVehiclesData(); // Refresh data when returning from RegisterVehicleActivity
        }
    }

    public void backAction(){
        finish();
    }


}
