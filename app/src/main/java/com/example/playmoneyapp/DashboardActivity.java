
package com.example.playmoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playmoneyapp.data.AppDatabase;
import com.example.playmoneyapp.data.dao.TransactionDao;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtTotalBalance, txtTotalIncome, txtTotalExpense;
    private Button btnAddTransaction, btnLogout;
    private TransactionDao transactionDao;

    private RecyclerView recyclerViewTransactions;
    private TransactionAdapter transactionAdapter;

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    // Variáveis numéricas para garantir atualização imediata do saldo
    private double currentIncome = 0.0;
    private double currentExpense = 0.0;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userId = getIntent().getLongExtra("USER_ID", -1);

        if (userId <= 0) {
            Toast.makeText(this, "Usuário não identificado!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTotalBalance = findViewById(R.id.txtTotalBalance);
        txtTotalIncome = findViewById(R.id.txtTotalIncome);
        txtTotalExpense = findViewById(R.id.txtTotalExpense);
        btnAddTransaction = findViewById(R.id.btnAddTransaction);

        // Botão Sair
        btnLogout = findViewById(R.id.btnLogout);

        recyclerViewTransactions = findViewById(R.id.recyclerViewTransactions);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(this));
        transactionAdapter = new TransactionAdapter();
        recyclerViewTransactions.setAdapter(transactionAdapter);

        transactionDao = AppDatabase.getDatabase(this).transactionDao();

        transactionAdapter.setOnItemClickListener(transaction -> {
            CharSequence[] opcoes = {"Editar", "Excluir", "Cancelar"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Gerir Transação");

            builder.setItems(opcoes, (dialog, which) -> {
                if (which == 0) {

                    Intent intent = new Intent(
                            DashboardActivity.this,
                            AddTransactionActivity.class
                    );

                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("TRANSACTION_ID", transaction.getId());
                    intent.putExtra("TRANSACTION_DESC", transaction.getDescription());
                    intent.putExtra("TRANSACTION_AMOUNT", transaction.getAmount());
                    intent.putExtra("TRANSACTION_TYPE", transaction.getType());

                    startActivity(intent);

                } else if (which == 1) {

                    new AlertDialog.Builder(DashboardActivity.this)
                            .setTitle("Excluir Transação")
                            .setMessage("Deseja realmente excluir esta transação?")
                            .setPositiveButton("Excluir", (confirmDialog, confirmWhich) -> {

                                Executors.newSingleThreadExecutor().execute(() -> {

                                    int excluidos =
                                            transactionDao.deleteTransactionByIdAndUser(
                                                    transaction.getId(), userId
                                            );

                                    runOnUiThread(() -> {

                                        if (excluidos > 0) {
                                            Toast.makeText(this,
                                                    "Transação excluída com sucesso!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this,
                                                    "Transação não encontrada!",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    });
                                });
                            })
                            .setNegativeButton("Cancelar", (confirmDialog, confirmWhich) -> {
                                confirmDialog.dismiss();
                            })
                            .show();
                }
            });

            builder.show();
        });

        loadFinancialData();

        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    AddTransactionActivity.class
            );
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Ação do botão Sair
        btnLogout.setOnClickListener(v -> {

            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Sair do PlayMoney")
                    .setMessage("Deseja realmente sair da sua conta?")
                    .setPositiveButton("Sair", (dialog, which) -> {

                        Intent intent = new Intent(
                                DashboardActivity.this,
                                LoginActivity.class
                        );

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    private void loadFinancialData() {

        transactionDao.getTotalIncomeByUser(userId).observe(this, income -> {
            currentIncome = income != null ? income : 0.0;
            txtTotalIncome.setText(formatoMoeda.format(currentIncome));
            updateBalance();
        });

        transactionDao.getTotalExpenseByUser(userId).observe(this, expense -> {
            currentExpense = expense != null ? expense : 0.0;
            txtTotalExpense.setText(formatoMoeda.format(currentExpense));
            updateBalance();
        });

        transactionDao.getAllTransactionsByUser(userId).observe(this, transactions -> {
            if (transactions != null) {
                transactionAdapter.setTransactions(transactions);
            }
        });
    }

    private void updateBalance() {
        double balance = currentIncome - currentExpense;
        txtTotalBalance.setText(formatoMoeda.format(balance));
    }
}
