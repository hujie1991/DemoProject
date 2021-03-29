package com.example.mytestapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.mytestapp.service.CoreModel;

import timber.log.Timber;


public class AutostartDetector extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(TextUtils.isEmpty(action)) {
            return;
        }
        Timber.d(action);
        switch (action) {
            case Intent.ACTION_TIME_TICK:
                Timber.d(intent.toString());
                break;
            case Intent.ACTION_LOCKED_BOOT_COMPLETED:
                Timber.d("开机");
                CoreModel.getInstance().startCoreService();
                CoreModel.getInstance().bindCoreService();
                break;
            case Intent.ACTION_SHUTDOWN:
                Timber.d("关机");
                break;
        }
    }
}
