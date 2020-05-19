package com.example.mytestapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mytestapp.R;

public class NotifyModel {
    public static final String GUARD_NOTIFY_CHANNEL_ID = "core";
    public static final int GUARD_NOTIFY_ID = 10002;
    public static final String DAEMON_NOTIFY_CHANNEL_ID = "daemon";
    public static final int DAEMON_NOTIFY_ID = 10001;

    public static int getGuardNotifyId = 1;

    public static void bindNotify(Context context, String title, String msg) {
        bindNotify(context, GUARD_NOTIFY_CHANNEL_ID, getGuardNotifyId++, title, msg, "myName");
    }

    public static void bindNotify(Context service, String channelId, int notifyId, String title, String msg, String channelName) {
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext(), channelId);
        builder.setContentTitle(title)
                .setContentText(msg + notifyId)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        manager.notify(notifyId, builder.build());
//        service.startForeground(notifyId, builder.build());
    }

    public static void updateNotify(Context context, String channelId, int notifyId, String title, String msg) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "guard", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("guard");
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId);
        builder.setContentTitle(title)
                .setContentText(msg)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(false);
        manager.notify(notifyId, builder.build());
    }

    public static void cancelNotify(Service service, int notifyId) {
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notifyId);
    }
}
