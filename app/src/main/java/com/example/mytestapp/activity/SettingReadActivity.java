package com.example.mytestapp.activity;

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
        datas.add(new BaseItemEntity("onClick0", "0"));
        datas.add(new BaseItemEntity("onClick1", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
        datas.add(new BaseItemEntity("onClick5", "5"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                break;

            case 1:
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
}
