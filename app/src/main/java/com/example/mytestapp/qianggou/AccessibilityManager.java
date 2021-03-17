package com.example.mytestapp.qianggou;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.mytestapp.service.GreenAccessibilityService;
import com.example.mytestapp.utils.AccessibilityLogUtils;
import com.example.mytestapp.utils.NodeUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.widget.Toast.LENGTH_SHORT;

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
            Toast.makeText(GreenAccessibilityService.getInstance().getApplicationContext(), "收到事件", LENGTH_SHORT).show();
        } else if (packageName.equals(YAN_XUAN_PACKAGE)) {
            YanXuanQiangGou.event(className);
            Toast.makeText(GreenAccessibilityService.getInstance().getApplicationContext(), "收到事件", LENGTH_SHORT).show();
        } else if (packageName.equals(TIAM_MAO_PACKAGE)) {
            TianMaoQiangGou.event(className);
            Toast.makeText(GreenAccessibilityService.getInstance().getApplicationContext(), "收到事件", LENGTH_SHORT).show();
        }
        Log.d(TAG, "packageName = " + packageName + " , className = " + className);
    }

    public void startPolling(long dlay) {
        stopPolling();
        subscribe = createTimer(dlay).subscribe(times -> {
            Log.d(TAG, "start");
//            startRun();
            logAllNodeInfo();
        }, error -> {
            Log.d(TAG, "startPolling", error);
            startPolling(5000);
        });
    }


    private void logAllNodeInfo() {
        GreenAccessibilityService instance = GreenAccessibilityService.getInstance();
        if (instance != null) {
            AccessibilityNodeInfo rootInActiveWindow = instance.getRootInActiveWindow();
            if (rootInActiveWindow == null) {
                return;
            }
            AccessibilityLogUtils.dfsnode(rootInActiveWindow, 0);
        }
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
            } else {
//                logId(rootInActiveWindow, "同意并使用", "android.widget.Button");
//                logId(rootInActiveWindow, "同意", "android.widget.Button");
//                logId(rootInActiveWindow, "退出", "android.widget.Button");
//                AccessibilityLogUtils.dfsnode(rootInActiveWindow, 1);


//                List<AccessibilityNodeInfo> byIds = NodeUtil.findByIds(rootInActiveWindow, "com.bbk.launcher2:id/more");
//                List<AccessibilityNodeInfo> byText = NodeUtil.findByIds(rootInActiveWindow, "com.bbk.launcher2:id/label");
//                if (byIds != null && !byIds.isEmpty()) {
//                    byIds.forEach(access -> {
//                        NodeUtil.clickNodeOrParent(access);
//                    });
//                }
            }
        }
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
                .interval(dlay, 5000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }
}
