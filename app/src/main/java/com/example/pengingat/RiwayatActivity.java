package com.example.pengingat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiwayatActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    RiwayatAdapter adapter;

    DatabaseHelper dbHelper;

    List<String[]> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        recyclerView =
                findViewById(R.id.recyclerRiwayat);

        dbHelper =
                new DatabaseHelper(this);

        loadData();

        // ==========================
        // 🔥 SWIPE HAPUS
        // ==========================

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT |
                                ItemTouchHelper.RIGHT
                ) {

                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {

                        int position =
                                viewHolder.getAdapterPosition();

                        // 🔥 INDEX NAMA = 1
                        String nama =
                                data.get(position)[1];

                        // 🔥 HAPUS DATABASE
                        dbHelper.deleteRiwayat(nama);

                        // 🔥 HAPUS LIST
                        data.remove(position);

                        // 🔥 REFRESH
                        adapter.notifyItemRemoved(position);
                    }
                };

        new ItemTouchHelper(simpleCallback)
                .attachToRecyclerView(recyclerView);
    }

    // ==========================
    // 🔥 LOAD DATA RIWAYAT
    // ==========================

    private void loadData() {

        // 🔥 AMBIL DATA STATUS SELESAI
        data =
                dbHelper.getRiwayat();

        adapter =
                new RiwayatAdapter(
                        this,
                        data
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 🔥 AUTO REFRESH
        loadData();
    }
}