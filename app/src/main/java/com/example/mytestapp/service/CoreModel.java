package com.example.mytestapp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.example.mytestapp.MyApplication;
import com.example.mytestapp.utils.ProcessUtil;

import timber.log.Timber;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-03-29 17:02
 */
public class CoreModel {

    private Context mContext;

    private CoreModel() {
        mContext = MyApplication.getContext();
    }

    public static CoreModel getInstance() {
        return CoreModelHolder.INSTANCE;
    }

    private static class CoreModelHolder {
        private static final CoreModel INSTANCE = new CoreModel();
    }

    public void startCoreService() {
        Timber.d("startCoreService()");
        if (ProcessUtil.isAppMainProcess(mContext)) {
            Timber.d("main thread");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(new Intent(mContext, GuardService.class));
            } else {
                mContext.startService(new Intent(mContext, GuardService.class));
            }
        }
    }

    public void bindCoreService() {
        if (ProcessUtil.isAppMainProcess(mContext)) {
            mContext.bindService(new Intent(mContext, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Timber.d("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Timber.d("onServiceDisconnected");
            bindCoreService();
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Timber.d("onBindingDied");
            onServiceDisconnected(name);
        }
    };

}
