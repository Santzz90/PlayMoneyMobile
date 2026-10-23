package com.example.playmoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.playmoneyapp.data.AppDatabase;
import com.example.playmoneyapp.data.entity.UserEntity;

import java.util.Random;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Ação do Botão Entrar (RF02 - Autenticação)
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Executa a consulta no banco de dados em segundo plano (Room requirement)
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                UserEntity user = db.userDao().findByEmail(email);

                runOnUiThread(() -> {
                    if (user != null && user.getPassword().equals(password)) {
                        Toast.makeText(this, "Login bem-sucedido! Bem-vindo, " + user.getName(), Toast.LENGTH_LONG).show();

                        // Transição automática para o Dashboard
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish(); // Fecha a tela de login para o utilizador não voltar a ela

                    } else {
                        Toast.makeText(this, "E-mail ou senha inválidos!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        // Botão para navegar para a tela de Cadastro
        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}


