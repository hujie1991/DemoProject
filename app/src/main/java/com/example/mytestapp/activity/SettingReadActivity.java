package com.example.mytestapp.activity;

import android.Manifest;
import android.content.Context;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.TelephonyUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-10-13 15:54
 */
public class SettingReadActivity extends BaseListActivity {
    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("小米导航设置", "0"));
        datas.add(new BaseItemEntity("oppo导航设置", "1"));
        datas.add(new BaseItemEntity("请求CALL_PHONE权限", "2"));
        datas.add(new BaseItemEntity("监听电话", "3"));
        datas.add(new BaseItemEntity("挂断电话", "4"));
        datas.add(new BaseItemEntity("申请电话权限", "5"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                boolean miuiNavBar = isMiuiNavBar(this);
                Toast.makeText(this, "miuiNavBar = " + miuiNavBar, Toast.LENGTH_LONG).show();
                break;

            case 1:
                boolean isNavigationbarKey = isNavigationbarKey(this);
                Toast.makeText(this, "miuiNavBar = " + isNavigationbarKey, Toast.LENGTH_LONG).show();
                break;

            case 2:
                requestCallPhonePermission();
                break;

            case 3:
                phoneListener();
                break;

            case 4:
                TelephonyUtil.endCall(this);
                break;

            case 5:
                initPermission();
                break;

            case 6:
                break;
        }
    }

    /**
     * 判断是否开启了导航按键
     * true：导航按键，false：全面屏
     */
    public static boolean isMiuiNavBar(Context context) {
        int force_fsg_nav_bar = Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0);
        return force_fsg_nav_bar == 0;
    }

    /**
     * 判断是否开启了导航按键
     * true：导航按键，false：全面屏
     */
    public static boolean isNavigationbarKey(Context context) {
        int hide_navigationbar_enable = Settings.Secure.getInt(context.getContentResolver(), "manual_hide_navigationbar", 0);
        return hide_navigationbar_enable == 0;
    }

    private void initPermission() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ANSWER_PHONE_CALLS)
                .subscribe(aBoolean -> {
                    Log.d(TAG, "aBoolean = " + aBoolean);
                    if (aBoolean) {

                    }
                });
    }

    private void phoneListener() {
        TelephonyManager tm = (TelephonyManager) getApplication().getSystemService(TELEPHONY_SERVICE);
        PhoneStateListener mPhoneStateListener = new PhoneStateListener() {

            public void onCallStateChanged(int state, String incomingNumber) {
                Log.d(TAG, "state = " + state + " , incomingNumber = " + incomingNumber);
            }
        };
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void requestCallPhonePermission() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(aBoolean -> {
                    Log.d(TAG, "aBoolean = " + aBoolean);
                    if (aBoolean) {

                    }
                });
    }
}
