package com.example.mytestapp.activity;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.example.mytestapp.entity.BaseItemEntity;

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
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
        datas.add(new BaseItemEntity("onClick5", "5"));
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
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
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
}
