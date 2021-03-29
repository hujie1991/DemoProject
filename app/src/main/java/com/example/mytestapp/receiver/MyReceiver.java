package com.example.mytestapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-03-19 13:50
 */
public class MyReceiver extends BroadcastReceiver {

    private final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent = " + intent.getAction());
    }
}
