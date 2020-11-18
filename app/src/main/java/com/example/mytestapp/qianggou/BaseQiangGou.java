package com.example.mytestapp.qianggou;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.utils.NodeUtil;

public class BaseQiangGou {

   static final String TAG = "AccessibilityManager";

//        logId(rootNodeInfo, "立即抢购", "android.widget.TextView");
//        logId(rootNodeInfo, "已预约", "android.widget.TextView");
//        logId(rootNodeInfo, "提交订单", "android.widget.Button");

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
}
