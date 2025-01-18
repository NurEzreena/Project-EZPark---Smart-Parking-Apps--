package com.example.ezpark;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewFullName, textViewEmail, textViewPhone, walletBalanceView;

    private ImageButton backButton;
    private EditText editTextFullName, editTextUsername, editTextDob, editTextPassword;
    private Button buttonReload, buttonUpdateProfile;
    private UserDatabaseHelper dbHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        backButton = findViewById(R.id.back_button);
        textViewFullName = findViewById(R.id.display_full_name);
        editTextFullName = findViewById(R.id.full_name);
        textViewEmail = findViewById(R.id.email_address);
        editTextUsername = findViewById(R.id.username); // Ensure this is an EditText in XML
        textViewPhone = findViewById(R.id.phone_number);
        editTextDob = findViewById(R.id.date_of_birth);
        editTextPassword = findViewById(R.id.password);
        walletBalanceView = findViewById(R.id.wallet_balance_display);
        buttonReload = findViewById(R.id.button_reload);
        buttonUpdateProfile = findViewById(R.id.update_profile_button);


        backButton = findViewById(R.id.back_button);
        dbHelper = new UserDatabaseHelper(this);
        username = getIntent().getStringExtra("username");
        Log.d("ProfileActivity", "Received username: " + username);
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User data not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if username is missing
            return;
        }


        loadUserData();

        backButton.setOnClickListener(v -> {
            onBackPressed(); // Navigate back to the previous activity
        });



        // Reload button listener
        buttonReload.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ReloadActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

        });

        // Update profile button listener
        buttonUpdateProfile.setOnClickListener(v -> {
            updateUserProfile();
        });
    }

    private void loadUserData() {
        Cursor cursor = dbHelper.getUserDetails(username);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                    String dob = cursor.getString(cursor.getColumnIndexOrThrow("dob"));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                    double walletBalance = cursor.getDouble(cursor.getColumnIndexOrThrow("wallet_balance"));

                    // Update UI
                    textViewFullName.setText(fullName);
                    editTextFullName.setText(fullName);
                    textViewEmail.setText(email);
                    editTextUsername.setText(username);
                    textViewPhone.setText(phone);
                    editTextDob.setText(dob);
                    editTextPassword.setText(password);
                    walletBalanceView.setText(String.format(Locale.getDefault(), "Wallet Balance: RM%.2f", walletBalance));
                } else {
                    Log.e("ProfileActivity", "Cursor is empty");
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("ProfileActivity", "Error retrieving user data", e);
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
        } else {
            Log.e("ProfileActivity", "Cursor is null");
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUserProfile() {
        // Retrieve updated values from EditText fields
        String updatedFullName = editTextFullName.getText().toString().trim();
        String updatedUsername = editTextUsername.getText().toString().trim();
        String updatedDob = editTextDob.getText().toString().trim();
        String updatedPassword = editTextPassword.getText().toString().trim();

        if (updatedFullName.isEmpty() || updatedUsername.isEmpty() || updatedDob.isEmpty() || updatedPassword.isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the user data in the database
        boolean isUpdated = dbHelper.updateUser(updatedUsername, updatedFullName, updatedDob, updatedPassword);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            username = updatedUsername; // Update the username for consistency
            loadUserData(); // Refresh the UI with updated data
        } else {
            Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }
}
