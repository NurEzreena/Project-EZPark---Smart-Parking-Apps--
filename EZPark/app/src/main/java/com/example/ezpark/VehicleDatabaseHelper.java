package com.example.ezpark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class VehicleDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehicle_db";
    private static final int DATABASE_VERSION = 2; // Updated version to add userId
    private static final String TABLE_VEHICLE = "vehicle_table";
    private static final String TABLE_USERS = "users"; // Users table (explicitly defined)

    // Columns for vehicle table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLATE_NUMBER = "plate_number";
    private static final String COLUMN_MODEL_NAME = "model_name";
    private static final String COLUMN_USER_ID = "user_id"; // New column for userId

    public VehicleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create vehicle table with userId column
        String CREATE_VEHICLE_TABLE = "CREATE TABLE " + TABLE_VEHICLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLATE_NUMBER + " TEXT, "
                + COLUMN_MODEL_NAME + " TEXT, "
                + COLUMN_USER_ID + " INTEGER, "  // Add userId column
                + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE" + // Foreign key reference to `users` table's `id` column
                ")";
        db.execSQL(CREATE_VEHICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_VEHICLE + " ADD COLUMN isDeleted INTEGER DEFAULT 0");
        }
    }


    // Insert a new vehicle with userId
    public boolean addVehicle(String plateNumber, String modelName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLATE_NUMBER, plateNumber.toUpperCase()); // Ensure uppercase
        values.put(COLUMN_MODEL_NAME, modelName);
        values.put(COLUMN_USER_ID, userId); // Add userId to the vehicle record

        long result = db.insert(TABLE_VEHICLE, null, values);
        db.close();
        return result != -1;
    }

    // Get all vehicles for a specific user
    public List<Vehicle> getVehiclesByUserId(int userId) {
        List<Vehicle> vehicleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VEHICLE + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_ID + " DESC", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String plateNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATE_NUMBER));
                String modelName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL_NAME));
                vehicleList.add(new Vehicle(id, plateNumber, modelName, userId)); // Include userId in the vehicle object
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vehicleList;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vehicle_table WHERE isDeleted = 0", null); // Ensure isDeleted = 0

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String plateNumber = cursor.getString(cursor.getColumnIndexOrThrow("plateNumber"));
                String modelName = cursor.getString(cursor.getColumnIndexOrThrow("modelName"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

                vehicleList.add(new Vehicle(id, plateNumber, modelName, userId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return vehicleList;
    }


    // Update a vehicle with userId
    public boolean updateVehicle(int id, String plateNumber, String modelName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PLATE_NUMBER, plateNumber.toUpperCase()); // Ensure uppercase
        values.put(COLUMN_MODEL_NAME, modelName);
        values.put(COLUMN_USER_ID, userId); // Include userId in the update

        int result = db.update(TABLE_VEHICLE, values, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Update the deleteVehicle method to return a boolean
    public boolean deleteVehicle(int vehicleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isDeleted", 1); // Mark the vehicle as deleted

        int rowsUpdated = db.update("vehicle_table", values, "id = ?", new String[]{String.valueOf(vehicleId)});
        db.close();

        return rowsUpdated > 0; // Return true if at least one row was updated
    }



}
