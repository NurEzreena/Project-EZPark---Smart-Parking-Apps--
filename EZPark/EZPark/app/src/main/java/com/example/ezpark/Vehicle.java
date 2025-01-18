package com.example.ezpark;

public class Vehicle {
    private int vehicleId;
    private String plateNumber;
    private String modelName;

    public Vehicle(int id, String plateNumber, String modelName) {
        this.vehicleId = id;
        this.plateNumber = plateNumber;
        this.modelName = modelName;
    }

    // Getters
    public int getId() {
        return vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getModelName() {
        return modelName;
    }

    @Override
    public String toString() {
        return plateNumber; // Spinner displays only the plate number
    }


    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        if (vehicleId != vehicle.vehicleId) return false;
        if (!plateNumber.equals(vehicle.plateNumber)) return false;
        return modelName.equals(vehicle.modelName);
    }

    @Override
    public int hashCode(){
        int result = vehicleId;
        result = 31 * result + plateNumber.hashCode();
        result = 31 * result + modelName.hashCode();
        return result;
    }
}
