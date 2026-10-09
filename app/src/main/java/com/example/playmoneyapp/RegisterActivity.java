package com.example.playmoneyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.playmoneyapp.data.AppDatabase;
import com.example.playmoneyapp.data.entity.UserEntity;
import java.util.Random;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegName, etRegEmail, etRegPassword;
    private Button btnRegister, btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> {
            String name = etRegName.getText().toString().trim();
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gera o código único no padrão exigido pelo projeto: PM + 6 dígitos (RF01)
            String userCode = "PM" + String.format("%06d", new Random().nextInt(999999));

            // Perfil padrão inicial definido como USER (RF03)
            String profile = "USER";

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

                // Verifica se o e-mail já existe
                UserEntity existingUser = db.userDao().findByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Cria e insere o novo utilizador no banco de dados local SQLite
                UserEntity newUser = new UserEntity(userCode, name, email, password, profile);
                db.userDao().insertUser(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Conta criada com sucesso! Código: " + userCode, Toast.LENGTH_LONG).show();
                    finish(); // Fecha a tela de cadastro e retorna ao login
                });
            });
        });

        btnBackToLogin.setOnClickListener(v -> finish());
    }
}