package com.example.ezpark;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReloadActivity extends AppCompatActivity {

    private TextView walletBalanceText;
    private EditText topUpAmountEditText;
    private ImageButton backButton;
    private Button paymentCreditDebitCard, paymentEWallet, paymentOnlineBanking;
    private UserDatabaseHelper dbHelper;
    private double walletBalance = 0.00;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload);

        dbHelper = new UserDatabaseHelper(this);
        username = getIntent().getStringExtra("username");

        backButton = findViewById(R.id.back_button);
        walletBalanceText = findViewById(R.id.wallet_balance);
        topUpAmountEditText = findViewById(R.id.top_up_amount);

        Button amount10 = findViewById(R.id.amount_rm10);
        Button amount30 = findViewById(R.id.amount_rm30);
        Button amount50 = findViewById(R.id.amount_rm50);
        Button amount100 = findViewById(R.id.amount_rm100);
        Button payNowButton = findViewById(R.id.pay_now_button);

        // Initialize payment option buttons
        paymentCreditDebitCard = findViewById(R.id.payment_credit_debit_card);
        paymentEWallet = findViewById(R.id.payment_e_wallet);
        paymentOnlineBanking = findViewById(R.id.payment_online_banking);

        // Fetch initial wallet balance
        walletBalance = dbHelper.getWalletBalance(username);
        updateWalletBalance();

        // Predefined amounts
        View.OnClickListener predefinedAmountListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = ((Button) v).getText().toString().replace("RM", "").trim();
                topUpAmountEditText.setText(amount);
            }
        };

        amount10.setOnClickListener(predefinedAmountListener);
        amount30.setOnClickListener(predefinedAmountListener);
        amount50.setOnClickListener(predefinedAmountListener);
        amount100.setOnClickListener(predefinedAmountListener);

        // Back button listener
        backButton.setOnClickListener(v -> {
            onBackPressed(); // Navigate back to the previous activity
        });

        // Pay Now button
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processTopUp();
            }
        });

        // Payment options listeners
        paymentCreditDebitCard.setOnClickListener(v -> {
            // This will just show a message for now
            Toast.makeText(this, "Visa Credit/Debit Card selected", Toast.LENGTH_SHORT).show();
        });

        paymentEWallet.setOnClickListener(v -> {
            // This will just show a message for now
            Toast.makeText(this, "Touch 'n Go E-Wallet selected", Toast.LENGTH_SHORT).show();
        });

        paymentOnlineBanking.setOnClickListener(v -> {
            // This will just show a message for now
            Toast.makeText(this, "DuitNow Online Banking selected", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateWalletBalance() {
        walletBalanceText.setText(String.format("Current wallet balance: RM%.2f", walletBalance));
    }

    private void processTopUp() {
        String topUpText = topUpAmountEditText.getText().toString();

        if (!topUpText.isEmpty()) {
            try {
                double topUpAmount = Double.parseDouble(topUpText);

                if (topUpAmount > 0) {
                    walletBalance += topUpAmount;
                    dbHelper.updateWallet(username, walletBalance);
                    updateWalletBalance();
                    Toast.makeText(this, "Wallet reloaded successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Enter a valid amount!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter an amount to top up!", Toast.LENGTH_SHORT).show();
        }
    }
}