package com.example.mytestapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class IntentUtils {

    private static final String TAG = "IntentUtils";


    public static void resolveActivity(PackageManager packageManager) {
        String packageName = null, className = null;
        Intent metaIntent = new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
        try {
            ResolveInfo resolved = packageManager.resolveActivity(metaIntent, PackageManager.MATCH_DEFAULT_ONLY);
            final List<ResolveInfo> appList = packageManager.queryIntentActivities(metaIntent, PackageManager.MATCH_DEFAULT_ONLY);
            Log.d(TAG, "resolved = " + resolved);
            if (wouldLaunchResolverActivity(resolved, appList)) {
                // If only one of the results is a system app then choose that as the default.
                final ResolveInfo systemApp = getSingleSystemActivity(packageManager, appList);
                if (systemApp != null) {
                    packageName = systemApp.activityInfo.packageName;
                    className = systemApp.activityInfo.name;
                }
            }
            final ActivityInfo info = resolved.activityInfo;
            final Intent intent = packageManager.getLaunchIntentForPackage(info.packageName);
            if (intent != null) {
                if (intent.getComponent() != null) {
                    packageName = intent.getComponent().getPackageName();
                    className = intent.getComponent().getClassName();
                } else {
                    packageName = resolved.activityInfo.packageName;
                    className = resolved.activityInfo.name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "packageName = " + packageName + " , className = " + className);

//        LauncherApps launcherApps = (LauncherApps)getSystemService(Context.LAUNCHER_APPS_SERVICE);
        if (!TextUtils.isEmpty(packageName)) {
            Intent intent = new Intent();
            intent.setClassName(packageName, className);

//            LauncherActivityInfo launcherActivityInfo = launcherApps.resolveActivity(intent, Process.myUserHandle());

            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            Log.d(TAG, "intent resolveInfos.size() = " + resolveInfos.size());

//            if (launcherActivityInfo != null) {
//                Log.d(TAG, "packageName = " + packageName + " , className = " + className);
//            }
        }

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(metaIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Log.d(TAG, "metaIntent resolveInfos.size() = " + resolveInfos.size());

//        LauncherActivityInfo launcherActivityInfo = launcherApps.resolveActivity(metaIntent, Process.myUserHandle());
//        if (launcherActivityInfo != null) {
//            Log.d(TAG, "packageName = " + packageName + " , className = " + className);
//        }


    }

    private static boolean wouldLaunchResolverActivity(ResolveInfo resolved,
                                                List<ResolveInfo> appList) {
        // If the list contains the above resolved activity, then it can't be
        // ResolverActivity itself.
        for (int i = 0; i < appList.size(); ++i) {
            ResolveInfo tmp = appList.get(i);
            if (tmp.activityInfo.name.equals(resolved.activityInfo.name)
                    && tmp.activityInfo.packageName.equals(resolved.activityInfo.packageName)) {
                return false;
            }
        }
        return true;
    }

    private static ResolveInfo getSingleSystemActivity(PackageManager packageManager, List<ResolveInfo> appList) {
        ResolveInfo systemResolve = null;
        final int N = appList.size();
        for (int i = 0; i < N; ++i) {
            try {
                ApplicationInfo info = packageManager.getApplicationInfo(
                        appList.get(i).activityInfo.packageName, 0);
                if ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    if (systemResolve != null) {
                        return null;
                    } else {
                        systemResolve = appList.get(i);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        return systemResolve;
    }

}
