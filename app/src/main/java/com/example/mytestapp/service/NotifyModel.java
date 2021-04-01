package com.example.mytestapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mytestapp.R;

/**
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2019-06-19 19:30
 */
public class NotifyModel {

    public static final String GUARD_NOTIFY_CHANNEL_ID = "core";
    public static final int GUARD_NOTIFY_ID = 10002;

    public static void bindNotify(Service service, String channelId, int notifyId, String title, String msg, String channelName) {
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext(), channelId);
        builder.setContentTitle(title)
                .setContentText(msg)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(null)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(false);
        service.startForeground(notifyId, builder.build());
    }

    public static Notification createNotification(Service service, String channelId, int notifyId, String title, String msg, String channelName) {
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext(), channelId);
        builder.setContentTitle(title)
                .setContentText(msg)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(false);
        return builder.build();
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
