package com.example.mytestapp.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.DeviceTypeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RxJavaActivity extends BaseListActivity {

    ImageView imageView;
    ImageView imageView1;

    ArrayList<String> actionList;

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("RxJava", "0"));
        datas.add(new BaseItemEntity("nexs", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
    }

    @Override
    public void initView() {
        super.initView();
        imageView = new ImageView(this);
        imageView.setPadding(0, 20, 0, 0);
        llContentView.addView(imageView);
        imageView1 = new ImageView(this);
        imageView1.setPadding(0, 20, 0, 0);
        llContentView.addView(imageView1);
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                onClick0();
                break;

            case 1:
                break;

            case 2:
                try {
                    PackageManager pm = getPackageManager();
                    PackageInfo pi = pm.getPackageInfo("com.coloros.colordirectservice", 0);
                    String name =  pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
                    Log.d(TAG, "name = " + name);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                break;

            case 4:
                Schedulers.io().scheduleDirect(()-> {
                    Timber.d("scheduleDirect()");
                }, 1, TimeUnit.SECONDS);
                break;
        }
    }

    public StringBuffer buildGuideFileName() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Build.MANUFACTURER).append("_").append(Build.VERSION.SDK_INT).append("_").append(DeviceTypeUtil.getCustomOsVersion());
        return stringBuffer;
    }

    private void onClick0() {

        long id = Thread.currentThread().getId();
        Log.d(TAG, "main id = " + id);

        Observable<Integer> objectObservable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            long threadId = Thread.currentThread().getId();
            Log.d(TAG, "Observable threadId = " + threadId);
//            emitter.onError(new Throwable("dfsfsdf"));
        });

//        objectObservable.subscribeOn(Schedulers.newThread()).
//                observeOn(Schedulers.io())


        Disposable subscribe = objectObservable
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
        .subscribe(consun -> {
            long threadId = Thread.currentThread().getId();
            Log.d(TAG, "consun = " + consun + " , threadId = " + threadId);
        }, error -> {
            Log.d(TAG, "error = " + error.getMessage());
        });


        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                long threadId = Thread.currentThread().getId();
                Log.d(TAG, "integer = " + integer + " , threadId = " + threadId);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        objectObservable.subscribe(observer);
    }

    private void onClick1() {

    }
}
