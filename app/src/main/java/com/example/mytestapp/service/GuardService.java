package com.example.mytestapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import timber.log.Timber;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-03-29 16:47
 */
public class GuardService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.d("onBind");
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Timber.d("dump");
        super.dump(fd, writer, args);
    }
}
