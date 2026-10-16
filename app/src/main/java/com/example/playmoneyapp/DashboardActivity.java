package com.example.playmoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.playmoneyapp.data.AppDatabase;
import com.example.playmoneyapp.data.dao.TransactionDao;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtTotalBalance, txtTotalIncome, txtTotalExpense;
    private Button btnAddTransaction;
    private TransactionDao transactionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Tratamento de margens gerado pelo sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos elementos visuais
        txtTotalBalance = findViewById(R.id.txtTotalBalance);
        txtTotalIncome = findViewById(R.id.txtTotalIncome);
        txtTotalExpense = findViewById(R.id.txtTotalExpense);
        btnAddTransaction = findViewById(R.id.btnAddTransaction);

        // Obter a instância do DAO através do AppDatabase
        transactionDao = AppDatabase.getInstance(this).transactionDao();

        loadFinancialData();

        // Ação para abrir a tela de cadastro de transações
        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddTransactionActivity.class);
            startActivity(intent);
        });
    }

    private void loadFinancialData() {
        // Observa as receitas em tempo real
        transactionDao.getTotalIncome().observe(this, income -> {
            double totalIncome = income != null ? income : 0.0;
            txtTotalIncome.setText(String.format("R$ %.2f", totalIncome));
            updateBalance();
        });

        // Observa as despesas em tempo real
        transactionDao.getTotalExpense().observe(this, expense -> {
            double totalExpense = expense != null ? expense : 0.0;
            txtTotalExpense.setText(String.format("R$ %.2f", totalExpense));
            updateBalance();
        });
    }

    private void updateBalance() {
        try {
            String incomeStr = txtTotalIncome.getText().toString().replace("R$ ", "").replace(",", ".");
            String expenseStr = txtTotalExpense.getText().toString().replace("R$ ", "").replace(",", ".");

            double income = incomeStr.isEmpty() ? 0.0 : Double.parseDouble(incomeStr);
            double expense = expenseStr.isEmpty() ? 0.0 : Double.parseDouble(expenseStr);
            double balance = income - expense;

            txtTotalBalance.setText(String.format("R$ %.2f", balance));
        } catch (NumberFormatException e) {
            txtTotalBalance.setText("R$ 0,00");
        }
    }
}