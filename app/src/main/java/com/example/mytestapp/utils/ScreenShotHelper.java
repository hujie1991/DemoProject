package com.example.mytestapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.WindowManager;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class ScreenShotHelper {

    public interface OnScreenShotListener {
        void onFinish(Bitmap bitmap);
    }

    private OnScreenShotListener mOnScreenShotListener;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private final SoftReference<Activity> mRefContext;

    public ScreenShotHelper(Activity context, int resultCode, Intent data, OnScreenShotListener onScreenShotListener) {
        this.mOnScreenShotListener = onScreenShotListener;
        this.mRefContext = new SoftReference<>(context);

        mMediaProjection = getMediaProjectionManager().getMediaProjection(resultCode, data);
        mImageReader = ImageReader.newInstance(getScreenWidth(), getScreenHeight(), PixelFormat.RGBA_8888, 2);
    }

    public void startScreenShot() {
        createVirtualDisplay();

        Handler handler = new Handler();
        handler.postDelayed(() -> new CreateBitmapTask().execute(), 1000);
    }

    public Observable<Bitmap> obserable() {
        Observable<Bitmap> screenCaptureEntityObservable = Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            createVirtualDisplay();
            Thread.sleep(1000);
            Image image = mImageReader.acquireLatestImage();
            Bitmap bitmap = imageToBitmap(image);
            emitter.onNext(bitmap);

            mVirtualDisplay.release();
            mMediaProjection.stop();
        }).subscribeOn(Schedulers.io());

        return screenCaptureEntityObservable;
    }

    public  class CreateBitmapTask extends AsyncTask<Image, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Image... params) {
            return imageToBitmap(mImageReader.acquireLatestImage());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mVirtualDisplay.release();
            mMediaProjection.stop();
            if (mOnScreenShotListener != null) {
                mOnScreenShotListener.onFinish(bitmap);
            }
        }
    }

    protected Bitmap imageToBitmap(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        return bitmap;
    }

    private MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) getContext().getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
    }

    private void createVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                getScreenWidth(),
                getScreenHeight(),
                Resources.getSystem().getDisplayMetrics().densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null
        );
    }

    private Activity getContext() {
        return mRefContext.get();
    }

    public int getScreenWidth() {
        Point outSize = new Point();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealSize(outSize);
        return outSize.x;
    }

    public int getScreenHeight() {
        Point outSize = new Point();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealSize(outSize);
        return outSize.y;
    }
}
