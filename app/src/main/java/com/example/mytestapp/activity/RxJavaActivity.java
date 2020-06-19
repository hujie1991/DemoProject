package com.example.mytestapp.activity;

import android.util.Log;

import com.example.mytestapp.entity.BaseItemEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends BaseListActivity {


    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("onClick0", "0"));
        datas.add(new BaseItemEntity("Observable.empty()", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                onClick0();
                break;

            case 1:
                onClick1();
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;
        }
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
        Observable.empty().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe()");
            }

            @Override
            public void onNext(Object s) {
                Log.d(TAG, "onNext()");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        });
    }
}
