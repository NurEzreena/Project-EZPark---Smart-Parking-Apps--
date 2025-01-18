package com.example.ezpark;

public class Vehicle {
    private int id;          // Primary key, unique ID for the vehicle
    private int userId;      // Foreign key, ID of the user who owns the vehicle
    private String plateNumber; // Vehicle plate number
    private String modelName;   // Vehicle model name

    // Constructor
    public Vehicle(int id, String plateNumber, String modelName, int userId) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.modelName = modelName;
        this.userId = userId;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return plateNumber; // Only return the plate number for the spinner
    }
}
