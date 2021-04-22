package com.example.mytestapp.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.mytestapp.ui.BottomRecentTaskView;
import com.example.mytestapp.utils.disklog.DiskLog;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-04-14 10:46
 */
public class GwOppoRecentTask {

    protected final Context mContext;
    protected final WindowManager mWindowManager;

    protected final AtomicBoolean isShow = new AtomicBoolean(false);

    protected int screenWidth;
    protected int screenHeight;

    final FrameLayout frameLayout;
    final BottomRecentTaskView mBottonView;

    public GwOppoRecentTask(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        initScreenWidth();

        frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Color.parseColor("#30000000"));
        frameLayout.setOnClickListener(view -> {hideRecentTaskView();});

        mBottonView = new BottomRecentTaskView(mContext);
//        mBottonView.setCleanButtonGravity(Gravity.BOTTOM);
        mBottonView.setOnClickListener(view -> {hideRecentTaskView();});
    }


    public final void showRecentTaskView() {
        if (isShow.compareAndSet(false, true)) {
            Timber.d("showRecentTaskView()");
            show();
        }
    }


    public final void hideRecentTaskView() {
        if (isShow.compareAndSet(true, false)) {
            Timber.d("removeRecentTaskView()");
            hide();
        }
    }


    protected void show() {
        int topMargin = getMarginTop();
        mBottonView.setTopMarginCleanButton(topMargin);
        showRecentsTaskView(screenHeight, mBottonView);
        showRecentsTaskView(topMargin, frameLayout);
    }

    protected void hide() {
        removeOverlap(frameLayout);
        removeOverlap(mBottonView);
    }

    /**
     * 展示顶部的最近任务视图
     */
    private void showRecentsTaskView(int height, View view) {
        WindowManager.LayoutParams wmParams = getDefaultLayoutParams();
        wmParams.gravity = Gravity.TOP | Gravity.LEFT;
        wmParams.height = height;
        wmParams.x = 0;
        wmParams.y = 0;
        addOverlap(view, wmParams);
    }


    /**
     * 添加window view
     */
    protected void addOverlap(View view, ViewGroup.LayoutParams params) {
        if (view != null) {
            addViewForWindow(view, params);
        }
    }


    /**
     * 移除window view
     */
    protected void removeOverlap(View view) {
        if (view != null && view.isAttachedToWindow()) {
            removeViewForWindow(view);
        }
    }


    protected int getMarginTop() {
        String customOsVersion = DeviceTypeUtil.getCustomOsVersion();
        String osVersionName = customOsVersion.replace("V", "");
        boolean navigationbarKey = isNavigationbarKey(mContext);

        int marginTop = 123;
        if (osVersionName.startsWith("5")) {
            marginTop = (int) (screenHeight * (navigationbarKey ? 0.82f : 0.85f));
        } else if (osVersionName.startsWith("7") || osVersionName.startsWith("6.7")) {
            marginTop = (int) (screenHeight * (navigationbarKey ? 0.828f : 0.887f));
        } else if (osVersionName.startsWith("6")) {
            marginTop = (int) (screenHeight * (navigationbarKey ? 0.86f : 0.89f));
        } else if (osVersionName.startsWith("11")) {
            marginTop = (int) (screenHeight * (navigationbarKey ? 0.846f : 0.895f));
        }
        Timber.d("marginTop = " + marginTop + " , navigationbarKey = " + navigationbarKey + " , osVersionName = " + osVersionName);
        return marginTop;
    }


    /**
     * 获取默认的 WindowManager.LayoutParams
     */
    protected WindowManager.LayoutParams getDefaultLayoutParams() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        wmParams.width = screenWidth;

        wmParams.packageName = mContext.getPackageName();
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SCALED
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return wmParams;
    }

    private void addViewForWindow(View view, ViewGroup.LayoutParams params) {
            mWindowManager.addView(view, params);
    }

    private void removeViewForWindow(View view) {
            mWindowManager.removeView(view);
    }

    public int initScreenWidth() {
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        screenWidth = point.x;
        screenHeight = point.y;
        return point.x;
    }

    /**
     * 判断是否开启了导航按键
     * true：导航按键，false：全面屏
     */
    public static boolean isNavigationbarKey(Context context) {
        int hide_navigationbar_enable = Settings.Secure.getInt(context.getContentResolver(), "manual_hide_navigationbar", 0);
        return hide_navigationbar_enable == 0;
    }

    public int dpToPx(int dp) {
        return (int) (dp * mContext.getResources().getDisplayMetrics().density + 0.5f);
    }

}
