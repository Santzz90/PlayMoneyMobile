
package com.example.playmoneyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.playmoneyapp.data.AppDatabase;
import com.example.playmoneyapp.data.dao.TransactionDao;
import com.example.playmoneyapp.data.entity.TransactionEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etDescription, etAmount;
    private RadioButton rbIncome;
    private Button btnSaveTransaction, btnCancel;

    private long transactionId = -1;
    private boolean isEditMode = false;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        userId = getIntent().getLongExtra("USER_ID", -1);

        if (userId <= 0) {
            Toast.makeText(this, "Usuário não identificado!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        rbIncome = findViewById(R.id.rbIncome);
        btnSaveTransaction = findViewById(R.id.btnSaveTransaction);
        btnCancel = findViewById(R.id.btnCancel);

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

        // Ação do botão Salvar ou Atualizar
        btnSaveTransaction.setOnClickListener(v -> {

            String description = etDescription.getText().toString();
            String amountStr = etAmount.getText().toString().trim();

            // Verifica campos vazios
            if (description.trim().isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this,
                        "Preencha todos os campos!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Descrição não pode começar com espaço
            if (Character.isWhitespace(description.charAt(0))) {
                Toast.makeText(this,
                        "A descrição não pode começar com espaço!",
                        Toast.LENGTH_SHORT).show();
                etDescription.requestFocus();
                return;
            }

            // Descrição aceita apenas letras e espaços
            if (!description.matches("[\\p{L} ]+")) {
                Toast.makeText(this,
                        "A descrição deve conter apenas letras e espaços!",
                        Toast.LENGTH_SHORT).show();
                etDescription.requestFocus();
                return;
            }

            description = description.trim();

            // Aceita vírgula ou ponto como separador decimal
            amountStr = amountStr.replace(",", ".");

            // Permite números inteiros ou decimais positivos
            if (!amountStr.matches("[0-9]+(\\.[0-9]+)?")) {
                Toast.makeText(this,
                        "Informe um valor numérico positivo válido!",
                        Toast.LENGTH_SHORT).show();
                etAmount.requestFocus();
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this,
                        "Insira um valor numérico válido!",
                        Toast.LENGTH_SHORT).show();
                etAmount.requestFocus();
                return;
            }

            // Impede zero, negativos e valores infinitos
            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                Toast.makeText(this,
                        "O valor deve ser positivo e maior que zero!",
                        Toast.LENGTH_SHORT).show();
                etAmount.requestFocus();
                return;
            }

            String type = rbIncome.isChecked() ? "RECEITA" : "DESPESA";

            // Evita cliques repetidos durante a gravação
            btnSaveTransaction.setEnabled(false);

            final String finalDescription = description;

            Executors.newSingleThreadExecutor().execute(() -> {

                try {
                    TransactionDao dao = AppDatabase
                            .getDatabase(getApplicationContext())
                            .transactionDao();

                    if (isEditMode) {

                        TransactionEntity transaction =
                                dao.getTransactionByIdAndUser(transactionId, userId);

                        if (transaction == null) {
                            runOnUiThread(() -> {
                                Toast.makeText(this,
                                        "Transação não encontrada!",
                                        Toast.LENGTH_SHORT).show();
                                btnSaveTransaction.setEnabled(true);
                            });
                            return;
                        }

                        transaction.setDescription(finalDescription);
                        transaction.setAmount(amount);
                        transaction.setType(type);

                        dao.update(transaction);

                    } else {

                        // Obtém a data atual
                        String dataAtual = new SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault()
                        ).format(new Date());

                        TransactionEntity transaction = new TransactionEntity(
                                userId,
                                1L,
                                amount,
                                finalDescription,
                                type,
                                "VARIAVEL",
                                dataAtual,
                                true
                        );

                        dao.insert(transaction);
                    }

                    runOnUiThread(() -> {
                        String msg = isEditMode
                                ? "Transação atualizada com sucesso!"
                                : "Transação guardada com sucesso!";

                        Toast.makeText(this, msg,
                                Toast.LENGTH_SHORT).show();

                        finish();
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this,
                                "Erro ao salvar transação!",
                                Toast.LENGTH_SHORT).show();

                        btnSaveTransaction.setEnabled(true);
                    });
                }
            });
        });

        // Botão Cancelar
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> {
                finish();
            });
        }
    }
}
