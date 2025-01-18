package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ezpark.Parking;
import java.util.ArrayList;
import java.util.List;

public class DigitalReceiptPaymentActivity extends AppCompatActivity {
    private ParkingDatabaseHelper parkingDatabaseHelper;
    private ParkingAdapter parkingAdapter;
    private RecyclerView recyclerView;
    private Button btnWallet;
    private ImageButton backButton;
    private long parkingId;  // Declare the parking ID variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_receipt_payment);

        parkingDatabaseHelper = new ParkingDatabaseHelper(this);
        recyclerView = findViewById(R.id.receiptPaymentRecyclerView);
        btnWallet = findViewById(R.id.btn_goWallet);
        backButton = findViewById(R.id.back_button);

        // Get parking ID from the Intent
        parkingId = getIntent().getLongExtra("PARKING_ID", -1);  // Default to -1 if not found

        // Setup RecyclerView
        setupRecyclerView();

        // Load data into the RecyclerView
        loadParkingData();

        // Handle Wallet Button Click
        btnWallet.setOnClickListener(v -> startActivity(new Intent(this, DigitalReceiptWalletActivity.class)));

        // Handle Back Button Click
        backButton.setOnClickListener(v -> backAction());
    }

    private void setupRecyclerView() {
        if (recyclerView == null) {
            Log.e("DigitalReceiptPayment", "RecyclerView not found!");
            return;
        }

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pass the selected state and parking ID
        String selectedState = getIntent().getStringExtra("SELECTED_STATE");  // Get the state from the intent
        parkingAdapter = new ParkingAdapter(new ArrayList<>(), selectedState, parkingId); // Pass the ID
        recyclerView.setAdapter(parkingAdapter);
    }

    private void loadParkingData() {
        // Fetch all parking data from the database
        List<Parking> parkingList = parkingDatabaseHelper.getAllParkings();

        if (parkingList.isEmpty()) {
            Log.e("DigitalReceiptPayment", "No parking records found!");
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "No parking records found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If data exists, show the RecyclerView and update the adapter with the list
        recyclerView.setVisibility(View.VISIBLE);
        parkingAdapter.updateParking(parkingList);  // Pass the list of parking objects
    }



    public void backAction() {
        finish();
    }
}