package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
        backsetting.setOnClickListener(view -> {

            Intent intent = new Intent(AppSettingsActivity.this, Homepage.class);
            startActivity(intent);
            finish(); // Optionally close the current activity
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
        // Confirm account deletion
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Handle account deletion logic
                    new AlertDialog.Builder(this)
                            .setMessage("Your account has been deleted.")
                            .setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss())
                            .show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void confirmLogout() {
        // Confirm logout
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", (dialog, which) -> {
                    // Navigate to welcome screen or login page
                    Intent intent = new Intent(this, MainActivity.class); // Replace with your WelcomeActivity
                    startActivity(intent);
                    finish(); // Close current activity
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
