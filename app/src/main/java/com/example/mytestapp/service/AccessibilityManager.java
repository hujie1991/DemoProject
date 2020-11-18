package com.example.mytestapp.service;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.utils.NodeUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
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

    private Disposable subscribe;
    private Disposable backDisposable;

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
            Log.d(TAG, "start");
            startRun();
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
                jingDongPolling(rootInActiveWindow);
            } else if (YAN_XUAN_PACKAGE.contentEquals(packageName)) {
                yanxuanPolling(rootInActiveWindow);
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

    private Flowable<Long> createTimer() {
        return Flowable
                .interval(5000, 50, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }

    private void jingDongPolling(AccessibilityNodeInfo rootNodeInfo) {
        AccessibilityNodeInfo qiangGou = NodeUtil.findById(rootNodeInfo, "com.jd.lib.productdetail.feature:id/add_2_car");
        if (qiangGou == null) {
            qiangGou = NodeUtil.findById(rootNodeInfo, "com.jd.lib.productdetail.feature:id/lib_pd_yuyueinfo_status");
        }
        if (qiangGou != null && !"立即抢购".equals(qiangGou.getText().toString())) {
            return;
        }

        //判断按钮状态
        if (qiangGou != null) {
            boolean b = NodeUtil.clickNodeOrParent(qiangGou);
            if (b) {
                Log.d(TAG, "立即抢购");
                return;
            }
        }

        AccessibilityNodeInfo order = NodeUtil.findByText(rootNodeInfo, "提交订单","android.widget.Button");
        if (order != null) {
            NodeUtil.clickNodeOrParent(order);
            Log.d(TAG, "提交了订单");
            delayBack();
        }
    }

    private void yanxuanPolling(AccessibilityNodeInfo rootNodeInfo) {
        AccessibilityNodeInfo qiangGou = NodeUtil.findById(rootNodeInfo, "com.netease.yanxuan:id/moutai_promotion_button");
        if (qiangGou != null && qiangGou.getText().toString().equals("今天10:00开始抢购")) {
            return;
        }

        if (qiangGou != null) {
            boolean b = NodeUtil.clickNodeOrParent(qiangGou);
            if (b) {
                Log.d(TAG, "开始抢购");
                return;
            }
        }

        AccessibilityNodeInfo order = NodeUtil.findById(rootNodeInfo, "com.netease.yanxuan:id/order_btn");
        if (order != null) {
            NodeUtil.clickNodeOrParent(order);
            Log.d(TAG, "提交了订单");
        }
    }

    private void jingDongEvent(String className) {
        if ("com.jingdong.app.mall.WebActivity".equals(className)) {
            GreenAccessibilityService.getInstance().back();
        }
    }

    private void yanXuanEvent(String className) {

    }

    private void delayBack() {
        if (backDisposable != null && !backDisposable.isDisposed()) {
            return;
        }
        backDisposable = Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribe(times -> {
                    GreenAccessibilityService.getInstance().back();
                    Log.d(TAG, "delayBack");
                });
    }


    private void logId(AccessibilityNodeInfo nodeInfo, String text, String viewClassName) {
        AccessibilityNodeInfo byText = NodeUtil.findByText(nodeInfo, text, viewClassName);
        if (byText != null) {
            Log.d(TAG, text + " - ViewIdResourceName = " + byText.getViewIdResourceName());
        }
    }

    private void logText(AccessibilityNodeInfo nodeInfo, String id) {
        AccessibilityNodeInfo idNodeInfo = NodeUtil.findById(nodeInfo, id);
        if (idNodeInfo != null) {
            Log.d(TAG, "text = " + idNodeInfo.getText());
        }
    }

//        logId(rootNodeInfo, "立即抢购", "android.widget.TextView");
//        logId(rootNodeInfo, "已预约", "android.widget.TextView");
//        logId(rootNodeInfo, "提交订单", "android.widget.Button");
}
