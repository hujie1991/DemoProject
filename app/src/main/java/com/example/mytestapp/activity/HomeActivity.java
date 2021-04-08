package com.example.mytestapp.activity;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.disklog.DiskLog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class HomeActivity extends BaseListActivity {

    private RxPermissions permissions;
    private Disposable permissionDisposable;

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("读取应用使用记录", "UsageStatsActivity"));
//        datas.add(new BaseItemEntity("高德定位", "GdLocationActivity"));
        datas.add(new BaseItemEntity("websocket", "WebSocketActivity"));
        datas.add(new BaseItemEntity("快捷方式测试", "ShortcutActivity"));
        datas.add(new BaseItemEntity("权限设置", "MainActivity"));
        datas.add(new BaseItemEntity("RxJava", "RxJavaActivity"));
//        datas.add(new BaseItemEntity("Setting数据读取", "SettingReadActivity"));
//        datas.add(new BaseItemEntity("文件测试", "FileTestActivity"));
        datas.add(new BaseItemEntity("无障碍辅助功能", "AccessibilityServiceActivity"));
        datas.add(new BaseItemEntity("java8新特性", "Java8NewActivity"));
//        datas.add(new BaseItemEntity("触发一个null异常", null));
        datas.add(new BaseItemEntity("截屏", "ScreenshotActivity"));
//        datas.add(new BaseItemEntity("ui测试页面", "UiTestActivity"));
//        datas.add(new BaseItemEntity("悬浮窗Activity", "SuspensionActivity"));
//        datas.add(new BaseItemEntity("设备管理器Activity", "DeviceAdminActivity"));
//        datas.add(new BaseItemEntity("步数传感器设置", "StepCounterActivity"));
//        datas.add(new BaseItemEntity("Activity生命周期", "ActivityLifeCycle"));
//        datas.add(new BaseItemEntity("Hook", "HookActivity"));

        initPermission();
    }

    @Override
    public void onClickItem(int position, String activityName) {
        if (activityName == null) {
            throw new NullPointerException("activityName is null");
        }
        DiskLog.d("position = %d, activityName = %s", position, activityName);
        Intent intent = new Intent();
        intent.setClassName(HomeActivity.this, "com.example.mytestapp.activity." + activityName);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!permissionDisposable.isDisposed()) {
            permissionDisposable.dispose();
        }
    }

    private void initPermission() {
        permissions = new RxPermissions(this);
        permissionDisposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    Log.d("HomeActivity", "aBoolean = " + aBoolean);
                    if (aBoolean) {

                    }
                });
        Log.d("HomeActivity", "permissionDisposable = " + permissionDisposable.isDisposed());
    }
}
