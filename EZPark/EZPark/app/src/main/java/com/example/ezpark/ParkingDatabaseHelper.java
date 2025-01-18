package com.example.ezpark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ParkingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "parking_db";
    private static final int DATABASE_VERSION = 3; // Incremented version to accommodate 'state' column

    // Table and Column Names
    private static final String TABLE_PARKING = "parking_table";

    public static final String COLUMN_ID = "parkingId";
    public static final String COLUMN_VEHICLE_ID = "vehicleId";
    public static final String COLUMN_TOTAL_COST = "totalCost";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_PAYMENT_DATE = "paymentDate";
    public static final String COLUMN_STATE = "state";

    public ParkingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Parking Table with the 'state' column
        String CREATE_PARKING_TABLE = "CREATE TABLE " + TABLE_PARKING + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VEHICLE_ID + " INTEGER, " +
                COLUMN_TOTAL_COST + " FLOAT, " +
                COLUMN_HOUR + " TEXT, " +
                COLUMN_PAYMENT_DATE + " TEXT, " +
                COLUMN_STATE + " TEXT" + // Adding state column
                ")";
        db.execSQL(CREATE_PARKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Check if version is upgraded
        if (oldVersion < 3) { // Updated to version 3 to handle 'state' column
            // Drop the old table and recreate
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
            onCreate(db);
        }
    }

    public Boolean addParking(int vehicleId, double totalCost, String hour, String paymentDate, String state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_ID, vehicleId);
        values.put(COLUMN_TOTAL_COST, totalCost);
        values.put(COLUMN_HOUR, hour);
        values.put(COLUMN_PAYMENT_DATE, paymentDate);
        values.put(COLUMN_STATE, state); // Inserting state value

        long result = db.insert(TABLE_PARKING, null, values);
        db.close();
        return result != -1;
    }

    public List<Parking> getAllParkings() {
        List<Parking> parkingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PARKING + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int parkingId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int vehicleId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VEHICLE_ID));
                double totalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_COST));
                String hour = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOUR));
                String paymentDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE));
                String state = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE)); // Retrieving state

                parkingList.add(new Parking(parkingId, vehicleId, totalCost, hour, state, paymentDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return parkingList;
    }

    public List<Parking> getPaymentDateAndCost() {
        List<Parking> parkingDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch only the required columns (paymentDate, totalCost, and state)
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PAYMENT_DATE + ", " + COLUMN_TOTAL_COST + ", " + COLUMN_STATE + " FROM " + TABLE_PARKING + " ORDER BY " + COLUMN_ID + " DESC", null);

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            do {
                // Extract the paymentDate, totalCost, and state values
                String paymentDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE));
                double totalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_COST));
                String state = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE)); // Extracting state

                // Create a new Parking object using the required data
                Parking parking = new Parking(0, 0, totalCost, "", state, paymentDate);

                // Add the parking object to the list
                parkingDataList.add(parking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return parkingDataList;
    }

    // Method to get the last inserted parking ID
    public long getLastInsertedParkingId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_PARKING, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getLong(0);  // Return the last inserted ID
        }
        return -1;
    }

    // Method to get a parking session by ID
    public Parking getParkingById(long parkingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARKING, null, COLUMN_ID + " = ?", new String[]{String.valueOf(parkingId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return new Parking(cursor);  // Assuming you have a constructor that takes a Cursor
        }
        return null;
    }
}
