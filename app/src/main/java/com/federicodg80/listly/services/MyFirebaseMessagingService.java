package com.federicodg80.listly.services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.federicodg80.listly.MainActivity;
import com.federicodg80.listly.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String CHANNEL_ID = "urgent_channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;

        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    getPackageName() + ":FCMLock"
            );
            try {
                wakeLock.acquire(10_000);
            } catch (SecurityException e) {
                Log.e(TAG, "No se pudo adquirir WakeLock: " + e.getMessage());
            }
        }

        try {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String body = data.get("body");

            // Si venían en el bloque notification
            if (remoteMessage.getNotification() != null) {
                if (title == null) title = remoteMessage.getNotification().getTitle();
                if (body == null) body = remoteMessage.getNotification().getBody();
            }

            // Mostrar Toast si la app está en primer plano
            if (isAppInForeground()) {
                Intent intent = new Intent("ACTION_SHOW_MOTION_TOAST");
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else {
                sendUrgentNotification(title, body, data);
            }

        } finally {
            // Liberar el WakeLock si se había adquirido (permite a una app pedirle al sistema que mantenga despierta alguna parte del dispositivo)
            if (wakeLock != null && wakeLock.isHeld()) wakeLock.release();
        }
    }

    // Método para detectar si la app está en primer plano
    private boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        final String packageName = getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    // Método para enviar notificaciones urgentes (en segundo plano o cerrada)
    private void sendUrgentNotification(String title, String body, Map<String, String> data) {
        Log.d("NAZGUL", "sendUrgentNotification: " );
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Reaccionar al tipo de notificación
        // String type = data != null ? data.get("type") : null;
        // String clickAction = data != null ? data.get("clickAction") : null;

       /* if (type != null) {
            Log.d("NAZGUL", "type de notificación: " + type);
            switch (type) {
                case "newList":
                    // Agregar datos específicos para nueva lista
                    intent.putExtra("navigate_to", "lists");
                    intent.putExtra("show_new_list", true);
                    break;
                case "taskAssigned":
                    intent.putExtra("navigate_to", "tasks");
                    break;
                case "reminder":
                    intent.putExtra("navigate_to", "notifications");
                    break;
            }
        }*/

        // Manejar clickAction
        /*if (clickAction != null) {
            intent.putExtra("click_action", clickAction);
        }*/

        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Sonido predeterminado y vibración
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrationPattern = {0, 500, 250, 500};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(vibrationPattern)
                .setDefaults(NotificationCompat.DEFAULT_ALL) // sonido, vibración y luces
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        // Solo usar full screen intent si el payload lo requiere explícitamente (ej. "fullscreen": "1")
        if (data != null && "1".equals(data.get("fullscreen"))) {
            builder.setFullScreenIntent(pendingIntent, true);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // minSdk es 30, así que los canales son obligatorios; creamos directamente
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Notificaciones Urgentes",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Notificaciones críticas");
        channel.enableVibration(true);
        channel.setVibrationPattern(vibrationPattern);
        channel.setSound(defaultSoundUri, channel.getAudioAttributes());
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}