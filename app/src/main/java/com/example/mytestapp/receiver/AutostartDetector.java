package com.example.mytestapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AutostartDetector extends BroadcastReceiver {

    static final String TAG = "AutostartDetector";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, intent.toString());
        if (Intent.ACTION_TIME_TICK.equals(action)) {
            Log.d(TAG, intent.toString());
        }
    }
}
