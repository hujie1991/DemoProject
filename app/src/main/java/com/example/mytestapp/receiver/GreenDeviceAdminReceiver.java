package com.example.mytestapp.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;


/**
 * @author zhuq
 * Email: zhuq@txtws.com
 * Date : 2019-06-28 10:32
 */
public class GreenDeviceAdminReceiver extends DeviceAdminReceiver {

    final String TAG = "GreenDeviceAdminReceiver";

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d(TAG,"onEnabled");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.d(TAG,"onDisabled");
        super.onDisabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Log.d(TAG,"onDisableRequested");
        return super.onDisableRequested(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
        Log.d(TAG,"onPasswordSucceeded");
    }

    @Override
    public void onPasswordExpiring(Context context, Intent intent, UserHandle user) {
        super.onPasswordExpiring(context, intent, user);
        Log.d(TAG,"onPasswordExpiring");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent, UserHandle user) {
        super.onPasswordFailed(context, intent, user);
        Log.d(TAG,"onPasswordFailed");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent, UserHandle user) {
        super.onPasswordSucceeded(context, intent, user);
        Log.d(TAG,"onPasswordSucceeded");
    }
}
