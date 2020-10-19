package com.example.mytestapp.activity;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mytestapp.R;
import com.example.mytestapp.utils.PermissionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UsageStatsActivity extends BaseActivity {

    private TextView tvContent;
    private EditText etNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_stats);
        initView();
    }

    private void initView() {
        tvContent = findViewById(R.id.tvContent);
        etNum = findViewById(R.id.etNum);
    }

    public void startQuery(View view) {
        if (PermissionUtils.isUsage(this)) {
            String s = etNum.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                long currTimes = System.currentTimeMillis();
                queryUsage(currTimes - Integer.parseInt(s) * 60000, System.currentTimeMillis());
            }
        } else {
            startAction("android.settings.USAGE_ACCESS_SETTINGS");
        }
    }


    private void startAction(String content) {
        Intent intent = new Intent(content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void queryUsage(long startTs, long endTs) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(startTs, endTs);
        UsageEvents.Event eventOut = new UsageEvents.Event();
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.ENGLISH);
        Date date = new Date();
        String item;
        int count = 0;

        while (usageEvents.hasNextEvent()) {
            if (!usageEvents.getNextEvent(eventOut)) {
                break;
            }
            date.setTime(eventOut.getTimeStamp());
            switch (eventOut.getEventType()) {
                case UsageEvents.Event.MOVE_TO_FOREGROUND:
                case UsageEvents.Event.MOVE_TO_BACKGROUND:
                    item = simpleDateFormat.format(date) + " : " + eventOut.getEventType() + " , " + eventOut.getPackageName() + "/" + eventOut.getClassName() + "\n";
                    builder.append(item);
//                    Log.d(TAG, item);
                    break;
                case UsageEvents.Event.SCREEN_INTERACTIVE:
                case UsageEvents.Event.SCREEN_NON_INTERACTIVE:
                case UsageEvents.Event.KEYGUARD_SHOWN:
                case UsageEvents.Event.KEYGUARD_HIDDEN:
                    item = simpleDateFormat.format(date) + " : " + eventOut.getEventType() + "\n";
                    builder.append(item);
//                    Log.d(TAG, item);
                    break;
                default:
                    continue;
            }
            count++;
        }

        String result = builder.toString();
        tvContent.setText(result);

        Log.d(TAG, "result = " + result);
    }
}
