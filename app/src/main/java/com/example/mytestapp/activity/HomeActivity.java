package com.example.mytestapp.activity;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import com.example.mytestapp.entity.BaseItemEntity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeActivity extends BaseListActivity {

    private RxPermissions permissions;
    private Disposable permissionDisposable;

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("权限设置", "MainActivity"));
        datas.add(new BaseItemEntity("步数传感器设置", "StepCounterActivity"));
        datas.add(new BaseItemEntity("悬浮窗Activity", "SuspensionActivity"));
        datas.add(new BaseItemEntity("设备管理器Activity", "DeviceAdminActivity"));
        datas.add(new BaseItemEntity("RxJava练习", "RxJavaActivity"));
        datas.add(new BaseItemEntity("java8新特性", "Java8NewActivity"));
        datas.add(new BaseItemEntity("Activity生命周期", "ActivityLifeCycle"));
        datas.add(new BaseItemEntity("Hook", "HookActivity"));

        initPermission();
    }

    @Override
    public void onClickItem(int position, String activityName) {
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
        permissionDisposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d("HomeActivity", "aBoolean = " + aBoolean);
                        if (aBoolean) {

                        }
                    }
                });
        Log.d("HomeActivity", "permissionDisposable = " + permissionDisposable.isDisposed());
    }
}
