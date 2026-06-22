package com.example.pengingat;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class EditObatActivity extends AppCompatActivity {

    EditText etNama, etDurasi;
    Button btnSimpan;

    DatabaseHelper dbHelper;
    int idObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_obat);

        etNama = findViewById(R.id.etNama);
        etDurasi = findViewById(R.id.etDurasi);
        btnSimpan = findViewById(R.id.btnSimpan);

        dbHelper = new DatabaseHelper(this);

        idObat = getIntent().getIntExtra("id", -1);

        if (idObat == -1) {
            Toast.makeText(this, "Data tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadData();

        btnSimpan.setOnClickListener(v -> {

            String nama = etNama.getText().toString();
            String tanggal = etDurasi.getText().toString();

            // 🔥 UPDATE YANG SESUAI DATABASE
            dbHelper.updateNamaDanTanggal(idObat, nama, tanggal);

            Toast.makeText(this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadData() {
        String[] data = dbHelper.getObatById(idObat);

        if (data != null) {
            etNama.setText(data[1]);     // nama
            etDurasi.setText(data[2]);   // tanggal
        }
    }
}