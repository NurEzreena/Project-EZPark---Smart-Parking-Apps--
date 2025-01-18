package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class StateSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_select);

        // Handle selection for Kedah
        findViewById(R.id.select_state_kedah).setOnClickListener(view -> returnSelectedState("Kedah"));

        // Handle selection for Pulau Pinang
        findViewById(R.id.select_state_pulau_pinang).setOnClickListener(view -> returnSelectedState("Pulau Pinang"));

        // Handle selection for Perak
        findViewById(R.id.select_state_perak).setOnClickListener(view -> returnSelectedState("Perak"));

        // Handle selection for Selangor
        findViewById(R.id.select_state_selangor).setOnClickListener(view -> returnSelectedState("Selangor"));

        // Handle selection for Kuala Lumpur
        findViewById(R.id.select_state_kuala_lumpur).setOnClickListener(view -> returnSelectedState("Kuala Lumpur"));

        // Handle click for Terengganu
        findViewById(R.id.select_state_terengganu).setOnClickListener(view -> returnSelectedState("Terengganu"));

        // Handle click for Kelantan
        findViewById(R.id.select_state_kelantan).setOnClickListener(view -> returnSelectedState("Kelantan"));

        // Handle click for Pahang
        findViewById(R.id.select_state_pahang).setOnClickListener(view -> returnSelectedState("Pahang"));

        // Handle click for Johor Bahru
        findViewById(R.id.select_state_johor_bahru).setOnClickListener(view -> returnSelectedState("Johor Bahru"));

        // Handle click for Negeri Sembilan
        findViewById(R.id.select_state_negeri_sembilan).setOnClickListener(view -> returnSelectedState("Negeri Sembilan"));

        ImageView backState = findViewById(R.id.backState);
        backState.setOnClickListener(view -> {

            Intent intent = new Intent(StateSelectActivity.this, Homepage.class);
            startActivity(intent);
            finish(); // Optionally close the current activity
        });

    }

    private void returnSelectedState(String stateName) {
        // Return the selected state to Homepage
        Intent intent = new Intent();
        intent.putExtra("SELECTED_STATE", stateName); // Add selected state to the Intent
        setResult(RESULT_OK, intent); // Send the result back to the previous activity
        finish(); // Close this activity
    }
}
