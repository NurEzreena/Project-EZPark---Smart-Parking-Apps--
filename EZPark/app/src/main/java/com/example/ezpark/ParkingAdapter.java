package com.example.ezpark;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.Collections;
import java.util.Comparator;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {
    private List<Parking> parkingList;
    private final int userId;  // Store the userId to pass to displayPaymentDetails

    // Constructor now requires userId as an argument
    public ParkingAdapter(List<Parking> parkingList, int userId) {
        this.parkingList = parkingList;
        this.userId = userId;  // Save the userId

        // Sort the parkingList by paymentDate (ascending order)
        sortParkingListByPaymentDate();
    }

    // Method to sort the parking list by payment date in ascending order
    private void sortParkingListByPaymentDate() {
        Collections.sort(parkingList, new Comparator<Parking>() {
            @Override
            public int compare(Parking p1, Parking p2) {
                // Assuming parking.getPaymentDate() returns a date string in a sortable format like "YYYY-MM-DD"
                return p1.getPaymentDate().compareTo(p2.getPaymentDate());
            }
        });
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_item, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        Parking parking = parkingList.get(position);

        Log.d("ParkingAdapter", "State for ParkingId " + parking.getParkingId() + ": " + parking.getState());

        holder.paymentAmountTextView.setText(String.format("RM %.2f", parking.getTotalCost()));
        holder.plateNumberTextView.setText(String.valueOf(parking.getVehicleId()));
        holder.durationTextView.setText(parking.getHour());
        holder.paymentDateTextView.setText(parking.getPaymentDate());
        holder.stateTextView.setText(parking.getState());

        // Set up click listener for itemView
        holder.itemView.setOnClickListener(v -> {
            // Call displayPaymentDetails method when an item is clicked
            parking.displayPaymentDetails(userId);  // Pass the userId to the method
        });
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    static class ParkingViewHolder extends RecyclerView.ViewHolder {
        TextView paymentAmountTextView, plateNumberTextView, durationTextView, paymentDateTextView, stateTextView;

        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentAmountTextView = itemView.findViewById(R.id.PaymentAmountTextView);
            plateNumberTextView = itemView.findViewById(R.id.PlateNumberTextView);
            durationTextView = itemView.findViewById(R.id.DurationTextView);
            paymentDateTextView = itemView.findViewById(R.id.PaymentDateTextView);
            stateTextView = itemView.findViewById(R.id.StateTextView);
        }
    }
}
