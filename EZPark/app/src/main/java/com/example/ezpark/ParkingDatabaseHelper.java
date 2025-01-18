package com.example.ezpark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ParkingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "parking_db";
    private static final int DATABASE_VERSION = 4; // Incremented version to accommodate 'userId' and 'state'

    // Table and Column Names
    public static final String TABLE_PARKING = "parking_table";
    private static final String TABLE_VEHICLE = "vehicle_table";
    private static final String TABLE_USERS = "users"; // Direct reference to 'users' table

    public static final String COLUMN_ID = "parkingId";
    public static final String COLUMN_VEHICLE_ID = "vehicleId";
    public static final String COLUMN_TOTAL_COST = "totalCost";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_PAYMENT_DATE = "paymentDate";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_USER_ID = "userId"; // Add userId column

    public ParkingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PARKING_TABLE = "CREATE TABLE " + TABLE_PARKING + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VEHICLE_ID + " INTEGER, " +
                COLUMN_TOTAL_COST + " FLOAT, " +
                COLUMN_HOUR + " TEXT, " +
                COLUMN_PAYMENT_DATE + " TEXT, " +
                COLUMN_STATE + " TEXT, " +
                COLUMN_USER_ID + " INTEGER, " +
                // Directly referencing the 'users' table and the 'id' column
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE, " +
                // Directly referencing the 'vehicle_table' and 'id' column
                "FOREIGN KEY (" + COLUMN_VEHICLE_ID + ") REFERENCES " + TABLE_VEHICLE + "(id) ON DELETE CASCADE" +
                ")";
        db.execSQL(CREATE_PARKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
            onCreate(db);
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_PARKING + " ADD COLUMN " + COLUMN_STATE + " TEXT");
        }
    }

    // Add a new parking record for a user
    public boolean addParking(int vehicleId, double totalCost, String hour, String paymentDate, String state, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_ID, vehicleId);
        values.put(COLUMN_TOTAL_COST, totalCost);
        values.put(COLUMN_HOUR, hour);
        values.put(COLUMN_PAYMENT_DATE, paymentDate);
        values.put(COLUMN_STATE, state);
        values.put(COLUMN_USER_ID, userId); // Add userId to the record

        long result = db.insert(TABLE_PARKING, null, values);
        db.close();
        return result != -1;
    }

    // Get all parking records for a specific user
    public List<Parking> getParkingsByUserId(int userId) {
        List<Parking> parkingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PARKING + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int parkingId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int vehicleId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VEHICLE_ID));
                double totalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_COST));
                String hour = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOUR));
                String paymentDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE));
                String state = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE)); // Check state
                int userIdColumn = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));

                Log.d("ParkingDatabase", "ParkingId: " + parkingId + ", State: " + state); // Debug state value

                parkingList.add(new Parking(parkingId, vehicleId, totalCost, hour, state, paymentDate, userIdColumn));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return parkingList;
    }




}
