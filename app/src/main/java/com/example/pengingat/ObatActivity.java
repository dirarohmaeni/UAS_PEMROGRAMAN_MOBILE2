package com.example.pengingat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ObatActivity extends AppCompatActivity {

    LinearLayout container;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obat);

        container =
                findViewById(R.id.containerObatAktif);

        dbHelper =
                new DatabaseHelper(this);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    // ==========================
    // 🔥 LOAD DATA
    // ==========================

    private void loadData() {

        container.removeAllViews();

        List<String[]> data =
                dbHelper.getObatAktif();

        // ==========================
        // 🔥 KALAU KOSONG
        // ==========================

        if (data.size() == 0) {

            TextView kosong =
                    new TextView(this);

            kosong.setText(
                    "🎉 Tidak ada obat aktif"
            );

            kosong.setTextSize(16);

            kosong.setTextColor(
                    Color.GRAY
            );

            kosong.setPadding(
                    0,
                    40,
                    0,
                    0
            );

            container.addView(kosong);

            return;
        }

        // ==========================
        // 🔥 LOOP DATA
        // ==========================

        for (String[] item : data) {

            // FORMAT:
            // 0 = id
            // 1 = nama
            // 2 = jam/tanggal
            // 3 = status

            int id =
                    Integer.parseInt(item[0]);

            String nama =
                    item[1];

            String jam =
                    item[2];

            String status =
                    item[3];

            // ==========================
            // 🔥 CARD
            // ==========================

            LinearLayout card =
                    new LinearLayout(this);

            card.setOrientation(
                    LinearLayout.VERTICAL
            );

            card.setPadding(
                    32,
                    28,
                    32,
                    28
            );

            card.setBackgroundResource(
                    R.drawable.bg_card2
            );

            card.setElevation(8f);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(
                    0,
                    0,
                    0,
                    24
            );

            card.setLayoutParams(params);

            card.setClickable(true);

            card.setFocusable(true);

            // ==========================
            // 🔥 CLICK DETAIL
            // ==========================

            card.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                ObatActivity.this,
                                DetailObatActivity.class
                        );

                intent.putExtra(
                        "NAMA",
                        nama
                );

                intent.putExtra(
                        "DESKRIPSI",
                        "Obat hasil rekomendasi AI"
                );

                intent.putExtra(
                        "JAM",
                        jam
                );

                intent.putExtra(
                        "DOSIS",
                        "1 Tablet"
                );

                intent.putExtra(
                        "STATUS",
                        status
                );

                startActivity(intent);
            });

            // ==========================
            // 🔥 NAMA
            // ==========================

            TextView tvNama =
                    new TextView(this);

            tvNama.setText(
                    "💊 " + nama
            );

            tvNama.setTextSize(17);

            tvNama.setTextColor(
                    Color.parseColor("#111827")
            );

            tvNama.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );

            // ==========================
            // 🔥 JAM
            // ==========================

            TextView tvJam =
                    new TextView(this);

            tvJam.setText(
                    "🕒 " + jam
            );

            tvJam.setTextSize(13);

            tvJam.setTextColor(
                    Color.parseColor("#6B7280")
            );

            tvJam.setPadding(
                    0,
                    10,
                    0,
                    0
            );

            // ==========================
            // 🔥 STATUS
            // ==========================

            TextView tvStatus =
                    new TextView(this);

            tvStatus.setTextSize(12);

            tvStatus.setPadding(
                    0,
                    18,
                    0,
                    0
            );

            if ("selesai".equals(status)) {

                tvStatus.setText(
                        "🟢 Sudah diminum"
                );

            } else {

                tvStatus.setText(
                        "🔴 Belum diminum"
                );
            }

            // ==========================
            // 🔥 BUTTON
            // ==========================

            Button btnSelesai =
                    new Button(this);

            btnSelesai.setText(
                    "Sudah diminum"
            );

            btnSelesai.setAllCaps(false);

            btnSelesai.setFocusable(false);

            btnSelesai.setBackgroundResource(
                    R.drawable.btn_primary
            );

            btnSelesai.setTextColor(
                    Color.WHITE
            );

            LinearLayout.LayoutParams btnParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            btnParams.setMargins(
                    0,
                    20,
                    0,
                    0
            );

            btnSelesai.setLayoutParams(
                    btnParams
            );

            // ==========================
            // 🔥 ACTION
            // ==========================

            btnSelesai.setOnClickListener(v -> {

                dbHelper.updateStatus(
                        id,
                        "selesai"
                );

                loadData();

                Intent intent =
                        new Intent(
                                ObatActivity.this,
                                RiwayatActivity.class
                        );

                startActivity(intent);
            });

            // ==========================
            // 🔥 ADD VIEW
            // ==========================

            card.addView(tvNama);

            card.addView(tvJam);

            card.addView(tvStatus);

            card.addView(btnSelesai);

            container.addView(card);
        }
    }
}