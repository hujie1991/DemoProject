package com.example.mytestapp.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestapp.R;

public class WindowOverlayTip {

    private View viewTip;
    private LinearLayout layoutCollapse;
    private LinearLayout layoutExpand;
    private TextView tvContent;
    private WindowManager windowManager;
    private boolean show;
    private static WindowOverlayTip windowOverlayTip;

    private WindowManager.LayoutParams layoutParams;
    public static WindowOverlayTip getInstance() {
        synchronized (WindowOverlayTip.class.getName()) {
            if (windowOverlayTip == null) {
                windowOverlayTip = new WindowOverlayTip();
            }
            return windowOverlayTip;
        }
    }

    private WindowOverlayTip() {}

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void dismiss() {
        if (windowManager != null && viewTip != null && isShow()) {
            windowManager.removeViewImmediate(viewTip);
            setShow(false);
        }
    }

    public void show(Context context, String message, final int gravity) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (viewTip == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            viewTip = inflater.inflate(R.layout.widget_overlay_tip, null, false);
            layoutCollapse = viewTip.findViewById(R.id.layout_collapse);
            layoutExpand = viewTip.findViewById(R.id.layout_expand);
            tvContent = viewTip.findViewById(R.id.tv_content);
        }
        if (tvContent != null && !TextUtils.isEmpty(message)) {
            message = message.replace("app_name", context.getString(R.string.app_name));
            tvContent.setText(message);
        }
        try {
            windowManager.removeView(viewTip);
        } catch (Exception ignored) { }
        if (windowManager != null && viewTip != null) {
            setShow(true);
            layoutParams = getLayoutParams(gravity); //Gravity.TOP
            windowManager.addView(viewTip, layoutParams);
            showExpandWindow();

            viewTip.setOnTouchListener(new View.OnTouchListener() {
                int lastX = 0;
                int lastY = 0;
                int paramX = 0;
                int paramY = 0;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = (int) motionEvent.getRawX();
                            lastY = (int) motionEvent.getRawY();
                            paramX = layoutParams.x;
                            paramY = layoutParams.y;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int dx = (int) motionEvent.getRawX() - lastX;
                            int dy = (int) motionEvent.getRawY() - lastY;

                            if (gravity == Gravity.BOTTOM) {
//                                layoutParams.x = paramX - dx;
                                layoutParams.y = paramY - dy;
                            } else {
//                                layoutParams.x = paramX - dx;
                                layoutParams.y = paramY + dy;
                            }
                            windowManager.updateViewLayout(viewTip, layoutParams);
                            break;
                        case MotionEvent.ACTION_UP:
                            int moveX = (int) motionEvent.getRawX() - lastX;
                            int moveY = (int) motionEvent.getRawY() - lastY;
                            if (Math.abs(moveX) < 10 && Math.abs(moveY) < 10) {
                                switchShow();
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    public void setContent(String content) {
        if (tvContent != null) {
            tvContent.setText(content);
            windowManager.updateViewLayout(viewTip, layoutParams);
        }
    }

    private void switchShow() {
        if (layoutCollapse.getVisibility() == View.GONE) {
            showCollapseWindow();
        } else {
            showExpandWindow();
        }
    }

    public void showExpandWindow(){
        layoutCollapse.setVisibility(View.GONE);
        layoutExpand.setVisibility(View.VISIBLE);
        windowManager.updateViewLayout(viewTip, layoutParams);
    }

    public void showCollapseWindow(){
        layoutCollapse.setVisibility(View.VISIBLE);
        layoutExpand.setVisibility(View.GONE);
        windowManager.updateViewLayout(viewTip, layoutParams);
    }

    private WindowManager.LayoutParams getLayoutParams(int gravity) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.format = PixelFormat.RGBA_8888;

        params.gravity = gravity | Gravity.RIGHT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.x = 0;
        params.y = 0;
        return params;
    }
}
