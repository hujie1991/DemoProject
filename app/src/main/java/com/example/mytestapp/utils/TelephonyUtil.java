package com.example.mytestapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2019-08-01 19:15
 */
public class TelephonyUtil {

    /**
     * 挂断电话
     */
    public static void endCall(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                TelecomManager telMag = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                if (context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                telMag.endCall();
            } else {
                TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                Method mt;
                mt = mTelephonyManager.getClass().getMethod("endCall");
                //允许访问私有方法
                mt.setAccessible(true);
                mt.invoke(mTelephonyManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
