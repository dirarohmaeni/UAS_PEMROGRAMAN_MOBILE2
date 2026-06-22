package com.example.pengingat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(
            Context context,
            Intent intent
    ) {

        String nama =
                intent.getStringExtra("nama");

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        String channelId =
                "obat_channel";

        // ==========================
        // 🔥 CHANNEL API 26+
        // ==========================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            channelId,
                            "Pengingat Obat",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "Notifikasi pengingat minum obat"
            );

            manager.createNotificationChannel(
                    channel
            );
        }

        // ==========================
        // 🔥 OPEN APP
        // ==========================

        Intent openIntent =
                new Intent(
                        context,
                        MainActivity.class
                );

        openIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        openIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        // ==========================
        // 🔥 NOTIFICATION
        // ==========================

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        channelId
                )
                        .setSmallIcon(
                                R.mipmap.ic_launcher
                        )
                        .setContentTitle(
                                "💊 Waktunya Minum Obat"
                        )
                        .setContentText(
                                nama
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )
                        .setContentIntent(
                                pendingIntent
                        )
                        .setAutoCancel(true);

        // ==========================
        // 🔥 SHOW NOTIF
        // ==========================

        manager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}