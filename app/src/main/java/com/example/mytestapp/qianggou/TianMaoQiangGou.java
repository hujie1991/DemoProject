package com.example.mytestapp.qianggou;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.utils.NodeUtil;

public class TianMaoQiangGou extends BaseQiangGou {


//    AccessibilityNodeInfo qiangGou = NodeUtil.findById(rootNodeInfo, "com.tmall.wireless:id/button_cart_charge");
//    //判断按钮状态
//        if (qiangGou != null) {
//        boolean b = NodeUtil.clickNodeOrParent(qiangGou);
//        if (b) {
//            Log.d(TAG, "结算");
//            return;
//        }
//    }

    public static void qiangGou(AccessibilityNodeInfo rootNodeInfo) {

        AccessibilityNodeInfo goumai = NodeUtil.findByText(rootNodeInfo, "立即购买", "android.widget.TextView");
        if (goumai != null) {
            boolean b = NodeUtil.clickNodeOrParent(goumai);
            if (b) {
                Log.d(TAG, "立即购买");
                return;
            }
        }

        AccessibilityNodeInfo confirm = NodeUtil.findById(rootNodeInfo, "com.tmall.wireless:id/confirm");
        if (confirm != null) {
            boolean b = NodeUtil.clickNodeOrParent(confirm);
            if (b) {
                Log.d(TAG, "确定");
                return;
            }
        }

        AccessibilityNodeInfo order = NodeUtil.findByText(rootNodeInfo, "提交订单", "android.widget.TextView");
        if (order != null) {
            NodeUtil.clickNodeOrParent(order);
            Log.d(TAG, "提交了订单");
        }
    }

    public static void event(String className) {

    }
}
