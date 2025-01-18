package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameField, fullNameField, emailField, phoneField, dobField, passwordField, confirmPasswordField;
    private Button signUpButton;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = findViewById(R.id.username);
        fullNameField = findViewById(R.id.full_name);
        emailField = findViewById(R.id.email_address);
        phoneField = findViewById(R.id.phone_number);
        dobField = findViewById(R.id.date_of_birth);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.password_confirmation);
        signUpButton = findViewById(R.id.sign_up_button);

        dbHelper = new UserDatabaseHelper(this);

        signUpButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String fullName = fullNameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();
            String dob = dobField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = dbHelper.insertUser(username, fullName, email, phone, dob, password);
                if (isInserted) {
                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
