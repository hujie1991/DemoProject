package com.example.mytestapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestapp.R;
import com.example.mytestapp.receiver.AutostartDetector;
import com.example.mytestapp.utils.DeviceTypeUtil;
import com.example.mytestapp.utils.IntentUtils;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "MainActivity";

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    TextView textView9;
    TextView textView10;
    TextView textView11;
    TextView textView12;
    TextView textView13;
    TextView textView14;

    private static String chilePackageName = "com.gwchina.lssw.child";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
//        initTimeRec();
        //触发LeakCanary内存泄漏检测
//        new LeakThread().start();
    }

    private void initTimeRec() {
        AutostartDetector bootCompletedReceiver = new AutostartDetector();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(bootCompletedReceiver, intentFilter);
    }

    class LeakThread extends Thread {
        @Override
        public void run() {
            try {
                Log.d("TAG", "start");
                Thread.sleep(6 * 60 * 1000);
                Log.d("TAG", "end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView14);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        textView7.setOnClickListener(this);
        textView8.setOnClickListener(this);
        textView9.setOnClickListener(this);
        textView10.setOnClickListener(this);
        textView11.setOnClickListener(this);
        textView12.setOnClickListener(this);
        textView13.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StringBuilder builder = new StringBuilder();
        boolean batteryIgnored = isBatteryIgnored(this);
        builder.append("耗电保护: ").append(batteryIgnored).append(" , ");
        boolean usage = isUsage(this);
        builder.append("应用使用权限查看: ").append(usage).append(" , ");
        boolean isAutoTime = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) != 0;
        builder.append("是否开启了自动同步时间: ").append(isAutoTime).append(" , ");
        textView14.setText(builder.toString());
//        boolean b = Settings.canDrawOverlays(getApplicationContext());
//        boolean b1 = PermissionUtils.hasOverlayPermission(this);
//        textView14.setText("悬浮窗权限: Settings = " + b + " , PermissionUtils = " + b1);

//        Boolean miuiAllowShowPageFromBackground = isMIUIAllowShowPageFromBackground(this);
//        textView14.setText("后台弹出界面权限: " + miuiAllowShowPageFromBackground);
        Log.d("AccessibilityTipsActivity", "onResume()");

//        SuspensionActivity.finishActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("AccessibilityTipsActivity", "onActivityResult() requestCode = " + requestCode + " , resultCode = " + resultCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                test1();
                break;
            case R.id.textView2:
                test2();
                break;
            case R.id.textView3:
                test3();
                break;
            case R.id.textView4:
                test4();
                break;
            case R.id.textView5:
                test5();
                break;
            case R.id.textView6:
                test6();
                break;
            case R.id.textView7:
                test7();
                break;
            case R.id.textView8:
                test8();
                break;
            case R.id.textView9:
                test9();
                break;
            case R.id.textView10:
                test10();
                break;
            case R.id.textView11:
                test11();
                break;
            case R.id.textView12:
                test12();
                break;
            case R.id.textView13:
                test13();
                break;
        }
    }


    private void test1() {
        if (DeviceTypeUtil.isFuntouchOS()) {
            goVivoManager(this);
        } else {
            requestOverlayPermission(this);
        }
    }

    private void test2() {
        startAction("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
    }

    private void test3() {
        startAction("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS");
    }

    private void test4() {
        startAction("android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
    }

    private void test5() {
        startAction("android.settings.USAGE_ACCESS_SETTINGS");
    }

    private void test6() {
        if (DeviceTypeUtil.isMiui()) {
            startAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        } else if (DeviceTypeUtil.isEmui()) {
            Toast.makeText(this, "华为没有此权限", Toast.LENGTH_SHORT).show();
        } else if (DeviceTypeUtil.isColorOS()) {

        } else if (DeviceTypeUtil.isFuntouchOS()) {
            startCn("com.iqoo.powersaving/com.iqoo.powersaving.PowerSavingManagerActivity");
        }
    }

    private void test7() {
        startCn("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");
    }

    private void test8() {

    }

    private void test9() {

    }

    private void test10() {
//        startAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
    }

    private void test11() {
        startActivity(new Intent(this, StepCounterActivity.class));
    }

    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            Log.d("fsdfsd", info.toString()); // Timber 是我打印 log 用的工具，这里只是打印一下 log
            String activityName = info.manageSpaceActivityName;
            Log.i("fsdfsd", "activityName： " + activityName);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("fsdfsd", e.toString()); // Timber 是我打印 log 用的工具，这里只是打印一下 log
            return false;
        }
    }

    private void test12() {
//        startAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", chilePackageName);
        startAction(Settings.ACTION_DATE_SETTINGS);
    }

    private void test13() {
//        UserHandle myUserHandle = Process.myUserHandle();
//        UserManager userManager = (UserManager) getSystemService(Context.USER_SERVICE);
//
//        List<UserHandle> userHandles = userManager.getUserProfiles();
//        boolean systemUser = userManager.isSystemUser();

//        boolean adbEnabled = isAdbEnabled(this);
//        Log.d(TAG, "adbEnabled = " + adbEnabled + " , userHandles.seze() = " + userHandles.size() + " , isSystemUser = " + systemUser);

        IntentUtils.resolveActivity(getPackageManager());
    }

    private void startAction(String content) {
        startAction(content, "");
    }

    private void startAction(String content, String packageName) {
        Intent intent = new Intent(content);
        if (!TextUtils.isEmpty(packageName)) {
            intent.setData(Uri.parse("package:" + packageName));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void startCn(String packageName) {
        Intent intent = new Intent();
        intent.setComponent(ComponentName.unflattenFromString(packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void startPackage(String packageName) {
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    /**
     * 是否开启耗电保护
     */
    public static boolean isBatteryIgnored(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return pm.isIgnoringBatteryOptimizations(chilePackageName);
        }
        return true;
    }


    private static final int REQUEST_OVERLAY = 4444;

    /**
     * 申请悬浮窗权限，仅支持6.0及以上版本
     *
     * @param activity
     */
    public void requestOverlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + chilePackageName));
            activity.startActivityForResult(intent, REQUEST_OVERLAY);
        }
    }

    /**
     * 跳转VIVO权限页面
     *
     * @param context
     */
    public void goVivoManager(Context context) {
        Intent localIntent;
        if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A"))) || (Build.MODEL.contains("vivo Y53L"))) {
            localIntent = new Intent();
            localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity");
            localIntent.putExtra("packagename", chilePackageName);
            localIntent.putExtra("tabId", "1");
            context.startActivity(localIntent);
        } else {
            localIntent = new Intent();
            localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity");
            localIntent.setAction("secure.intent.action.softPermissionDetail");
            localIntent.putExtra("packagename", chilePackageName);
            context.startActivity(localIntent);
        }
    }

    /**
     * 判断是否开启查看应用使用权限
     *
     * @return true 有权限，false 没有权限
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean isUsage(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            }
            return true;
        } else {
            return (mode == AppOpsManager.MODE_ALLOWED);
        }
    }

    /**
     * 判断小米手机是否允许弹出后台界面权限
     * * true已允许，false已拒绝，null 发生异常
     */
    public static Boolean isMIUIAllowShowPageFromBackground(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021; // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            Method method = ops.getClass().getMethod("checkOpNoThrow",
                    int.class, int.class, String.class);
            Integer result = (Integer) method.invoke(ops, op, Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            return null;
        }
    }
}
