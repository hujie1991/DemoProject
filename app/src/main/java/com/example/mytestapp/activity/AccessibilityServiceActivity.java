package com.example.mytestapp.activity;

import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.service.GreenAccessibilityService;
import com.example.mytestapp.utils.AccessibilityLogUtils;
import com.example.mytestapp.utils.PermissionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-06-15 16:42
 */
public class AccessibilityServiceActivity  extends BaseListActivity{

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("开启无障碍服务", "0"));
        datas.add(new BaseItemEntity("点击截屏", "1"));
        datas.add(new BaseItemEntity("点击返回", "2"));
        datas.add(new BaseItemEntity("点击Home", "3"));
        datas.add(new BaseItemEntity("点击开始轮询打印页面元素", "4"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                if (PermissionUtils.isAccessibilitySettingsOn(this)) {
                    Toast.makeText(this, "无障碍服务权限已开启", Toast.LENGTH_SHORT).show();
                } else {
                    PermissionUtils.startAccessibility(this);
                }
                break;
            case 1:
                if (isNext()) {
                    GreenAccessibilityService.getInstance().screenshot();
                }
                break;
            case 2:
                if (isNext()) {
                    GreenAccessibilityService.getInstance().back();
                }
                break;
            case 3:
                if (isNext()) {
                    GreenAccessibilityService.getInstance().home();
                }
                break;
            case 4:
                if (isNext()) {
                    createTimer(5000).subscribe(times -> {
                        GreenAccessibilityService instance = GreenAccessibilityService.getInstance();
                        Log.d(TAG, "instance = " + instance) ;
                        if (instance != null && instance.getRootInActiveWindow() != null) {
                            AccessibilityNodeInfo rootInActiveWindow = instance.getRootInActiveWindow();
                            AccessibilityLogUtils.dfsnode(rootInActiveWindow, 1);
                        }
                    });
                }
                break;

        }
    }

    private Flowable createTimer(long delay) {
        Flowable flowable = Flowable
                .interval(delay, 5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
        return flowable;
    }

    private boolean isNext() {
        if (PermissionUtils.isAccessibilitySettingsOn(this) && GreenAccessibilityService.getInstance() != null) {
            return true;
        }
        PermissionUtils.startAccessibility(this);
        Toast.makeText(this, "请开启无障碍服务", Toast.LENGTH_SHORT).show();
        return false;
    }
}
