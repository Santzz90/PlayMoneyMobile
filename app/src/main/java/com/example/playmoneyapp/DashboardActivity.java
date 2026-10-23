package com.example.playmoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
    private Button btnAddTransaction;
    private TransactionDao transactionDao;

    private RecyclerView recyclerViewTransactions;
    private TransactionAdapter transactionAdapter;

    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    // Variáveis numéricas para garantir atualização imediata do saldo
    private double currentIncome = 0.0;
    private double currentExpense = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTotalBalance = findViewById(R.id.txtTotalBalance);
        txtTotalIncome = findViewById(R.id.txtTotalIncome);
        txtTotalExpense = findViewById(R.id.txtTotalExpense);
        btnAddTransaction = findViewById(R.id.btnAddTransaction);

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
                    Intent intent = new Intent(DashboardActivity.this, AddTransactionActivity.class);
                    intent.putExtra("TRANSACTION_ID", transaction.getId());
                    intent.putExtra("TRANSACTION_DESC", transaction.getDescription());
                    intent.putExtra("TRANSACTION_AMOUNT", transaction.getAmount());
                    intent.putExtra("TRANSACTION_TYPE", transaction.getType());
                    startActivity(intent);

                } else if (which == 1) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        transactionDao.delete(transaction);
                    });
                }
            });
            builder.show();
        });

        loadFinancialData();

        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddTransactionActivity.class);
            startActivity(intent);
        });
    }

    private void loadFinancialData() {
        transactionDao.getTotalIncome().observe(this, income -> {
            currentIncome = income != null ? income : 0.0;
            txtTotalIncome.setText(formatoMoeda.format(currentIncome));
            updateBalance();
        });

        transactionDao.getTotalExpense().observe(this, expense -> {
            currentExpense = expense != null ? expense : 0.0;
            txtTotalExpense.setText(formatoMoeda.format(currentExpense));
            updateBalance();
        });

        transactionDao.getAllTransactions().observe(this, transactions -> {
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