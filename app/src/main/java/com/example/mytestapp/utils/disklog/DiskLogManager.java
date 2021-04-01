package com.example.mytestapp.utils.disklog;

import android.annotation.SuppressLint;
import android.text.TextUtils;


import com.example.mytestapp.utils.AppContextHolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2019-08-13 09:32
 */
public class DiskLogManager {
    private static final int QUEUE_LENGTH = 10000 * 10;
    private static final int CHECK_CLEAN_LOG_DELAY = 86400000;
    private static final Pattern pattern = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2})\\.*");
    private static final String LOG_SUFFIX = ".gol";

    private BlockingQueue<String> mBlockQueue;
    private WriteTask mWriteTask;
    private boolean logEnable = true;
    private long mCheckCleanLog;

    public static DiskLogManager getInstance() {
        return DiskLogManagerHolder.mInstance;
    }

    private static class DiskLogManagerHolder {
        private static final DiskLogManager mInstance = new DiskLogManager();
    }

    private DiskLogManager() {
    }

    public void log(String content) {
        if (!logEnable) return;
        if (mBlockQueue == null) {
            mBlockQueue = new LinkedBlockingQueue<>(QUEUE_LENGTH);
        }
        if (!mBlockQueue.offer(content)) {
            Timber.e("队列已满，添加失败");
        }
        prepareLog();
    }

    private synchronized void prepareLog() {
        if (mWriteTask == null || mWriteTask.isInterrupted() || !mWriteTask.isAlive()) {
            mWriteTask = new WriteTask(mBlockQueue);
            mWriteTask.start();
        }
    }

    private String getCachePath() {
        String path = AppContextHolder.getAppContext().getFilesDir().getAbsolutePath() + File.separator + "log" + File.separator;
        Timber.d("path = %s", path);
        return path;
    }

    private String getLogFilePath(String fileName) {
        return getCachePath() + fileName + LOG_SUFFIX;
    }

    private void makeFilePath(File file) {
        if (file == null) {
            return;
        }
        File parent = file.getParentFile();
        if (!parent.exists()) {
            makeDir(parent);
        }
    }

    private boolean makeDir(File file) {
        return file != null && (file.exists() || file.mkdirs());
    }

    private class WriteTask extends Thread {
        private BlockingQueue<String> mBlockingQueue;
        @SuppressLint("SimpleDateFormat")
        private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private static final int TIMEOUT_DAYS = 6;

        WriteTask(BlockingQueue<String> blockingQueue) {
            mBlockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            try {
                while (logEnable) {
                    //等待20s，没有则结束线程
                    String content = mBlockingQueue.poll(20, TimeUnit.SECONDS);
                    if (content == null) {
                        interrupt();
                        break;
                    }
                    writeTrack(content);
                    if (System.currentTimeMillis() - mCheckCleanLog > CHECK_CLEAN_LOG_DELAY) {
                        mCheckCleanLog = System.currentTimeMillis();
                        cleanLogDir();
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }

        private void writeTrack(String content) {
            String filePath = getLogFilePath(mDateFormat.format(new Date()));
            File file = new File(filePath);
            makeFilePath(file);
            FileWriter writer = null;
            BufferedWriter bufWriter = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                writer = new FileWriter(file, true);
                bufWriter = new BufferedWriter(writer);
                bufWriter.write(content);
                bufWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    if (bufWriter != null) {
                        bufWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cleanLogDir() {
            File root = new File(getCachePath());
            String today = mDateFormat.format(new Date());
            if (root.exists()) {
                File[] files = root.listFiles();
                if (files == null || files.length == 0) return;
                String tmpDate;
                File tmpFile;
                for (File tempFile : files) {
                    tmpFile = tempFile;
                    try {
                        tmpDate = getDate(tmpFile.getName());
                        if (tmpDate == null) continue;
                        if (!computeTwoDaysWithInSpecified(tmpDate, today)) {
                            // 如果存储的文件名的时间戳和今天的间隔在7天之外，则删除文件夹
                            delete(tempFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        /**
         * 通过给定的字符串获取对应的日期
         */
        private String getDate(String content) {

            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }

        private boolean computeTwoDaysWithInSpecified(String startStr, String endStr) {
            Date startDate = transDate(startStr);
            Date endDate = transDate(endStr);
            if (startDate == null || endDate == null) {
                return false; // 日期格式错误，判断不在范围内
            }
            long timeLong = endDate.getTime() - startDate.getTime();
            int dayInterval = (int) (timeLong / 1000 / 60 / 60 / 24);
            return dayInterval >= 0 && dayInterval <= WriteTask.TIMEOUT_DAYS;
        }

        private Date transDate(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            Date date = null;
            try {
                date = mDateFormat.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }

        private void delete(File file) {
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                file.delete();
                return;
            }
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (File theFile : childFiles) {
                delete(theFile);
            }
            file.delete();
        }
    }
}
