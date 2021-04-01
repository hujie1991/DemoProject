package com.example.mytestapp.crash;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Process;


import androidx.annotation.NonNull;

import com.example.mytestapp.MyApplication;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2020-04-21 18:24
 */
public class DiskCrashHandler implements Thread.UncaughtExceptionHandler{

    public static void init() {
        DiskCrashHandler crashHandler = new DiskCrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    private static void restoreCrash(@SuppressWarnings("unused") Thread thread, Throwable ex) {
        Timber.d("restoreCrash");
        Timber.e(ex);
        Schedulers.io().scheduleDirect(DiskCrashHandler::killProcess, 1, TimeUnit.SECONDS);
    }

    private static void killProcess() {
        Timber.d("killProcess()");
        Process.killProcess(Process.myPid());
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Timber.d("uncaughtException");
        saveCrash(t, e);
        restoreCrash(t, e);
    }


    private void saveCrash(@SuppressWarnings("unused") Thread thread, Throwable ex) {
        ex.printStackTrace(System.err);
        // 收集异常信息，写入到sd卡
        File dir = new File(MyApplication.getContext().getExternalFilesDir(null) + File.separator + "crash");

        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                Timber.e("CrashHandler create dir fail");
                return;
            }
        }

        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            String name = dateFormat.format(new Date(System.currentTimeMillis())) + ".log";
            File fileName = new File(dir, name);
            if (!fileName.exists()) {
                @SuppressWarnings("unused")
                boolean newFile = fileName.createNewFile();
            }

            PrintStream err = new PrintStream(fileName);

            err.println("--------------------------------AppInfo--------------------------------");
//            err.println("AndroidVersion: " + AppUtils.getAppVersionName());
            err.println();
            err.println("--------------------------------SystemInfo:--------------------------------");
            err.println("Product: " + android.os.Build.PRODUCT);
            err.println("CPU_ABI: " + android.os.Build.CPU_ABI);
            err.println("TAGS: " + android.os.Build.TAGS);
            err.println("VERSION_CODES.BASE:" + android.os.Build.VERSION_CODES.BASE);
            err.println("MODEL: " + android.os.Build.MODEL);
            err.println("SDK: " + Build.VERSION.SDK_INT);
            err.println("VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE);
            err.println("DEVICE: " + android.os.Build.DEVICE);
            err.println("DISPLAY: " + android.os.Build.DISPLAY);
            err.println("BRAND: " + android.os.Build.BRAND);
            err.println("BOARD: " + android.os.Build.BOARD);
            err.println("FINGERPRINT: " + android.os.Build.FINGERPRINT);
            err.println("ID: " + android.os.Build.ID);
            err.println("MANUFACTURER: " + android.os.Build.MANUFACTURER);
            err.println("USER: " + android.os.Build.USER);
            err.println();
            err.println();
            err.println("--------------------------------CrashContent--------------------------------");
            ex.printStackTrace(err);
            err.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
