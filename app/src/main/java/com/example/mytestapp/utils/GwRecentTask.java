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

import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-04-14 10:46
 */
public class GwRecentTask {

    protected final Context mContext;
    protected final WindowManager mWindowManager;

    protected final AtomicBoolean isShow = new AtomicBoolean(false);

    protected int screenWidth;
    protected int screenHeight;

    final FrameLayout frameLayout;
    final com.example.mytestapp.ui.BottomRecentTaskView mBottonView;

    public GwRecentTask(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        initScreenWidth();

        frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Color.parseColor("#40000000"));
        frameLayout.setOnClickListener(view -> {hideRecentTaskView();});

        mBottonView = new BottomRecentTaskView(mContext);
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
        int topMargin = (int) (screenHeight * 0.854f + getNavigationBarHeight());
        Timber.d("topMargin = " + topMargin);
        mBottonView.setTopMarginCleanButton(topMargin);
        showRecentsTaskView(screenHeight, mBottonView);
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


    private int getNavigationBarHeight() {
        boolean navigationbarKey = isNavigationbarKey(mContext);
        if (navigationbarKey) {
            return 0;
        }
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        if (height <= 0) {
            height = dpToPx(42);
        }
        DiskLog.d("getNavigationBarHeight = %d", height);
        return height/2;
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
    public boolean isNavigationbarKey(Context context) {
        int navigation_gesture_on = Settings.Secure.getInt(context.getContentResolver(), "navigation_gesture_on", 0);
        return navigation_gesture_on == 0;
    }

    public int dpToPx(int dp) {
        return (int) (dp * mContext.getResources().getDisplayMetrics().density + 0.5f);
    }








    private Bitmap getWallPaper(Resources resources, Drawable drawable, int outWidth, int outHeight) {
        Bitmap tmp = Bitmap.createBitmap((int) outWidth, (int) outHeight, Bitmap.Config.ARGB_8888);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (tmp != null) {
            Canvas c = new Canvas(tmp);
            drawable.draw(c);
        }
//        return new BitmapDrawable(resources, tmp);
        return tmp;
    }

    public Bitmap rsBlur(Context context, Bitmap source, int radius){
        if (source == null) {
            return null;
        }
        Bitmap inputBmp = source;
        //(1)
        RenderScript renderScript =  RenderScript.create(context);

        Timber.d("scale size: %d , width = %d , height = %d", inputBmp.getWidth() * inputBmp.getHeight(), inputBmp.getWidth(), inputBmp.getHeight());

        // Allocate memory for Renderscript to work with
        //(2)
        final Allocation input = Allocation.createFromBitmap(renderScript,inputBmp);
        final Allocation output = Allocation.createTyped(renderScript,input.getType());
        //(3)
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
//        scriptIntrinsicBlur.setVar(0, radius);
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);
        //(8)
        renderScript.destroy();

        return inputBmp;
    }
}
