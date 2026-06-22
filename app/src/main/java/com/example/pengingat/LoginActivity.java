package com.example.pengingat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> login());
    }

    private void login() {

        String nama = etNama.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // DATA DUMMY
        String dummyNama = "Satria";
        String dummyEmail = "satria@gmail.com";
        String dummyPassword = "123456";

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Semua field harus diisi",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (nama.equals(dummyNama)
                && email.equals(dummyEmail)
                && password.equals(dummyPassword)) {

            Toast.makeText(
                    this,
                    "Login Berhasil",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent =
                    new Intent(LoginActivity.this,
                            MainActivity.class);

            intent.putExtra("nama", nama);

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Nama, Email atau Password salah",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}