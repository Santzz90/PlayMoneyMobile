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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        rbIncome = findViewById(R.id.rbIncome);
        btnSaveTransaction = findViewById(R.id.btnSaveTransaction);

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

            // Criar a entidade com os dados da transação
            TransactionEntity transaction = new TransactionEntity(
                    1L, // userId padrão
                    1L, // categoryId padrão
                    amount,
                    description,
                    type,
                    "VARIAVEL",
                    "2026-10-31",
                    true
            );

            // Gravar na base de dados Room usando thread em segundo plano
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getDatabase(getApplicationContext()).transactionDao().insertTransaction(transaction);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Transação guardada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela e retorna ao Dashboard
                });
            });
        });
    }
}