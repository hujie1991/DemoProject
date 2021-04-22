package com.example.mytestapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.mytestapp.R;


/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2021-04-13 16:43
 */
public class BottomRecentTaskView extends FrameLayout {

    private ImageView ivClean;

    public BottomRecentTaskView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.core_bottom_recent_task_view, this);
        ivClean = findViewById(R.id.ivClean);

        setBackgroundColor(Color.parseColor("#40ffffff"));
    }


    public void hideCleanButton() {
        ivClean.setVisibility(View.GONE);
    }

    public void showCleanButton() {
        ivClean.setVisibility(View.VISIBLE);
    }

    public void setTopMarginCleanButton(int topMargin) {
        LayoutParams layoutParams = (LayoutParams) ivClean.getLayoutParams();
        layoutParams.topMargin = topMargin;
    }

    public void setBottomMarginCleanButton(int bottomMargin) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) ivClean.getLayoutParams();
        layoutParams.bottomMargin = bottomMargin;
    }

    public void setCleanButtonGravity(int grayity) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) ivClean.getLayoutParams();
        layoutParams.gravity = grayity | layoutParams.gravity;
    }
}
