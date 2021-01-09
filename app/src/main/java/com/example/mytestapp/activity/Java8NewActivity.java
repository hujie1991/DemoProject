package com.example.mytestapp.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.UserManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mytestapp.entity.BaseItemEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Java8NewActivity extends BaseListActivity {


    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("？？？", "0"));
        datas.add(new BaseItemEntity("获取最近任务列表并打印", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("抢占音频焦点", "4"));
        datas.add(new BaseItemEntity("打开应用详情页", "5"));
        datas.add(new BaseItemEntity("是否系统用户", "6"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                break;

            case 1:
//                click1();

                break;

            case 2:
                click2();
                break;

            case 3:
                click3();
                break;

            case 4:
                click4();
                break;

            case 5:
                click5();

            case 6:
                click6();
                break;
        }
    }

    private void click6() {
        UserManager userManager = (UserManager) getSystemService(Context.USER_SERVICE);
        boolean isSystemUser = userManager.isSystemUser();
        Toast.makeText(this, "isSystemUser = " + isSystemUser, Toast.LENGTH_LONG).show();
    }

    private void click3() {
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        Function<String, String> backToString1 = String::valueOf;

        backToString.apply("123");
        Integer apply = toInteger.apply("3423");
        backToString.apply("123");

        Optional<String> bam = Optional.of("bam");
        bam.ifPresent(s -> Log.d(TAG,"s = " + s));

        List<Integer> integers = Arrays.asList(1, 2, 3);
        integers.stream().filter((a) -> a > 10).forEach(System.out::println);


    }

    private void onClick0() {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a);
            }
        };

        formula.calculate(100);
        formula.sqrt(2);
        formula.add(10, 11);
    }

    private void click1() {
        List<String> strings = Arrays.asList("one", "two", "three");

        Collections.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        Collections.sort(strings, (a, b) -> {
            return a.compareTo(b);
        });

        Collections.sort(strings, String::compareTo);
    }

    private void click2() {
        Converter<String, Integer> converter = Integer::valueOf;
        int num = 1;
        Converter<String, Integer> converter1 = (form) -> Integer.valueOf(form + num);
    }

    private AudioManager mAudioManager;
    private AudioFocusRequest mFocusRequest;
    private AudioAttributes mAudioAttributes;
    AudioManager.OnAudioFocusChangeListener mListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "focusChange = " + focusChange);
        }
    };

    private void click4() {
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        requestFocus();
    }
    private void click5() {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + "com.coloros.speechassist"));
        startActivity(intent);
    }


    public void requestFocus() {
        int result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mFocusRequest == null) {
                if (mAudioAttributes == null) {
                    mAudioAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
                }
                mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(mAudioAttributes)
                        .setWillPauseWhenDucked(true)
                        .setOnAudioFocusChangeListener(mListener)
                        .build();
            }
            result = mAudioManager.requestAudioFocus(mFocusRequest);
        } else {
            result = mAudioManager.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }


    @FunctionalInterface
    interface Converter<F, T> {
        T convert(F from);
    }

    interface Formula {
        double calculate(int a);
        default double sqrt(int a) {
            return Math.sqrt(a);
        }

        default int add(int a, int b) {
            return a + b;
        }
    }
}
