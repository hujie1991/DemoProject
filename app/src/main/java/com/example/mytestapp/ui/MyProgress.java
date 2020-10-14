package com.example.mytestapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-09-09 16:06
 */
public class MyProgress extends AppCompatTextView {

    Paint paint = new Paint();
    int x;
    Rect rect;

    public MyProgress(Context context) {
        super(context);
        initView();
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        rect = new Rect();
    }

    @Override

    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(rect, paint);

        paint.setColor(Color.RED);
        if (x != 100) {
            canvas.drawRect(0, 0, getMeasuredWidth() * x / 100, getMeasuredHeight(), paint);
        } else {
            paint.setColor(Color.GREEN);
            canvas.drawRect(0, 0, getMeasuredWidth() * x / 100, getMeasuredHeight(), paint);
        }
        super.onDraw(canvas);
    }

    public void setProgress(int x) {
        this.x = x;
        invalidate();
    }
}
