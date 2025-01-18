package com.example.ezpark;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameOrEmailField, passwordField;
    private Button loginButton;
    private TextView newUserTab;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDatabaseHelper(this);

        usernameOrEmailField = findViewById(R.id.username_email);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        newUserTab = findViewById(R.id.new_user_tab);

        loginButton.setOnClickListener(v -> {
            String credential = usernameOrEmailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (credential.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.checkUser(credential, password)) {
                Cursor cursor = dbHelper.getUserDetails(credential);
                if (cursor != null && cursor.moveToFirst()) {
                    int usernameIndex = cursor.getColumnIndex("username");

                    if (usernameIndex != -1) {
                        String username = cursor.getString(usernameIndex);

                        // Navigate to ProfileActivity and pass the username
                        Intent intent = new Intent(LoginActivity.this, Homepage.class);
                        intent.putExtra("username", username); // Pass the username
                        startActivity(intent);
                        finish();

                    } else {
                        Log.e("MainActivity", "Column 'username' not found");
                        Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close(); // Always close the cursor
                } else {
                    Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        newUserTab.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }
}