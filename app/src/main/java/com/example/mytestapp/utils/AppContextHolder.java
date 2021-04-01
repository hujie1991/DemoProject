package com.example.mytestapp.utils;

import android.content.Context;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-04-01 15:42
 */
public class AppContextHolder {

    private static Context appContext;

    public static void setAppContext(Context context) {
        if (context != null) {
            appContext = context.getApplicationContext();
        }
    }

    public static Context getAppContext() {
        return appContext;
    }

}
