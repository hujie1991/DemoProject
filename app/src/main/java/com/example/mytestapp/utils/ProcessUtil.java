package com.example.mytestapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2019-06-19 16:12
 */
public class ProcessUtil {

    /**
     * 判断是否是主进程
     */
    public static boolean isAppMainProcess(Context context) {
        try {
            int pid = android.os.Process.myPid();
            String process = getProcessNameByPID(context, pid);
            return TextUtils.isEmpty(process) || context.getPackageName().equalsIgnoreCase(process);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 根据 pid 获取进程名
     */
    public static String getProcessNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo == null) {
                continue;
            }
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    /**
     * 获取正在运行的后台进程
     */
    public static List<PackageInfo> getRunProcess(Context context) {
        List<PackageInfo> localList = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> runList = new ArrayList<>();
        localList.forEach(info -> {
            if (((ApplicationInfo.FLAG_SYSTEM & info.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & info.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_STOPPED & info.applicationInfo.flags) == 0)
                    && !info.packageName.contains("google")//排除谷歌包名的插件应用
                    && !info.packageName.equals("com.tencent.mm")//排除微信
                    && !info.packageName.equals(context.getPackageName())
            ) {
                runList.add(info);
            }
        });
        return runList;
    }

}
