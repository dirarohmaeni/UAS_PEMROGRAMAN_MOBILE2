package com.example.pengingat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailObatActivity extends AppCompatActivity {

    // TEXT VIEW
    TextView tvNama, tvDeskripsi, tvDurasi, tvDosis, tvStatus;

    // BUTTON
    TextView btnSudah, btnEdit, btnHapus;

    DatabaseHelper dbHelper;

    String namaObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_obat);

        // INIT VIEW
        tvNama = findViewById(R.id.tvNama);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvDurasi = findViewById(R.id.tvDurasi);
        tvDosis = findViewById(R.id.tvDosis);
        tvStatus = findViewById(R.id.tvStatus);

        btnSudah = findViewById(R.id.btnSudah);
        btnEdit = findViewById(R.id.btn_edit);
        btnHapus = findViewById(R.id.btn_hapus);

        dbHelper = new DatabaseHelper(this);

        // 🔥 AMBIL DATA DARI INTENT
        namaObat =
                getIntent().getStringExtra("NAMA");

        String dosis =
                getIntent().getStringExtra("DOSIS");

        String jam =
                getIntent().getStringExtra("JAM");

        String deskripsi =
                getIntent().getStringExtra("DESKRIPSI");

        String status =
                getIntent().getStringExtra("STATUS");

        // VALIDASI
        if (namaObat == null) {

            Toast.makeText(
                    this,
                    "Data tidak ditemukan",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // SET DATA
        tvNama.setText(namaObat);

        tvDeskripsi.setText(
                deskripsi != null
                        ? deskripsi
                        : "Obat hasil rekomendasi AI"
        );

        tvDurasi.setText(
                "Jam Minum: " + jam
        );

        tvDosis.setText(
                dosis != null
                        ? dosis
                        : "1 Tablet"
        );

        // STATUS
        if ("Sudah diminum".equals(status)) {

            tvStatus.setText(
                    "🟢 Sudah diminum"
            );

            tvStatus.setBackgroundResource(
                    R.drawable.bg_status_done
            );

            btnSudah.setEnabled(false);

            btnSudah.setAlpha(0.5f);

        } else {

            tvStatus.setText(
                    "🔴 Belum diminum"
            );

            tvStatus.setBackgroundResource(
                    R.drawable.bg_status_pending
            );

            btnSudah.setEnabled(true);

            btnSudah.setAlpha(1f);
        }

        // ==========================
        // TANDAI SUDAH MINUM
        // ==========================

        btnSudah.setOnClickListener(v -> {

            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() ->
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                    );

            // UPDATE STATUS DATABASE
            dbHelper.updateStatusByNama(
                    namaObat,
                    "selesai"
            );

            // UPDATE UI
            tvStatus.setText(
                    "🟢 Sudah diminum"
            );

            tvStatus.setBackgroundResource(
                    R.drawable.bg_status_done
            );

            btnSudah.setEnabled(false);

            btnSudah.setAlpha(0.5f);

            Toast.makeText(
                    this,
                    "Obat masuk ke riwayat 👍",
                    Toast.LENGTH_SHORT
            ).show();

            // PINDAH KE HALAMAN RIWAYAT
            Intent intent =
                    new Intent(
                            DetailObatActivity.this,
                            RiwayatActivity.class
                    );

            startActivity(intent);

            finish();
        });

        // ==========================
        // HAPUS
        // ==========================

        btnHapus.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Hapus Obat")
                    .setMessage(
                            "Yakin mau hapus obat ini?"
                    )
                    .setPositiveButton(
                            "Ya",
                            (dialog, which) -> {

                                dbHelper.deleteObatByNama(
                                        namaObat
                                );

                                Toast.makeText(
                                        this,
                                        "Data dihapus",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();
                            }
                    )
                    .setNegativeButton(
                            "Batal",
                            null
                    )
                    .show();
        });

        // ==========================
        // EDIT
        // ==========================

        btnEdit.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DetailObatActivity.this,
                            EditObatActivity.class
                    );

            intent.putExtra(
                    "nama",
                    namaObat
            );

            startActivity(intent);
        });
    }
}