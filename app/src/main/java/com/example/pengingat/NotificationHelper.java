package com.example.pengingat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static void showNotification(
            Context context,
            String namaObat
    ) {

        String channelId =
                "obat_channel";

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

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

            manager.createNotificationChannel(
                    channel
            );
        }

        // ==========================
        // 🔥 OPEN APP
        // ==========================

        Intent intent =
                new Intent(
                        context,
                        MainActivity.class
                );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        // ==========================
        // 🔥 BUILD NOTIF
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
                                namaObat
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )
                        .setContentIntent(
                                pendingIntent
                        )
                        .setAutoCancel(true);

        manager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}