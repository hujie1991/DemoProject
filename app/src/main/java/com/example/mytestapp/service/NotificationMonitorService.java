package com.example.mytestapp.service;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NotificationMonitorService extends NotificationListenerService {

    static final String TAG = "Notificationdsf";

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "onListenerConnected");
//        cancelNotifica(null);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, "onNotificationPosted isClearable = " + sbn.isClearable() + " , package = " + sbn.getPackageName());
        cancelNotifica(sbn);
    }

    private void cancelNotifica(StatusBarNotification sbn) {
        cancelNotification(sbn.getKey());

//        try {
//            // 反射执行View类的getListenerInfo()方法，拿到v的mListenerInfo对象，这个对象就是点击事件的持有者
//            Method method = NotificationListenerService.class.getDeclaredMethod("getNotificationInterface");
//            method.setAccessible(true);//由于getListenerInfo()方法并不是public的，所以要加这个代码来保证访问权限
//            Object mListenerInfo = method.invoke(this);//这里拿到的就是mListenerInfo对象，也就是点击事件的持有者
//            Log.d(TAG, "NotificationMonitorService INotificationManager = " + mListenerInfo.toString());
//
//            hookNotificationManager(mListenerInfo);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(TAG, "errormessage = " + e.getMessage());
//        }
    }


    public void hookNotificationManager(final Object originService) throws Exception {

        Log.d("INotificationManager", "INotificationManager = " + originService.toString());

        Class iNotiMagClz = Class.forName("android.app.INotificationManager");
        // 【2】得到我们的动态代理对象
        Object proxyNotiMag = Proxy.newProxyInstance(getApplicationContext().getClass().getClassLoader(), new Class[]{iNotiMagClz}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                Log.d(TAG,"name = " + name);
                if (args != null && args.length > 0) {
                    for (Object arg : args) {
                        Log.d(TAG,"arg = " + arg);
                    }
                }
                // 操作交由originService 处理，不拦截通知
                return method.invoke(originService, args);
                // 拦截通知，什么也不做
                // return null;
                // 或者是根据通知的 Tag 和 ID 进行筛选
            }
        });
        // 【3】偷天换日，使用 proxyNotiMag 替换系统的 sService
        Field sServiceField = NotificationListenerService.class.getDeclaredField("mNoMan");
        sServiceField.setAccessible(true);
        sServiceField.set(this, proxyNotiMag);
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(TAG, "onNotificationRemoved");
    }
}
