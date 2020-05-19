package com.example.mytestapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestapp.R;

import java.lang.ref.WeakReference;

public class SuspensionActivity extends AppCompatActivity {

    private LinearLayout layoutCollapse;
    private LinearLayout layoutExpand;
    private TextView tvContent;

    private final String TAG = "AccessibilityTipsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();
        setContentView(R.layout.activity_suspension);

        mWeakActivity = new WeakReference<>(this);

        initView();
    }

    private void initView() {
//        layoutCollapse = findViewById(R.id.layout_collapse);
//        layoutExpand = findViewById(R.id.layout_expand);
        tvContent = findViewById(R.id.tv_content);
        tvContent.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 10000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AccessibilityTipsActivity", "onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AccessibilityTipsActivity", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AccessibilityTipsActivity", "onStop()");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("AccessibilityTipsActivity", "dispatchTouchEvent()");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        Log.d("AccessibilityTipsActivity", "onWindowAttributesChanged()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AccessibilityTipsActivity", "onResume()----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeakActivity = null;
    }

    private static WeakReference<SuspensionActivity> mWeakActivity;

    public static void finishActivity() {
        if (mWeakActivity != null && mWeakActivity.get() != null) {
            mWeakActivity.get().finish();
        }
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        Log.d("AccessibilityTipsActivity", "onWindowFocusChanged() hasFocus = " + hasFocus);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d("AccessibilityTipsActivity", "onBackPressed()");
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("AccessibilityTipsActivity", "onKeyDown()");
//        return super.onKeyDown(keyCode, event);
//    }
}
