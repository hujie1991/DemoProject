package com.example.mytestapp.service;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.qianggou.JingDongQiangGou;
import com.example.mytestapp.qianggou.TianMaoQiangGou;
import com.example.mytestapp.qianggou.YanXuanQiangGou;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-11-17 14:40
 */
public class AccessibilityManager {

    private final String TAG = "AccessibilityManager";
    private final String JONG_DONG_PACKAGE = "com.jingdong.app.mall";
    private final String YAN_XUAN_PACKAGE = "com.netease.yanxuan";
    private final String TIAM_MAO_PACKAGE = "com.tmall.wireless";

    private Disposable subscribe;

    private AccessibilityManager() {
    }

    private final static AccessibilityManager mInstance = new AccessibilityManager();

    public static AccessibilityManager getInstance() {
        return mInstance;
    }


    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        if (packageName.equals(JONG_DONG_PACKAGE)) {
            JingDongQiangGou.event(className);
        } else if (packageName.equals(YAN_XUAN_PACKAGE)) {
            YanXuanQiangGou.event(className);
        } else if (packageName.equals(TIAM_MAO_PACKAGE)) {
            TianMaoQiangGou.event(className);
        }
        Log.d(TAG, "packageName = " + packageName + " , className = " + className);
    }

    public void startPolling(long dlay) {
        stopPolling();
        subscribe = createTimer(dlay).subscribe(times -> {
            Log.d(TAG, "start");
            startRun();
        }, error -> {
            Log.d(TAG, "startPolling", error);
            startPolling(50);
        });
    }

    public void startRun() {
        GreenAccessibilityService instance = GreenAccessibilityService.getInstance();
        if (instance != null) {
            AccessibilityNodeInfo rootInActiveWindow = instance.getRootInActiveWindow();
            if (rootInActiveWindow == null) {
                return;
            }
            CharSequence packageName = rootInActiveWindow.getPackageName();
            if (JONG_DONG_PACKAGE.contentEquals(packageName)) {
                JingDongQiangGou.qiangGou(rootInActiveWindow);
            } else if (YAN_XUAN_PACKAGE.contentEquals(packageName)) {
                YanXuanQiangGou.qiangGou(rootInActiveWindow);
            } else if (TIAM_MAO_PACKAGE.contentEquals(packageName)) {
                TianMaoQiangGou.qiangGou(rootInActiveWindow);
            }
        }
    }

    public void stopPolling() {
        dispose();
    }

    private void dispose() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }

    private Flowable<Long> createTimer(long dlay) {
        return Flowable
                .interval(dlay, 50, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }
}
