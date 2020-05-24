package com.example.mytestapp.utils;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;

import androidx.annotation.RequiresApi;


public class PermissionUtils {

    /**
     * 判断是否有悬浮窗权限，6.0以下判断为默认有，6.0及以上版本通过系统api判断
     */
    public static boolean hasOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (DeviceTypeUtil.isFuntouchOS()) {
                int currentMode = getFloatPermissionStatus(context);
                return currentMode == 0;
            }
            if (!Settings.canDrawOverlays(context)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取悬浮窗权限状态
     *
     * @param context
     * @return 1或其他是没有打开，0是打开，该状态的定义和{@link android.app.AppOpsManager#MODE_ALLOWED}，MODE_IGNORED等值差不多，自行查阅源码
     */
    public static int getFloatPermissionStatus(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("content://com.iqoo.secure.provider.secureprovider/allowfloatwindowapp");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.getColumnNames();
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentlmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return getFloatPermissionStatus2(context);
            }

        } else {
            return getFloatPermissionStatus2(context);
        }
    }

    /**
     * VIVO判断悬浮窗权限方法
     *
     * @param context
     * @return
     */
    private static int getFloatPermissionStatus2(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/float_window_apps");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentMode = cursor.getInt(cursor.getColumnIndex("currentmode"));
                cursor.close();
                return currentMode;
            } else {
                cursor.close();
                return 1;
            }
        }
        return 1;
    }

    /**
     * 判断是否开启查看应用使用权限
     *
     * @return true 有权限，false 没有权限
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isUsage(Context context) {
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

}
