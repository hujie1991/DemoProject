package com.example.mytestapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mytestapp.crash.DiskCrashHandler;
import com.example.mytestapp.service.CoreModel;

import timber.log.Timber;

public class MyApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        init();
    }

    private void init() {
        CoreModel.getInstance().startCoreService();
        CoreModel.getInstance().bindCoreService();

        DiskCrashHandler.init();
        registerActivityLifecycleCallbacks(new ActivityLifecycle());
        initLibrary();
        initTimber();
    }

    private void initLibrary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        if (BuildConfig.openDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


    public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
        private static final String TAG = "ActivityLife";
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Timber.d(activity.getClass().getSimpleName() + "...onCreate");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Timber.d(activity.getClass().getSimpleName() + "...onActivityStarted");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Timber.d(activity.getClass().getSimpleName() + "...onResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Timber.d(activity.getClass().getSimpleName() + "...onPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Timber.d(activity.getClass().getSimpleName() + "...onStopped");
        }
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Timber.d(activity.getClass().getSimpleName() + "...onSaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Timber.d(activity.getClass().getSimpleName() + "...onActivityDestroyed");
        }
    }
}
