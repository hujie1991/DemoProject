package com.example.mytestapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestapp.R;

public class ActivityLifeCycle extends AppCompatActivity {

    final String TAG = "ActivityLifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        initView();
    }

    private void initView() {
        findViewById(R.id.tvStartTime).setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS)));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent()");
    }
}
