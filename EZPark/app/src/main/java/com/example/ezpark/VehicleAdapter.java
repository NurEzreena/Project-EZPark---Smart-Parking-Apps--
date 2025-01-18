package com.example.ezpark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> vehicleList;
    private List<Vehicle> filterVehicles;
    private final OnDeleteClickListener onDeleteClickListener;
    private final OnUpdateClickListener onUpdateClickListener;

    // Interfaces to handle delete and update actions
    public interface OnDeleteClickListener {
        void onDeleteClick(int vehicleId);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(Vehicle vehicle);
    }

    public VehicleAdapter(List<Vehicle> vehicleList, OnDeleteClickListener onDeleteClickListener, OnUpdateClickListener onUpdateClickListener) {
        this.vehicleList = vehicleList;
        this.filterVehicles = new ArrayList<>(vehicleList);
        this.onDeleteClickListener = onDeleteClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_item, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.plateNumberTextView.setText(vehicle.getPlateNumber());
        holder.modelNameTextView.setText(vehicle.getModelName());

        // Handling delete and update buttons
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(vehicle.getId()));
        holder.updateButton.setOnClickListener(v -> onUpdateClickListener.onUpdateClick(vehicle));
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public void updateVehicles(List<Vehicle> newVehicles) {
        vehicleList = new ArrayList<>(newVehicles);
        filterVehicles = new ArrayList<>(newVehicles);
        notifyDataSetChanged();
    }

    public void filterVehicles(String query) {
        if (query.isEmpty()) {
            filterVehicles = new ArrayList<>(vehicleList);
        } else {
            List<Vehicle> filterList = new ArrayList<>();
            for (Vehicle vehicle : vehicleList) {
                if (vehicle.getPlateNumber().toLowerCase().contains(query.toLowerCase())) {
                    filterList.add(vehicle);
                }
            }
            filterVehicles = filterList;
        }
        notifyDataSetChanged();
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        public TextView plateNumberTextView;
        public TextView modelNameTextView;
        public ImageButton deleteButton;
        public ImageButton updateButton;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            plateNumberTextView = itemView.findViewById(R.id.plateNumberTextView);
            modelNameTextView = itemView.findViewById(R.id.modelNameTextView);
            deleteButton = itemView.findViewById(R.id.delete_vehicle_1);
            updateButton = itemView.findViewById(R.id.edit_vehicle_1);
        }
    }
}
