package com.example.mytestapp.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.mytestapp.qianggou.AccessibilityManager;


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

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        if (event != null && event.getPackageName() != null && event.getClassName() != null
//                && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            AccessibilityManager.getInstance().onAccessibilityEvent(event);
            Log.d(TAG,  "type = " + event.getEventType() + "event--package = " + event.getPackageName() + " , className =" + event.getClassName());
//        }
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
