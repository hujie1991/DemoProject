package com.example.mytestapp.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.pm.ShortcutManagerCompat;

import com.example.mytestapp.R;
import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.receiver.MyReceiver;

import java.util.List;

public class ShortcutActivity extends BaseListActivity {

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("创建快捷图标", "0"));
        datas.add(new BaseItemEntity("创建快捷图标1", "1"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                click0();
                break;
            case 1:
                click1();
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void click0() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createShortcutAboveO(this, "测试图标1");
        }
    }


    private void click1() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createShortcutAboveO(Context ctx, String name) {
        ShortcutManager shortcutManager = (ShortcutManager) ctx.getSystemService(Context.SHORTCUT_SERVICE);
        //判断是否支持该方式动态创建
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(ctx)) {
            Intent launchIntent = new Intent(ctx, UsageStatsActivity.class);
            launchIntent.setAction(Intent.ACTION_VIEW);
//            launchIntent.setClassName("com.gwchina.lssw.child", "com.gwchina.child.module_page.presentation.MainActivity");
            ShortcutInfo.Builder builder = new ShortcutInfo.Builder(ctx, "010101")
                    .setShortLabel(name)
                    .setIcon(Icon.createWithResource(ctx, R.mipmap.ic_launcher))
                    .setIntent(launchIntent);
            /**
             * 第二个参数为弹出创建快捷方式确认框时的回调PendingIntent，此例不关注该回调，因此为null，
             * 如果需要监听该回调，需要自定义一个BroadcastReceiver，可参考参考文献中的例子
             */
            Intent intent = new Intent(ctx, MyReceiver.class);
            intent.setAction("");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            boolean isSupport = shortcutManager.requestPinShortcut(builder.build(), null);
            Log.d(TAG,"isSupport = " + isSupport);
        }
    }
}