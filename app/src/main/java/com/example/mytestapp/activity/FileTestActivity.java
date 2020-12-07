package com.example.mytestapp.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;


import androidx.core.app.ActivityCompat;

import com.example.mytestapp.entity.BaseItemEntity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-11-24 15:58
 */
public class FileTestActivity extends BaseListActivity {

    private RxPermissions permissions;
    private Disposable permissionDisposable;

    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("内部文件存储路径", "0"));
        datas.add(new BaseItemEntity("外部文件存储路径", "1"));
        datas.add(new BaseItemEntity("创建temp.txt", "2"));
        datas.add(new BaseItemEntity("Pictures/test文件.PNG", "3"));
        datas.add(new BaseItemEntity("Pictures/test文件.PNG", "4"));
        datas.add(new BaseItemEntity("Pictures/test文件.PNG", "5"));
        datas.add(new BaseItemEntity("权限判断测试", "6"));
        datas.add(new BaseItemEntity("申请存储权限", "7"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                click0();
                break;

            case 1:
                click1();
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
                break;

            case 6:
                click6();
                break;

            case 7:
                initPermission();
                break;
        }
    }

    private void click0() {
        File filesDir = getFilesDir();
        String cacheDirPath = filesDir.getAbsolutePath();
        String apkPath = cacheDirPath + File.separator + "apk" + File.separator + "test.txt";
        File file = new File(apkPath);

        File parentFile = file.getParentFile();
        try {
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


        File cacheDir = getCacheDir();
        String absolutePath = cacheDir.getAbsolutePath();


        Log.d(TAG, "cacheDirPath = " + cacheDirPath);
        Log.d(TAG, "absolutePath = " + absolutePath);
    }

    private void click1() {
        //获取外部存储根目录
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d(TAG, "absolutePath = " + absolutePath);

        //获取app外部指定路径
        File externalFilesDir = getExternalFilesDir("");
        Log.d(TAG, "externalFilesDir = " + externalFilesDir.getAbsolutePath());

        //获取app外部缓存目录路径
        File externalCacheDir = getExternalCacheDir();
        Log.d(TAG, "externalCacheDir = " + externalCacheDir.getAbsolutePath());

        //获取外部公共目录的路径
        String externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d(TAG, "externalStoragePublicDirectory = " + externalStoragePublicDirectory);
    }

    private void click2() {
        //创建自身目录下的文件
        //生成需要下载的路径，通过输入输出流读取写入
        String apkFilePath = getExternalFilesDir("apk").getAbsolutePath();
        File newFile = new File(apkFilePath + File.separator + "temp.txt");
        OutputStream os = null;
        try {
            os = new FileOutputStream(newFile);
            if (os != null) {
                os.write("file is created".getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void click3() {
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Uri uri = insertFileIntoMediaStore("test文件.PNG", "", absolutePath);
        if (uri != null) {
            Log.d(TAG, "uri = " + uri.getPath());
        }
    }

    private void click4() {
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Uri uri = insertFileIntoMediaStore("test文件.PNG", "", absolutePath);
        Log.d(TAG, "uri = " + uri.getPath());
        saveFile(this, uri);
    }

    private void click5() {
//        FileProvider.getUriForFile()
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Uri uri = insertFileIntoMediaStore("test文件.PNG", "", absolutePath);
        Log.d(TAG, "uri = " + uri.getPath());
        readUriToBitmap(this, uri);
    }

    private void click6() {
        boolean externalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean externalStorageLegacy = Environment.isExternalStorageLegacy();
        boolean isAbove10 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
        boolean isTargetAbove10 = getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.Q;
        String state = "";
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isHasSd = Environment.MEDIA_MOUNTED.equals(state);

        //有存储权限，存在SD卡，并且没有启用分区存储
        if (externalStorage && isHasSd && !isSubareaStorage(this)) {

        } else {

        }

        Log.d(TAG, "externalStorage = " + externalStorage + " , externalStorageLegacy = " + externalStorageLegacy + " , isAbove10 = " + isAbove10 + " , isTargetAbove10 = " + isTargetAbove10 + " , isHasSd = " + isHasSd);
    }


    /**
     * 是否是分区存储模式
     * */
    private boolean isSubareaStorage(Context context) {
        boolean externalStorageLegacy = Environment.isExternalStorageLegacy();
        boolean isAbove10 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
        boolean isTargetAbove10 = context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.Q;
        return isAbove10 && isTargetAbove10 && !externalStorageLegacy;
    }

    /**
     * 创建并获取公共目录下的文件路径
     * 这里的fileName指文件名，不包含路径
     * relativePath 包含某个媒体下的子路径
     *
     * */
    private Uri insertFileIntoMediaStore(String fileName, String fileType, String relativePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null;
        }
        ContentResolver resolver = getContentResolver();
        //设置文件参数到ContentValues中
        ContentValues values = new ContentValues();
        //设置文件名
        values.put(MediaStore.Images.Media.DESCRIPTION, fileName);
        //设置文件描述，这里以文件名为例子
//        values.put(MediaStore.Downloads.DESCRIPTION, fileName);
        //设置文件类型
        values.put(MediaStore.Downloads.MIME_TYPE, "image/*");
        //注意RELATIVE_PATH需要targetVersion=29
        //故该方法只可在Android10的手机上执行
        //todo
        values.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath);
        //EXTERNAL_CONTENT_URI代表外部存储器
        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //insertUri表示文件保存的uri路径
        Uri insertUri = resolver.insert(external, values);
        return insertUri;
    }

    /**
     * 公共目录下的指定文件夹下创建文件
     * 结合上面代码，我们主要是在公共目录下创建文件或文件夹拿到本地路径uri，不同的Uri，可以保存到不同的公共目录中。接下来使用输入输出流就可以写入文件
     *
     * 下面代码仅是以文件复制存储举例，`sourcePath`表示原文件地址，复制文件到新的目录; 文件下载直接下载本地，无需文件复制。
     * */
    private void saveFile(Context context, Uri insertUri) {
        if (insertUri == null) {
            return;
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            os = context.getContentResolver().openOutputStream(insertUri);
            if (os == null) {
                return;
            }
            os.write("file is created 你复活打".getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *使用MediaStore读取公共目录下的文件
     *
     * 通过ContentResolver openFileDescriptor接口，选择对应的打开方式。
     * 例如”r”表示读，”w”表示写，返回ParcelFileDescriptor类型的文件描述符。
     * */
    private Bitmap readUriToBitmap(Context context, Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        FileDescriptor fileDescriptor = null;
        Bitmap tagBitmap = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Log.d(TAG, "inputStream = " + inputStream);
//            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
//            if (parcelFileDescriptor != null && parcelFileDescriptor.getFileDescriptor() != null) {
//                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//                //转换uri为bitmap类型
//                tagBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tagBitmap;
    }

    /**
     *使用MediaStore删除文件
     * */
    public void deleteFile (Context context, Uri fileUri) {
        context.getContentResolver().delete(fileUri, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!permissionDisposable.isDisposed()) {
            permissionDisposable.dispose();
        }
    }

    private void initPermission() {
        permissions = new RxPermissions(this);
        permissionDisposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    Log.d("HomeActivity", "aBoolean = " + aBoolean);
                    if (aBoolean) {

                    }
                });
        Log.d("HomeActivity", "permissionDisposable = " + permissionDisposable.isDisposed());
    }

}
