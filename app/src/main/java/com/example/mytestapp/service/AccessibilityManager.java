package com.example.mytestapp.service;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.utils.NodeUtil;

import java.util.List;
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

    Disposable subscribe;

    private AccessibilityManager() {}

    private final static AccessibilityManager mInstance = new AccessibilityManager();

    public static AccessibilityManager getInstance() {
        return mInstance;
    }


    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        if (packageName.equals(JONG_DONG_PACKAGE)) {
            jingDongEvent(className);
        } else if (packageName.equals(YAN_XUAN_PACKAGE)) {
            yanXuanEvent(className);
        }
        Log.d(TAG, "packageName = " + packageName + " , className = " + className);
    }

    public void startPolling() {
        stopPolling();
        subscribe = createTimer().subscribe(times -> {
            GreenAccessibilityService instance = GreenAccessibilityService.getInstance();
            if (instance != null && instance.getRootInActiveWindow() != null) {
                AccessibilityNodeInfo rootInActiveWindow = instance.getRootInActiveWindow();
                if (rootInActiveWindow.getPackageName().equals(JONG_DONG_PACKAGE)) {
                    jingDongPolling(rootInActiveWindow);
                } else if (rootInActiveWindow.getPackageName().equals(YAN_XUAN_PACKAGE)) {
                    yanxuanPolling(rootInActiveWindow);
                }
            }
        });
    }

    public void stopPolling() {
        dispose();
    }

    private void dispose() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }

    private Flowable<Long> createTimer() {
        return Flowable
                .interval(2000, 2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    private void jingDongPolling(AccessibilityNodeInfo rootNodeInfo) {
        AccessibilityNodeInfo nodeInfo = NodeUtil.findById(rootNodeInfo, "com.jd.lib.productdetail.feature:id/add_2_car");
        if (nodeInfo != null) {
            //判断按钮状态
            Log.d(TAG, "nodeInfo = " + nodeInfo.getText());
        } else {
            nodeInfo = NodeUtil.findById(rootNodeInfo, "com.jd.lib.settlement.feature:id/a0o");
            if (nodeInfo != null) {
                //提交订单
                Log.d(TAG, "nodeInfo = " + nodeInfo.getText());
            }
        }
    }

    private void yanxuanPolling(AccessibilityNodeInfo rootNodeInfo) {
        AccessibilityNodeInfo nodeInfo = NodeUtil.findById(rootNodeInfo, "com.jd.lib.productdetail.feature:id/add_2_car");
    }

    private void jingDongEvent(String className) {

    }

    private void yanXuanEvent(String className) {

    }
}
