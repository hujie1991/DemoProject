package com.example.mytestapp.receiver;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.UserHandle;
import android.util.Log;

import java.util.List;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-03-23 14:00
 */
public class LauncherAppsCompatVL {

    static final String TAG = "LauncherAppsCompatVL";

    final WrappedCallback wrappedCallback = new WrappedCallback();
    private static final LauncherAppsCompatVL mInstance = new LauncherAppsCompatVL();

    private LauncherAppsCompatVL(){

    }

    public static LauncherAppsCompatVL getInstance() {
        return mInstance;
    }

    public void registerCallback(Context context) {
        LauncherApps mLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        mLauncherApps.registerCallback(wrappedCallback);
    }

    private static class WrappedCallback extends LauncherApps.Callback {

        public WrappedCallback() {
            Log.d(TAG, "WrappedCallback");
        }

        @Override
        public void onPackageRemoved(String packageName, UserHandle user) {
            Log.d(TAG, "onPackageRemoved packageName = " + packageName);
        }

        @Override
        public void onPackageAdded(String packageName, UserHandle user) {
            Log.d(TAG, "onPackageAdded packageName = " + packageName);
        }

        @Override
        public void onPackageChanged(String packageName, UserHandle user) {
            Log.d(TAG, "onPackageChanged packageName = " + packageName);
        }

        @Override
        public void onPackagesAvailable(String[] packageNames, UserHandle user, boolean replacing) {
            Log.d(TAG, "onPackagesAvailable");
        }

        @Override
        public void onPackagesUnavailable(String[] packageNames, UserHandle user, boolean replacing) {
            Log.d(TAG, "onPackagesUnavailable");
        }

        @Override
        public void onPackagesSuspended(String[] packageNames, UserHandle user) {
            Log.d(TAG, "onPackagesSuspended");
        }

        @Override
        public void onPackagesUnsuspended(String[] packageNames, UserHandle user) {
            Log.d(TAG, "onPackagesUnsuspended");
        }

        @Override
        public void onShortcutsChanged(String packageName, List<ShortcutInfo> shortcuts, UserHandle user) {
            Log.d(TAG, "onShortcutsChanged packageName = " + packageName);
        }
    }
}
