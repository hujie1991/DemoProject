package com.example.mytestapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestapp.MyApplication;
import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.DeviceTypeUtil;
import com.example.mytestapp.utils.GwOppoRecentTask;
import com.example.mytestapp.utils.GwRecentTask;
import com.example.mytestapp.utils.disklog.DiskLog;

import java.lang.reflect.Method;
import java.util.List;

import timber.log.Timber;


public class OppoTaskActivity extends BaseListActivity {

    private TextView textView;
    GwOppoRecentTask gwRecentTask;
    Handler mHandler = new Handler();

    @Override
    public void initView() {
        super.initView();
        gwRecentTask = new GwOppoRecentTask(this);
        textView = new TextView(this);
        llContentView.addView(textView);
        initTextInfo();
    }

    private void initTextInfo() {
        boolean navigationbarKey = GwOppoRecentTask.isNavigationbarKey(this);
        int appVersionCode = getAppVersionCode("com.vivo.upslide");
        String appVersionName = getAppVersionName("com.vivo.upslide");
        String appName = getAppName("com.vivo.upslide");
        String customOsVersion = DeviceTypeUtil.getCustomOsVersion();
        Point screenPoint = getScreenPoint();
        float density = getResources().getDisplayMetrics().density;
        int navigationBarHeight = getNavigationBarHeight();

        String builder = "appVersionCode = " + appVersionCode + "\n" +
                "appVersionName = " + appVersionName + "\n" +
                "appName = " + appName + "\n" +
                "虚拟按键导航 = " + navigationbarKey + "\n" +
                "width = " + screenPoint.x + " , height = " + screenPoint.y + ", density = " + density + "\n" +
                "customOsVersion = " + customOsVersion + "\n" +
                "navigationBarHeight = " + navigationBarHeight + "\n";
        textView.setText(builder);
    }

    public int getAppVersionCode(final String packageName) {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getAppVersionName(final String packageName) {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getAppName(final String packageName) {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("开启悬浮窗权限", "0"));
        datas.add(new BaseItemEntity("显示悬浮窗", "1"));
        datas.add(new BaseItemEntity("隐藏悬浮窗", "2"));
        datas.add(new BaseItemEntity("开始监听oppo桌面状态", "3"));
        datas.add(new BaseItemEntity("延迟5秒获取launcher_state", "4"));
        datas.add(new BaseItemEntity("开始监听系统按键事件", "5"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 123);
                break;
            case 1:
                mHandler.postDelayed(()-> gwRecentTask.showRecentTaskView(), 3000);
                break;
            case 2:
                gwRecentTask.hideRecentTaskView();
                break;
            case 3:
                click3();
                break;
            case 4:
                click4();
            case 5:
                click5();
                break;
        }
    }

    private InnerReceiver mInnerReceiver = new InnerReceiver();

    private void click5() {
        IntentFilter mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        MyApplication.getContext().registerReceiver(mInnerReceiver, mFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getContext().unregisterReceiver(mInnerReceiver);
    }

    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Timber.d("receive action:" + action);
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if ("homekey".equalsIgnoreCase(reason) || "recentapps".equalsIgnoreCase(reason)) {
                        Timber.d("receive action:" + action + ",reason:" + reason);
                    }
                }
            }
        }

    }

    private final static String LAUNCHER_STATE = "launcher_state";
    Handler mMainHandler = new Handler();

    private void click4() {
        mHandler.postDelayed(()-> {
            int launcherState = Settings.Secure.getInt(getContentResolver(), LAUNCHER_STATE, 0);
            textView.setText(textView.getText().toString() + " \n launcherState = " + launcherState);
        }, 3000);
    }

    private ContentObserver mLauncherStateContentObserver = new ContentObserver(mMainHandler) {
        @Override
        public void onChange(boolean selfChange) {
            int launcherState = Settings.Secure.getInt(getContentResolver(), LAUNCHER_STATE, 0);
            Timber.d("launcher_state changed:" +launcherState) ;
            Toast.makeText(OppoTaskActivity.this, "launcher_state = " + launcherState, Toast.LENGTH_SHORT).show();
            switch (launcherState) {
                case 0:
                    break;
                case 2:
                    break;
                case 5:
                    break;
                case 7:
                    break;
                case 8:
                    break;

            }
        }
    };

    private void click3() {
        getContentResolver().registerContentObserver(Settings.Secure.getUriFor(LAUNCHER_STATE), false, mLauncherStateContentObserver);
    }

    public static void goVivoManager(Context context) {
        Intent localIntent;
        if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A"))) || (Build.MODEL.contains("vivo Y53L"))) {
            localIntent = new Intent();
            localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity");
            localIntent.putExtra("packagename", context.getPackageName());
            localIntent.putExtra("tabId", "1");
            context.startActivity(localIntent);
        } else {
            localIntent = new Intent();
            localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity");
            localIntent.setAction("secure.intent.action.softPermissionDetail");
            localIntent.putExtra("packagename", context.getPackageName());
            context.startActivity(localIntent);
        }
    }

    public Point getScreenPoint() {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point;
    }

    protected int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        DiskLog.d("getNavigationBarHeight = %d", height);
        return height;
    }

}