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

    private EditText etName, etEmail, etPassword;
    private Button btnRegister, btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            int randomNum = 100000 + new Random().nextInt(900000);
            String userCode = "PM" + randomNum;
            String profile = "USER";

            UserEntity newUser = new UserEntity(userCode, name, email, password, profile);

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getDatabase(getApplicationContext()).userDao().insertUser(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });

        btnBackToLogin.setOnClickListener(v -> {
            finish();
        });
    }
}