package com.example.mytestapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.DeviceTypeUtil;
import com.example.mytestapp.utils.GwRecentTask;
import com.example.mytestapp.utils.disklog.DiskLog;

import java.util.List;


public class VivoTaskActivity extends BaseListActivity {

    private TextView textView;
    GwRecentTask gwRecentTask;
    Handler mHandler = new Handler();

    @Override
    public void initView() {
        super.initView();
        gwRecentTask = new GwRecentTask(this);
        textView = new TextView(this);
        llContentView.addView(textView);
        initTextInfo();
    }

    private void initTextInfo() {
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
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                goVivoManager(this);
                break;
            case 1:
                mHandler.postDelayed(()-> gwRecentTask.showRecentTaskView(), 3000);
//                gwRecentTask.showRecentTaskView();
                break;
            case 2:
                gwRecentTask.hideRecentTaskView();
                break;
            case 3:
                break;
        }
    }


    private void click0() {

    }


    private void click1() {

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