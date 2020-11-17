package com.example.mytestapp.activity;

import android.widget.Toast;

import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.service.AccessibilityManager;
import com.example.mytestapp.service.GreenAccessibilityService;
import com.example.mytestapp.utils.PermissionUtils;

import java.util.List;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-06-15 16:42
 */
public class AccessibilityServiceActivity extends BaseListActivity {

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("开启无障碍服务", "0"));
        datas.add(new BaseItemEntity("点击开始轮询打印页面元素", "1"));
        datas.add(new BaseItemEntity("暂停轮询", "2"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (value) {
            case "0":
                if (PermissionUtils.isAccessibilitySettingsOn(this)) {
                    Toast.makeText(this, "无障碍服务权限已开启", Toast.LENGTH_SHORT).show();
                } else {
                    PermissionUtils.startAccessibility(this);
                }
                break;
            case "1":
                if (isNext()) {
                    AccessibilityManager.getInstance().startPolling();
                }
                break;
            case "2":
                AccessibilityManager.getInstance().stopPolling();
                break;
        }
    }



    private boolean isNext() {
        if (PermissionUtils.isAccessibilitySettingsOn(this) && GreenAccessibilityService.getInstance() != null) {
            return true;
        }
        PermissionUtils.startAccessibility(this);
        Toast.makeText(this, "请开启无障碍服务", Toast.LENGTH_SHORT).show();
        return false;
    }
}
