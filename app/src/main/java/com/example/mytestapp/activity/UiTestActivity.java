package com.example.mytestapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestapp.R;
import com.example.mytestapp.ui.MyProgress;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-09-09 09:48
 */
public class UiTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_ui_text_activity);
        initView();
    }

    private void initView() {
        final MyProgress myProgress = findViewById(R.id.progress);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        EditText etNum = findViewById(R.id.etNum);
        TextView tvCon = findViewById(R.id.tvCon);
        tvCon.setOnClickListener(v ->{
            String s = etNum.getText().toString();
            int num = 10;
            if (!TextUtils.isEmpty(s)) {
                num = Integer.parseInt(s);
            }
            myProgress.setProgress(num);

            if (num >= 50) {
                progressBar.setBackgroundResource(R.drawable.progress_bar_progres_color);
            } else {
                progressBar.setBackgroundResource(R.drawable.progress_bar_bg);
            }
        });
    }
}
