package com.example.ezpark;

import android.content.ContentValues;
import android.util.Log;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 4; // Incremented for schema update
    // Match or exceed the previous version// Current database version


    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_WALLET_BALANCE = "wallet_balance";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "full_name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "dob TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "wallet_balance REAL DEFAULT 10.0)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }



    public boolean insertUser(String username, String fullName, String email, String phone, String dob, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("full_name", fullName);
        values.put("email", email);
        values.put("phone", phone);
        values.put("dob", dob);
        values.put("password", password);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }


    public boolean checkUser(String credential, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                "(" + COLUMN_EMAIL + "=? OR " + COLUMN_USERNAME + "=?) AND " + COLUMN_PASSWORD + "=?",
                new String[]{credential, credential, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public Cursor getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
    }



    public boolean updateUser(String username, String fullName, String dob, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("dob", dob);
        values.put("password", password); // Update password as String

        int rowsUpdated = db.update("users", values, "username = ?", new String[]{username});
        db.close();
        return rowsUpdated > 0; // Return true if rows are updated
    }



    public void updateWallet(String username, double walletBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("wallet_balance", walletBalance);

        int rowsUpdated = db.update("users", values, "username = ?", new String[]{username});
        Log.d("DatabaseHelper", "Rows updated for wallet: " + rowsUpdated);
        db.close();
    }

    public double getWalletBalance(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"wallet_balance"},
                "username = ?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("wallet_balance"));
            cursor.close();
            return balance;
        }

        return 0.00; // Default if user not found
    }



}