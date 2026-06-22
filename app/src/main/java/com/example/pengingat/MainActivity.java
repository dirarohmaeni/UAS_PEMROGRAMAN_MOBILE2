package com.example.pengingat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // NAVIGATION
    private LinearLayout navHome, navObat, navRiwayat;

    private ImageView iconHome, iconObat, iconRiwayat;

    private TextView txtHome, txtObat, txtRiwayat;
    private TextView tvNama, tvTanggal, tvRekomendasi;

    // FORM
    private EditText etKeluhan;
    private Button btnCari, btnTambah;

    // CONTAINER
    private LinearLayout containerObatHariIni;

    // DATA
    private final List<ObatModel> daftarObat = new ArrayList<>();

    private final List<JadwalObat> hasilAI =
            new ArrayList<>();

    private String hasilRekomendasi = "";

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        dbHelper = new DatabaseHelper(this);

        setActive("home");
        setUserName();
        setTanggal();

        setupNavigation();
        setupDataObat();
        setupCariObat();
        setupTambahObat();
    }

    private void initView() {

        // NAV
        navHome = findViewById(R.id.navHome);
        navObat = findViewById(R.id.navObat);
        navRiwayat = findViewById(R.id.navRiwayat);

        iconHome = findViewById(R.id.iconHome);
        iconObat = findViewById(R.id.iconObat);
        iconRiwayat = findViewById(R.id.iconRiwayat);

        txtHome = findViewById(R.id.txtHome);
        txtObat = findViewById(R.id.txtObat);
        txtRiwayat = findViewById(R.id.txtRiwayat);

        // CONTENT
        tvNama = findViewById(R.id.tvNama);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvRekomendasi = findViewById(R.id.tvRekomendasi);

        etKeluhan = findViewById(R.id.etKeluhan);

        btnCari = findViewById(R.id.btnCari);
        btnTambah = findViewById(R.id.btnTambah);

        containerObatHariIni =
                findViewById(R.id.containerHariIni);
    }

    private void setUserName() {

        String nama =
                getIntent().getStringExtra("NAMA_USER");

        if (nama != null && !nama.isEmpty()) {
            tvNama.setText("Hello " + nama + "!");
        } else {
            tvNama.setText("Selamat Datang!");
        }
    }

    private void setTanggal() {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "EEEE, dd MMMM yyyy",
                        new Locale("id", "ID")
                );

        String tanggal = sdf.format(new Date());

        tvTanggal.setText(tanggal);
    }

    private void setupNavigation() {

        navHome.setOnClickListener(v ->
                setActive("home"));

        navObat.setOnClickListener(v ->
                startActivity(
                        new Intent(this,
                                ObatActivity.class)
                ));

        navRiwayat.setOnClickListener(v ->
                startActivity(
                        new Intent(this,
                                RiwayatActivity.class)
                ));
    }

    private void setupDataObat() {

        daftarObat.add(
                new ObatModel(
                        "Paracetamol",
                        "500mg 3x sehari",
                        "demam"
                )
        );

        daftarObat.add(
                new ObatModel(
                        "OBH Combi",
                        "10ml 3x sehari",
                        "batuk"
                )
        );
    }

    // ==========================
    // AI REKOMENDASI
    // ==========================

    private void setupCariObat() {

        btnCari.setOnClickListener(v -> {

            String keluhan =
                    etKeluhan.getText()
                            .toString()
                            .trim();

            if (keluhan.isEmpty()) {

                Toast.makeText(
                        this,
                        "Masukkan keluhan dulu",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            tvRekomendasi.setText(
                    "AI sedang mencari rekomendasi..."
            );

            cariObatAI(keluhan);
        });
    }
    private void cariObatAI(String keluhan) {

        new Thread(() -> {

            try {

                OkHttpClient client =
                        new OkHttpClient();

                JSONObject json =
                        new JSONObject();

                // MODEL GROQ
                json.put(
                        "model",
                        "llama-3.3-70b-versatile"
                );

                JSONArray messages =
                        new JSONArray();

                JSONObject userMsg =
                        new JSONObject();

                userMsg.put("role", "user");

                userMsg.put(
                        "content",
                        "Kamu adalah AI kesehatan. " +
                                "Berikan rekomendasi obat berdasarkan keluhan user. " +
                                "Format WAJIB:\n" +
                                "NamaObat|Jam|Dosis\n\n" +

                                "Contoh:\n" +
                                "Paracetamol|08:00|1 Tablet\n" +
                                "Vitamin C|12:00|1 Tablet\n" +
                                "OBH Combi|19:00|10 ml\n\n" +

                                "Jangan kasih penjelasan tambahan.\n\n" +

                                "Keluhan: " + keluhan
                );

                messages.put(userMsg);

                json.put("messages", messages);

                RequestBody body =
                        RequestBody.create(
                                json.toString(),
                                MediaType.parse(
                                        "application/json"
                                )
                        );

                Request request =
                        new Request.Builder()
                                .url(
                                        "https://api.groq.com/openai/v1/chat/completions"
                                )
                                .addHeader(
                                        "Authorization",
                                        "Bearer " +
                                                BuildConfig.GROQ_KEY
                                )
                                .addHeader(
                                        "Content-Type",
                                        "application/json"
                                )
                                .post(body)
                                .build();

                Response response =
                        client.newCall(request)
                                .execute();

                String result =
                        response.body().string();

                System.out.println(result);

                JSONObject obj =
                        new JSONObject(result);

                // CEK ERROR RESPONSE
                if (!obj.has("choices")) {

                    runOnUiThread(() -> {

                        tvRekomendasi.setText(
                                "AI gagal memberikan rekomendasi"
                        );

                        Toast.makeText(
                                this,
                                result,
                                Toast.LENGTH_LONG
                        ).show();
                    });

                    return;
                }

                String jawaban =
                        obj.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                runOnUiThread(() -> {

                    hasilAI.clear();

                    String[] lines =
                            jawaban.split("\n");

                    StringBuilder tampil =
                            new StringBuilder();

                    for (String line : lines) {

                        if (line.contains("|")) {

                            String[] part =
                                    line.split("\\|");

                            if (part.length >= 3) {

                                String nama =
                                        part[0].trim();

                                String jam =
                                        part[1].trim();

                                String dosis =
                                        part[2].trim();

                                hasilAI.add(
                                        new JadwalObat(
                                                nama,
                                                jam,
                                                dosis
                                        )
                                );

                                tampil.append("💊 ")
                                        .append(nama)
                                        .append("\n")
                                        .append("🕒 ")
                                        .append(jam)
                                        .append(" - ")
                                        .append(dosis)
                                        .append("\n\n");
                            }
                        }
                    }

                    if (hasilAI.isEmpty()) {

                        tvRekomendasi.setText(
                                "AI tidak memberi format yang sesuai"
                        );

                        return;
                    }

                    hasilRekomendasi =
                            tampil.toString();

                    tvRekomendasi.setText(
                            hasilRekomendasi
                    );
                });

            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(() -> {

                    tvRekomendasi.setText(
                            "Gagal mengambil rekomendasi AI"
                    );

                    Toast.makeText(
                            this,
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
            }

        }).start();
    }
    // ==========================
    // TAMBAH KE JADWAL
    // ==========================

    private void setupTambahObat() {

        btnTambah.setOnClickListener(v -> {

            if (hasilAI.isEmpty()) {

                Toast.makeText(
                        this,
                        "Belum ada rekomendasi",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            for (JadwalObat obat : hasilAI) {

                dbHelper.insertObat(
                        obat.nama,
                        obat.jam,
                        obat.dosis
                );
            }

            Toast.makeText(
                    this,
                    "Jadwal berhasil ditambahkan!",
                    Toast.LENGTH_SHORT
            ).show();

            loadData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    // ==========================
    // LOAD DATA
    // ==========================

    private void loadData() {

        containerObatHariIni.removeAllViews();

        List<String[]> dataHariIni =
                dbHelper.getObatHariIni();

        for (String[] item : dataHariIni) {

            // FORMAT DATA
            // 0 = nama
            // 1 = jam/tanggal
            // 2 = status

            String namaObat =
                    item[0];

            String jam =
                    item[1];

            String status =
                    item[2];

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
                    32,
                    32,
                    32
            );

            card.setBackgroundResource(
                    R.drawable.bg_card2
            );

            card.setClickable(true);

            card.setFocusable(true);

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

            // ==========================
            // 🔥 NAMA
            // ==========================

            TextView tvNamaObat =
                    new TextView(this);

            tvNamaObat.setText(
                    "💊 " + namaObat
            );

            tvNamaObat.setTextSize(18);

            tvNamaObat.setTextColor(
                    getResources().getColor(
                            R.color.text_dark
                    )
            );

            tvNamaObat.setTypeface(
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

            tvJam.setPadding(
                    0,
                    14,
                    0,
                    0
            );

            // ==========================
            // 🔥 DOSIS
            // ==========================

            TextView tvDosis =
                    new TextView(this);

            tvDosis.setText(
                    "💊 1 Tablet"
            );

            tvDosis.setTextSize(13);

            tvDosis.setPadding(
                    0,
                    8,
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
                    18,
                    8,
                    18,
                    8
            );

            if ("selesai".equals(status)) {

                tvStatus.setText(
                        "🟢 Sudah diminum"
                );

                tvStatus.setBackgroundResource(
                        R.drawable.bg_status_done
                );

            } else {

                tvStatus.setText(
                        "🔴 Belum diminum"
                );

                tvStatus.setBackgroundResource(
                        R.drawable.bg_chip2
                );
            }

            // ==========================
            // 🔥 ADD VIEW
            // ==========================

            card.addView(tvNamaObat);

            card.addView(tvJam);

            card.addView(tvDosis);

            card.addView(tvStatus);

            // ==========================
            // 🔥 CLICK DETAIL
            // ==========================

            card.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                DetailObatActivity.class
                        );

                intent.putExtra(
                        "NAMA",
                        namaObat
                );

                intent.putExtra(
                        "DOSIS",
                        "1 Tablet"
                );

                intent.putExtra(
                        "JAM",
                        jam
                );

                intent.putExtra(
                        "DESKRIPSI",
                        "Obat hasil rekomendasi AI"
                );

                intent.putExtra(
                        "STATUS",
                        status
                );

                startActivity(intent);
            });

            containerObatHariIni.addView(card);
        }
    }

    private void setActive(String menu) {

        int aktif =
                ContextCompat.getColor(
                        this,
                        R.color.primary
                );

        int nonaktif =
                ContextCompat.getColor(
                        this,
                        R.color.text_grey
                );

        iconHome.setColorFilter(nonaktif);
        iconObat.setColorFilter(nonaktif);
        iconRiwayat.setColorFilter(nonaktif);

        txtHome.setTextColor(nonaktif);
        txtObat.setTextColor(nonaktif);
        txtRiwayat.setTextColor(nonaktif);

        switch (menu) {

            case "home":

                iconHome.setColorFilter(aktif);

                txtHome.setTextColor(aktif);

                break;

            case "obat":

                iconObat.setColorFilter(aktif);

                txtObat.setTextColor(aktif);

                break;

            case "riwayat":

                iconRiwayat.setColorFilter(aktif);

                txtRiwayat.setTextColor(aktif);

                break;
        }
    }
}