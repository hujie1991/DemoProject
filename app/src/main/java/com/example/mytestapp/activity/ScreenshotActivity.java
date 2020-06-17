package com.example.mytestapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.List;

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
    }

    @Override
    public void initView() {
        super.initView();
        imageView = new ImageView(this);
        llContentView.addView(imageView);
    }

    @Override
    public void onClickItem(int position, String value) {
        if (position == 0) {
            if (mData != null) {
                mRecyclerView.postDelayed(this::startScreenShot, 1000);
                return;
            }
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    private void startScreenShot() {
        ScreenShotHelper screenShotHelper = new ScreenShotHelper(getApplicationContext(), RESULT_OK, mData, bitmap -> {
            Toast.makeText(this, "截屏成功", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "截屏成功");
            imageView.setImageBitmap(bitmap);
            saveCapture(bitmap);
        });
        screenShotHelper.startScreenShot();
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
