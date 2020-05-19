package com.example.mytestapp.activity;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestapp.R;
import com.example.mytestapp.service.NotificationMonitorService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook);
        TextView tvOne = findViewById(R.id.tvOne);
        tvOne.setOnClickListener(this);
        findViewById(R.id.tvTwo).setOnClickListener(this);
        try {
            // 1.点击事件拦截
            hookOnClickListener(tvOne);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookOnClickListener(View view) throws Exception {
        // 1.反射得到 ListenerInfo 对象
        Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
        getListenerInfo.setAccessible(true);
        Object listenerInfo = getListenerInfo.invoke(view);

        //2.得到原始的 OnClickListener事件方法
        Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
        Field mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
        mOnClickListener.setAccessible(true);
        View.OnClickListener originOnClickListener = (View.OnClickListener)mOnClickListener.get(listenerInfo);

        // 3.用 Hook代理类 替换原始的 OnClickListener
        View.OnClickListener hookedOnClickListener = new HookedClickListenerProxy(originOnClickListener);
        mOnClickListener.set(listenerInfo, hookedOnClickListener);
    }

    static class HookedClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener origin;
        public HookedClickListenerProxy(View.OnClickListener originOnClickListener) {
            this.origin = originOnClickListener;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), " 拦截后 after Hook Click Listener", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "hook后的方法");
            if (origin != null) {
                origin.onClick(v);
            }
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvOne:
                Log.d(TAG, "发送了通知");
//                NotifyModel.bindNotify(this, "测试消息", "这是第几个消息");
//                toggleNotificationListenerService(this);
                break;

            case R.id.tvTwo:
                try {
                    // 2. 通知拦截
                    hookNotificationManager(this);
                    Log.d(TAG, "反射监听通知成功");
//
//                    \9kl                              0uuuu
//
//                    toggleNotificationListenerService(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }



    public static void hookNotificationManager(final Context context) throws Exception {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Method getService = NotificationManager.class.getDeclaredMethod("getService");
        getService.setAccessible(true);
        // 【1】得到系统的 sService
        final Object originService = getService.invoke(notificationManager);
        Log.d("INotificationManager", "INotificationManager = " + originService.toString());

        Class iNotiMagClz = Class.forName("android.app.INotificationManager");
        // 【2】得到我们的动态代理对象
        Object proxyNotiMag = Proxy.newProxyInstance(context.getClass().getClassLoader(), new Class[]{iNotiMagClz}, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String name = method.getName();
                        Log.d(TAG,"name = " + name);
                        if (args != null && args.length > 0) {
                            for (Object arg : args) {
                                Log.d(TAG,"arg = " + arg);
                            }
                        }
                        Toast.makeText(context.getApplicationContext(), "检测到有人发通知了", Toast.LENGTH_SHORT).show();
                        // 操作交由originService 处理，不拦截通知
                        return method.invoke(originService, args);
                        // 拦截通知，什么也不做
                        // return null;
                        // 或者是根据通知的 Tag 和 ID 进行筛选
                    }
                });
        // 【3】偷天换日，使用 proxyNotiMag 替换系统的 sService
        Field sServiceField = NotificationManager.class.getDeclaredField("sService");
        sServiceField.setAccessible(true);
        sServiceField.set(notificationManager, proxyNotiMag);
    }

}
