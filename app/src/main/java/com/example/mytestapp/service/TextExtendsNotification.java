package com.example.mytestapp.service;

import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-09-16 15:32
 */
public class TextExtendsNotification extends NotificationMonitorService {

    String TAG = "TextExtendsNotification";

    public TextExtendsNotification() {
        super();
        Log.d(TAG, "init()");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, "onNotificationPosted isClearable = " + sbn.isClearable() + " , package = " + sbn.getPackageName());
    }
}
