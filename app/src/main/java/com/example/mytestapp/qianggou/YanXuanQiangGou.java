package com.example.mytestapp.qianggou;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.utils.NodeUtil;

public class YanXuanQiangGou extends BaseQiangGou {


    public static void qiangGou(AccessibilityNodeInfo rootNodeInfo) {

        AccessibilityNodeInfo qiangGou = NodeUtil.findById(rootNodeInfo, "com.netease.yanxuan:id/moutai_promotion_button");
        if (qiangGou != null && (qiangGou.getText().toString().equals("今天10:00开始抢购") || qiangGou.getText().toString().equals("今天20:00开始抢购"))) {
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
            boolean b = NodeUtil.clickNodeOrParent(order);
            Log.d(TAG, "提交了订单 = " + b);
        }
    }

    public static void event(String className) {

    }
}
