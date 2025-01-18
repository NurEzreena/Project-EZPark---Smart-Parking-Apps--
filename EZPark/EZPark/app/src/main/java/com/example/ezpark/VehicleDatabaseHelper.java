package com.example.ezpark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.SQLException;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class VehicleDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehicle_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_VEHICLE = "vehicle_table";

    // Columns for vehicle table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLATE_NUMBER = "plate_number";
    private static final String COLUMN_MODEL_NAME = "model_name";

    public VehicleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VEHICLE_TABLE = "CREATE TABLE " + TABLE_VEHICLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLATE_NUMBER + " TEXT, "
                + COLUMN_MODEL_NAME + " TEXT" + ")";
        db.execSQL(CREATE_VEHICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        onCreate(db);
    }

    // Insert a new vehicle
    public Boolean addVehicle(String plateNumber, String modelName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLATE_NUMBER, plateNumber.toUpperCase()); // Ensure uppercase
        values.put(COLUMN_MODEL_NAME, modelName);

        long result = db.insert(TABLE_VEHICLE, null, values);
        db.close();
        return result != -1;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VEHICLE + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String plateNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATE_NUMBER));
                String modelName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL_NAME));
                vehicleList.add(new Vehicle(id, plateNumber, modelName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vehicleList;
    }

    public boolean updateVehicles(int id, String plateNumber, String modelName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PLATE_NUMBER, plateNumber.toUpperCase()); // Ensure uppercase
        values.put(COLUMN_MODEL_NAME, modelName);

        int result = db.update(TABLE_VEHICLE, values, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean deleteVehicles(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_VEHICLE, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
}
