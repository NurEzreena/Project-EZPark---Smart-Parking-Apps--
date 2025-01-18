package com.example.ezpark;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {

    private List<Parking> parkingList;
    private String selectedState; // Store the selected state
    private long parkingId;  // Declare the parkingId variable

    // In ParkingAdapter constructor:
    public ParkingAdapter(List<Parking> parkingList, String selectedState, long parkingId) {
        this.parkingList = parkingList;
        this.selectedState = selectedState;
        this.parkingId = parkingId;  // Use parkingId as long
    }

    @Override
    public ParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parking_item, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParkingViewHolder holder, int position) {
        Parking parking = parkingList.get(position);
        holder.PaymentDateTextView.setText(parking.getPaymentDate());
        holder.PaymentAmountTextView.setText(String.valueOf(parking.getTotalCost()));

    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public void updateParking(List<Parking> newParkingList) {
        this.parkingList.clear();
        this.parkingList.addAll(newParkingList);
        notifyDataSetChanged();  // Notify adapter that the data has changed
    }


    public static class ParkingViewHolder extends RecyclerView.ViewHolder {
        public TextView PaymentDateTextView;
        public TextView PaymentAmountTextView;

        public ParkingViewHolder(View itemView) {
            super(itemView);
            PaymentDateTextView = itemView.findViewById(R.id.PaymentDateTextView);
            PaymentAmountTextView = itemView.findViewById(R.id.PaymentAmountTextView);
        }
    }
}
