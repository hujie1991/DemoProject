package com.example.mytestapp.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.utils.AccessibilityLogUtils;
import com.example.mytestapp.utils.NodeUtil;

import java.util.List;


/**
 * @author zhuq
 * Email: zhuq@txtws.com
 * Date : 2019-06-28 10:07
 */
public class GreenAccessibilityService extends AccessibilityService {

    String TAG = "GreenAccessibilityService";

    private static GreenAccessibilityService instance;

    public static GreenAccessibilityService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    Handler mHandler = new Handler();

    final Runnable runnable = () -> {
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        Log.d(TAG, "rootInActiveWindow = " + rootInActiveWindow);
        if (rootInActiveWindow == null) {
            mHandler.postDelayed((Runnable) this, 50);
        } else {
            AccessibilityLogUtils.dfsnode(rootInActiveWindow, 0);
            List<AccessibilityNodeInfo> noTip = NodeUtil.findByText(rootInActiveWindow, "不再显示", "android.widget.CheckBox");
            if (noTip != null && !noTip.isEmpty()) {
                NodeUtil.clickNodeOrParent(noTip.get(0));
            }
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event != null) {
            Log.d(TAG, "event.getType = " + event.getEventTime() + ", event = " + event);
            CharSequence packageName = event.getPackageName();
            CharSequence className = event.getClassName();
            if (!TextUtils.isEmpty(packageName) && packageName.toString().equals("com.android.systemui")
                && !TextUtils.isEmpty(className) && className.toString().equals("com.android.systemui.media.MediaProjectionPermissionActivity")) {
                Log.d(TAG, "onAccessibilityEvent equals");
                mHandler.postDelayed(runnable, 50);
            }
        }
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onInterrupt() {
        instance = null;
        Log.d(TAG, "onInterrupt()");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected()");
        instance = this;
    }

    public void back() {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    public void home() {
        performGlobalAction(GLOBAL_ACTION_HOME);
    }

    public void toggleRecents() {
        performGlobalAction(GLOBAL_ACTION_RECENTS);
    }

    public void screenshot() {
        performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT);
    }
}
