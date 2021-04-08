package com.example.mytestapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.ScreenShotHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-06-15 18:15
 */
public class ScreenshotActivity extends BaseListActivity {

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private Intent mData;
    private ImageView imageView;

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("点击截屏", "0"));
        datas.add(new BaseItemEntity("延迟10秒截屏", "1"));
    }

    @Override
    public void initView() {
        super.initView();
        imageView = new ImageView(this);
        imageView.setPadding(0, 20, 0, 0);
        llContentView.addView(imageView);
    }

    @Override
    public void onClickItem(int position, String value) {
        if (position == 0) {
            if (mData != null) {
                mRecyclerView.postDelayed(this::startScreenShot, 3000);
                return;
            }
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        } else if (position == 1) {
            new ScreenShotHelper(this, RESULT_OK, mData, null)
                    .obserable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        Toast.makeText(this, "截屏成功", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "截屏成功");
                        imageView.setImageBitmap(bitmap);
                        saveCapture(bitmap);
                    });
        }
    }

    private void startScreenShot() {
        ScreenShotHelper screenShotHelper = new ScreenShotHelper(this, RESULT_OK, mData, bitmap -> {
            Toast.makeText(this, "截屏成功", Toast.LENGTH_SHORT).show();
            bitmap = AddTimeWatermark(bitmap);

            Log.d(TAG, "截屏成功");
            imageView.setImageBitmap(bitmap);
            saveCapture(bitmap);
        });
        screenShotHelper.startScreenShot();
    }



    private Bitmap AddTimeWatermark(Bitmap mBitmap) {
        //获取原始图片与水印图片的宽与高
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        Bitmap mNewBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mNewBitmap);
        //向位图中开始画入mBitmap原始图片
        mCanvas.drawBitmap(mBitmap,0,0,null);
        //添加文字
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#70000000"));
        int textSize = dp2px(14);
        mPaint.setTextSize(textSize);

        String text = "王者荣耀 绝地求生";

        int marginleft = dp2px(15);
        int marginTop = dp2px(30);

        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);

        Bitmap textBgBitmap = Bitmap.createBitmap(bounds.width() + 20, textSize + 10, Bitmap.Config.ARGB_8888);
//        textBgBitmap.eraseColor(Color.parseColor("#60ffffff"));
        textBgBitmap.eraseColor(Color.RED);
        mCanvas.drawBitmap(textBgBitmap, (mBitmapWidth - bounds.width() - 20) / 2, mBitmapHeight - textSize - 5,null);

        //水印文字的位置坐标
        mCanvas.drawText(text, (mBitmapWidth - bounds.width()) / 2, mBitmapHeight - 5, mPaint);

        return mNewBitmap;
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION: {
                if (resultCode == RESULT_OK && data != null) {
                    this.mData = data;
                    startScreenShot();
                }
                break;
            }
        }
    }

    private File saveCapture(Bitmap bitmap) {
        if (bitmap == null) return null;
        File captureFile = new File(Environment.getExternalStorageDirectory() + "/screenshot/" + System.currentTimeMillis() + ".jpg");
        if (!captureFile.exists()) {
            captureFile.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(captureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
            outputStream.flush();
            outputStream.close();
            return captureFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
