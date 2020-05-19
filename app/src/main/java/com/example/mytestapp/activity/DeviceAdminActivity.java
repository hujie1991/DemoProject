package com.example.mytestapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mytestapp.R;
import com.example.mytestapp.receiver.GreenDeviceAdminReceiver;

public class DeviceAdminActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView1;
    TextView textView2;
    TextView textView13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_admin);
        init();
    }

    private void init() {
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView13 = findViewById(R.id.textView13);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                test1();
                break;
            case R.id.textView2:
                test2();
                break;
        }
    }

    private void test1() {
        startAdminActive(this);
    }


    /**
     * 启动激活设备管理页面
     * @param context
     */
    public static void startAdminActive(Activity context) {
        ComponentName mAdminName = new ComponentName(context, GreenDeviceAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, context.getString(R.string.app_name));
        context.startActivityForResult(intent, 104);
        Log.d("startAdminActive", "startAdminActive");
    }

    /**
     * 判断是否已激活设备管理
     * @param context
     * @return
     */
    public static boolean isAdminActive(Context context) {
        try {
            DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mDeviceComponentName = new ComponentName(context, GreenDeviceAdminReceiver.class);
            return mDPM.isAdminActive(mDeviceComponentName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView13.setText("设备管理器权限: " + isAdminActive(this));
    }

    private void test2() {

    }
}
