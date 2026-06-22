package com.example.pengingat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    Context context;

    List<String[]> list;

    public RiwayatAdapter(Context context, List<String[]> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_riwayat,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        // 🔥 FORMAT BARU
        // 0 = id
        // 1 = nama
        // 2 = tanggal
        // 3 = status

        String nama =
                list.get(position)[1];

        String tanggal =
                list.get(position)[2];

        holder.text1.setText(
                "💊 " + nama
        );

        holder.text2.setText(
                "✔ Sudah diminum - " + tanggal
        );
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView text1, text2;

        public ViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            text1 =
                    itemView.findViewById(R.id.text1);

            text2 =
                    itemView.findViewById(R.id.text2);
        }
    }
}