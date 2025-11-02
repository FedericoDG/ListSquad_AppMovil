package com.federicodg80.listly.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class KeepAliveService extends Service {

    private static final String CHANNEL_ID = "keep_alive_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("List Squad activo")
                .setContentText("Servicio en primer plano activo")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(1, notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Asegura que Android intente reiniciar el servicio si lo mata
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Servicio de notificaciones",
                NotificationManager.IMPORTANCE_MIN
        );
        channel.setDescription("Mantiene List Squad vivo para recibir notificaciones");
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }
}
