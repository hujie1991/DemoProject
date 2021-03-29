package com.example.mytestapp.activity;


import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.mytestapp.entity.BaseItemEntity;

import java.util.List;

public class GdLocationActivity extends BaseListActivity {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.d(TAG, "aMapLocation = " + aMapLocation);
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    String coordType = aMapLocation.getCoordType();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e(TAG, "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("启动定位", "0"));
        datas.add(new BaseItemEntity("停止定位", "1"));
        datas.add(new BaseItemEntity("2222", "2"));
        datas.add(new BaseItemEntity("3333", "3"));
        initLocation();
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位场景为出行
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果，该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.setLocationOption(mLocationOption);

//        mLocationClient.stopLocation();
//        mLocationClient.startLocation();
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                Log.d(TAG, "开始定位");
                mLocationClient.startLocation();
                break;
            case 1:
                mLocationClient.stopLocation();
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}