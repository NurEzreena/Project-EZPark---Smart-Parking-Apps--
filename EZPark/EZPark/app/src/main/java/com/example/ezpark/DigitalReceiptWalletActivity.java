package com.example.ezpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DigitalReceiptWalletActivity extends AppCompatActivity {


    private Button btnPayment;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_receipt_wallet);

        btnPayment = findViewById(R.id.btn_goPayment);
        backButton = findViewById(R.id.back_button);

        btnPayment.setOnClickListener(v -> {
            Intent intent = new Intent(DigitalReceiptWalletActivity.this, DigitalReceiptPaymentActivity.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(v -> { backAction(); });

    }


    public void backAction(){
        finish();
    }
}
