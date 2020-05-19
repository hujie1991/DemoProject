package com.example.mytestapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.mytestapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepCounterActivity extends AppCompatActivity {

    private TextView stepDetectorText;
    private TextView stepCounterText;
    private TextView stepDetectorTimeText;
    private TextView isSupportStepDetector;
    private TextView isSupportStepCounter;
    private SensorManager sensorManager;
    private Sensor stepCounter;//步伐总数传感器
    private Sensor stepDetector;//单次步伐传感器
    private SensorEventListener stepCounterListener;//步伐总数传感器事件监听器
//    private SensorEventListener stepDetectorListener;//单次步伐传感器事件监听器

    private SimpleDateFormat simpleDateFormat;//时间格式化

    final String TAG = "StepCounterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        initView();
        initData();
        initListener();
    }

    protected void initView() {
        stepDetectorText = (TextView) findViewById(R.id.StepDetector);
        stepCounterText = (TextView) findViewById(R.id.StepCounter);
        stepDetectorTimeText = (TextView) findViewById(R.id.StepDetectorTime);
        isSupportStepDetector = (TextView) findViewById(R.id.IsSupportStepDetector);
        isSupportStepCounter = (TextView) findViewById(R.id.IsSupportStepCounter);
    }

    protected void initData() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器系统服务
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);//获取计步总数传感器
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);//获取单次计步传感器

        isSupportStepCounter.setText("是否支持StepCounter:" +
                String.valueOf(getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)));
        isSupportStepDetector.setText("是否支持StepDetector:" +
                String.valueOf(getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)));

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    }

    private int count = 0;

    protected void initListener() {
        stepCounterListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                boolean isMainThread = Looper.getMainLooper().getThread() == Thread.currentThread();
                count++;
                stepDetectorText.setText("count = " + count);
                Log.d(TAG, "Counter-SensorChanged isMainThread = " + isMainThread + event.values[0] + "---" + event.accuracy + "---" + event.timestamp);
                long time = event.timestamp / 1000000;
                stepDetectorTimeText.setText("当前步伐时间:" + simpleDateFormat.format(new Date(time)));
                stepCounterText.setText("总步伐计数:" + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.d(TAG, "SensorChanged = " + sensor.getName() + "---" + accuracy);
            }
        };

//        stepDetectorListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent event) {
//                Log.e(TAG, "Detector-SensorChanged = " + event.values[0] + "---" + event.accuracy + "---" + event.timestamp);
//                stepDetectorText.setText("当前步伐计数:" + event.values[0]);
//                long time = event.timestamp / 1000000;
//                stepDetectorTimeText.setText("当前步伐时间:" + simpleDateFormat.format(new Date(time)));
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//                Log.e(TAG, "Detector-Accuracy = " + sensor.getName() + "---" + accuracy);
//
//            }
//        };
    }

    private void registerSensor() {
        //注册传感器事件监听器
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER) &&
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)) {
//            sensorManager.registerListener(stepDetectorListener, stepDetector, SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(stepCounterListener, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private void unregisterSensor() {
        //解注册传感器事件监听器
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER) &&
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)) {
            sensorManager.unregisterListener(stepCounterListener);
//            sensorManager.unregisterListener(stepDetectorListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterSensor();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensor();
    }
}