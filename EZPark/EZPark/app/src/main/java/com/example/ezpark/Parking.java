package com.example.ezpark;

import android.database.Cursor;


public class Parking {
    private int parkingId;
    private int vehicleId;
    private double totalCost;
    private String hour;
    private String state;
    private String paymentDate;

    // Constructor that accepts individual parameters
    public Parking(int parkingId, int vehicleId, double totalCost, String hour, String state, String paymentDate) {
        this.parkingId = parkingId;
        this.vehicleId = vehicleId;
        this.totalCost = totalCost;
        this.hour = hour;
        this.state = state;
        this.paymentDate = paymentDate;
    }

    // Constructor that accepts a Cursor (for database retrieval)
    public Parking(Cursor cursor) {
        this.parkingId = cursor.getInt(cursor.getColumnIndexOrThrow(ParkingDatabaseHelper.COLUMN_ID));
        this.vehicleId = cursor.getInt(cursor.getColumnIndexOrThrow(ParkingDatabaseHelper.COLUMN_VEHICLE_ID));
        this.totalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(ParkingDatabaseHelper.COLUMN_TOTAL_COST));
        this.hour = cursor.getString(cursor.getColumnIndexOrThrow(ParkingDatabaseHelper.COLUMN_HOUR));
        this.paymentDate = cursor.getString(cursor.getColumnIndexOrThrow(ParkingDatabaseHelper.COLUMN_PAYMENT_DATE));
        this.state = cursor.getString(cursor.getColumnIndexOrThrow(ParkingDatabaseHelper.COLUMN_STATE));
    }

    // Getter methods for the attributes
    public int getParkingId() {
        return parkingId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getHour() {
        return hour;
    }

    public String getState() {
        return state;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    // ToString Method for Debugging
    @Override
    public String toString() {
        return "Parking{" +
                "parkingId=" + parkingId +
                ", vehicleId=" + vehicleId +
                ", totalCost=" + totalCost +
                ", hour='" + hour + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
