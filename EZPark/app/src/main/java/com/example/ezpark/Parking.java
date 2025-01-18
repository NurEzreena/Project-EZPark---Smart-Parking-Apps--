package com.example.ezpark;

public class Parking {
    private int parkingId;       // Primary key, unique ID for the parking session
    private int vehicleId;       // Foreign key, ID of the vehicle associated with this session
    private double totalCost;    // Total cost for the parking session
    private String hour;         // Duration of the parking session (e.g., "1 Hour")
    private String state;        // State where the parking occurred
    private String paymentDate;  // Date of the parking session/payment
    private int userId;          // Foreign key, ID of the user who created the session

    // Constructor
    public Parking(int parkingId, int vehicleId, double totalCost, String hour, String state, String paymentDate, int userId) {
        this.parkingId = parkingId;
        this.vehicleId = vehicleId;
        this.totalCost = totalCost;
        this.hour = hour;
        this.state = state;
        this.paymentDate = paymentDate;
        this.userId = userId;
    }

    // Getter and Setter methods
    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void displayPaymentDetails(int userId) {
        // Check if the provided userId matches the current object's userId
        if (this.userId == userId) {
            System.out.println("Vehicle ID: " + this.vehicleId);
            System.out.println("Total Cost: $" + this.totalCost);
            System.out.println("Parking Duration: " + this.hour);
            System.out.println("State: " + this.state);
            System.out.println("Payment Date: " + this.paymentDate);
        } else {
            System.out.println("User ID does not match the payment details.");
        }
    }


    // toString method for debugging and logging
    @Override
    public String toString() {
        return "Parking{" +
                "parkingId=" + parkingId +
                ", vehicleId=" + vehicleId +
                ", totalCost=" + totalCost +
                ", hour='" + hour + '\'' +
                ", state='" + state + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", userId=" + userId +
                '}';
    }
}
