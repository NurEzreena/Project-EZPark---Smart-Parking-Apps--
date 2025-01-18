package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DigitalReceiptPaymentActivity extends AppCompatActivity {
    private ParkingDatabaseHelper parkingDatabaseHelper;
    private ParkingAdapter parkingAdapter;
    private RecyclerView recyclerView;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_receipt_payment);

        // Initialize helpers
        parkingDatabaseHelper = new ParkingDatabaseHelper(this);

        recyclerView = findViewById(R.id.receiptPaymentRecyclerView);
        backButton = findViewById(R.id.back);

        int userId = getIntent().getIntExtra("userId", -1);
        Log.d("DigitalReceiptPayment", "Received userId: " + userId);

        Log.d("DigitalReceiptPayment", "Received Data - UserId: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "Invalid user data. Exiting.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch parking data from the database using ParkingDatabaseHelper
        List<Parking> parkingList = parkingDatabaseHelper.getParkingsByUserId(userId);

        if (parkingList == null || parkingList.isEmpty()) {
            Log.e("DigitalReceiptPayment", "No parking records found!");
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "No parking records found.", Toast.LENGTH_SHORT).show();
        } else {
            // Sort the parking list by payment date in ascending order
            sortParkingListByPaymentDate(parkingList);

            // Pass the sorted parking data and userId to the adapter
            parkingAdapter = new ParkingAdapter(parkingList, userId);
            recyclerView.setAdapter(parkingAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // Back button click
        backButton.setOnClickListener(v -> finish());
    }

    // Method to sort the parking list by payment date in ascending order
    private void sortParkingListByPaymentDate(List<Parking> parkingList) {
        Collections.sort(parkingList, new Comparator<Parking>() {
            @Override
            public int compare(Parking p1, Parking p2) {
                // Assuming parking.getPaymentDate() returns a date string in a sortable format like "YYYY-MM-DD"
                return p1.getPaymentDate().compareTo(p2.getPaymentDate());
            }
        });
    }
}
