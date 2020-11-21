package com.example.mytestapp.qianggou;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.mytestapp.service.GreenAccessibilityService;
import com.example.mytestapp.utils.NodeUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class JingDongQiangGou extends BaseQiangGou{

    private static Disposable backDisposable;

    public static void qiangGou(AccessibilityNodeInfo rootNodeInfo) {
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

        AccessibilityNodeInfo order = NodeUtil.findByText(rootNodeInfo, "提交订单", "android.widget.Button");
        if (order != null) {
            NodeUtil.clickNodeOrParent(order);
            Log.d(TAG, "提交了订单");
            delayBack();
        }
    }

    public static void event(String className) {
//        if ("com.jingdong.app.mall.WebActivity".equals(className)) {
//            GreenAccessibilityService.getInstance().back();
//        }
    }

    private static void delayBack() {
        if (backDisposable != null && !backDisposable.isDisposed()) {
            return;
        }
        backDisposable = Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribe(times -> {
                    GreenAccessibilityService.getInstance().back();
                    Log.d(TAG, "delayBack");
                });
    }
}
