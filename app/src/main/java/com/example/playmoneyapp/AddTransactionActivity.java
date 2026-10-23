package com.example.playmoneyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.playmoneyapp.data.AppDatabase;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import java.util.concurrent.Executors;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etDescription, etAmount;
    private RadioButton rbIncome;
    private Button btnSaveTransaction;

    private long transactionId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        rbIncome = findViewById(R.id.rbIncome);
        btnSaveTransaction = findViewById(R.id.btnSaveTransaction);

        // Verifica se recebeu dados para edição
        if (getIntent().hasExtra("TRANSACTION_ID")) {
            transactionId = getIntent().getLongExtra("TRANSACTION_ID", -1);
            String desc = getIntent().getStringExtra("TRANSACTION_DESC");
            double amount = getIntent().getDoubleExtra("TRANSACTION_AMOUNT", 0.0);
            String type = getIntent().getStringExtra("TRANSACTION_TYPE");

            etDescription.setText(desc);
            etAmount.setText(String.valueOf(amount));
            rbIncome.setChecked("RECEITA".equals(type));

            isEditMode = true;
            btnSaveTransaction.setText("Atualizar Transação");
        }

        btnSaveTransaction.setOnClickListener(v -> {
            String description = etDescription.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (description.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Insira um valor válido!", Toast.LENGTH_SHORT).show();
                return;
            }

            String type = rbIncome.isChecked() ? "RECEITA" : "DESPESA";

            TransactionEntity transaction = new TransactionEntity(
                    1L,
                    1L,
                    amount,
                    description,
                    type,
                    "VARIAVEL",
                    "2026-10-31",
                    true
            );

            if (isEditMode) {
                transaction.setId(transactionId);
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                if (isEditMode) {
                    AppDatabase.getDatabase(getApplicationContext()).transactionDao().update(transaction);
                } else {
                    AppDatabase.getDatabase(getApplicationContext()).transactionDao().insert(transaction);
                }

                runOnUiThread(() -> {
                    String msg = isEditMode ? "Transação atualizada com sucesso!" : "Transação guardada com sucesso!";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}