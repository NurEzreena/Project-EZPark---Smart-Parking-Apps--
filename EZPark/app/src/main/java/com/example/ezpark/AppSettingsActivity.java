package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AppSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Find Views
        TextView languageOption = findViewById(R.id.LanguageTextView);
        TextView helpCenterOption = findViewById(R.id.HelpCenterTextView);
        TextView termsConditionsOption = findViewById(R.id.TermConditionTextView);
        TextView deleteAccountOption = findViewById(R.id.deleteAccountTextView);
        TextView logoutOption = findViewById(R.id.logoutTextView);
        ImageView backsetting = findViewById(R.id.backSetting);

        // Set Click Listeners to Show Alert Messages
        languageOption.setOnClickListener(view -> showLanguageOptions());
        helpCenterOption.setOnClickListener(view -> showHelpCenterInfo());
        termsConditionsOption.setOnClickListener(view -> showTermsAndConditions());
        deleteAccountOption.setOnClickListener(view -> confirmDeleteAccount());
        logoutOption.setOnClickListener(view -> confirmLogout());

        // In AppSettingsActivity.java
        String username = getIntent().getStringExtra("username");
        int userId = getIntent().getIntExtra("userId", -1);  // -1 is the default value if no userId is passed


        backsetting.setOnClickListener(v -> {
            onBackPressed(); // Navigate back to the previous activity
        });
    }

    private void showLanguageOptions() {
        // Show a list of languages
        String[] languages = {"Malay", "English", "Chinese"};
        new AlertDialog.Builder(this)
                .setTitle("Choose Language")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage = languages[which];
                    new AlertDialog.Builder(this)
                            .setMessage("You selected " + selectedLanguage)
                            .setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss())
                            .show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showHelpCenterInfo() {
        // Show details about contacting the help center
        new AlertDialog.Builder(this)
                .setTitle("Help Center")
                .setMessage("For assistance, please contact us at:\n\nEmail: support@ezpark.com\nPhone: +60142424095")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showTermsAndConditions() {
        // Show terms and conditions
        new AlertDialog.Builder(this)
                .setTitle("Terms & Conditions")
                .setMessage("Here are the Terms & Conditions of using the app:\n\n1. You agree to follow the rules.\n2. Respect other users.\n3. Don't misuse the app.\n\nFor full terms, visit our website.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void confirmDeleteAccount() {
        // Get the username from the intent
        String username = getIntent().getStringExtra("username");

        // Ensure username is not null or empty
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Invalid user data", Toast.LENGTH_SHORT).show();
            return; // Exit if username is invalid
        }

        // Get the user ID using the UserDatabaseHelper
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        int userId = dbHelper.getUserIdByUsername(username);

        // Ensure that userId is valid before attempting to delete
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return; // Exit if userId is invalid
        }

        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Attempt to delete the user from the database
                    boolean isDeleted = dbHelper.deleteUserById(userId);

                    if (isDeleted) {
                        // If the deletion was successful, show success message and navigate away
                        new AlertDialog.Builder(this)
                                .setMessage("Your account has been deleted.")
                                .setPositiveButton("OK", (dialog1, which1) -> {
                                    // Navigate to MainActivity (login screen) after deletion
                                    Intent intent = new Intent(AppSettingsActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity
                                })
                                .show();
                    } else {
                        // If the deletion failed, show an error message
                        new AlertDialog.Builder(this)
                                .setMessage("An error occurred while deleting your account. Please try again later.")
                                .setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss())
                                .show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void confirmLogout() {
        // Confirm logout with a dialog
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", (dialog, which) -> {
                    // Navigate to the login screen (MainActivity)
                    Intent intent = new Intent(this, MainActivity.class); // This will navigate to MainActivity
                    startActivity(intent);
                    finish(); // Close the current activity (AppSettingsActivity)
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()) // Dismiss dialog if user cancels
                .show();
    }


}
