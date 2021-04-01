package com.example.mytestapp.utils.disklog;

import android.annotation.SuppressLint;
import android.util.Log;


import com.example.mytestapp.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2019-08-13 09:32
 */
class DiskLogTree {
    private static final int MAX_TAG_LENGTH = 25;
    private static final int CALL_STACK_INDEX = 4;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
    private final ThreadLocal<String> explicitTag = new ThreadLocal<>();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    DiskLogTree() {
    }

    private String createStackElementTag(StackTraceElement element) {
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        return tag.length() > MAX_TAG_LENGTH ? tag.substring(0, MAX_TAG_LENGTH) : tag;
    }

    String getTag() {
        String tag = explicitTag.get();
        if (tag != null) {
            explicitTag.remove();
        }
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
    }

    /**
     * Log a verbose message with optional format args.
     */
    public void v(String message, Object... args) {
        prepareLog(Log.VERBOSE, null, message, args);
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    public void v(Throwable t, String message, Object... args) {
        prepareLog(Log.VERBOSE, t, message, args);
    }

    /**
     * Log a verbose exception.
     */
    public void v(Throwable t) {
        prepareLog(Log.VERBOSE, t, null);
    }

    /**
     * Log a debug message with optional format args.
     */
    public void d(String message, Object... args) {
        prepareLog(Log.DEBUG, null, message, args);
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    public void d(Throwable t, String message, Object... args) {
        prepareLog(Log.DEBUG, t, message, args);
    }

    /**
     * Log a debug exception.
     */
    public void d(Throwable t) {
        prepareLog(Log.DEBUG, t, null);
    }

    /**
     * Log an info message with optional format args.
     */
    public void i(String message, Object... args) {
        prepareLog(Log.INFO, null, message, args);
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    public void i(Throwable t, String message, Object... args) {
        prepareLog(Log.INFO, t, message, args);
    }

    /**
     * Log an info exception.
     */
    public void i(Throwable t) {
        prepareLog(Log.INFO, t, null);
    }

    /**
     * Log a warning message with optional format args.
     */
    public void w(String message, Object... args) {
        prepareLog(Log.WARN, null, message, args);
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    public void w(Throwable t, String message, Object... args) {
        prepareLog(Log.WARN, t, message, args);
    }

    /**
     * Log a warning exception.
     */
    public void w(Throwable t) {
        prepareLog(Log.WARN, t, null);
    }

    /**
     * Log an error message with optional format args.
     */
    public void e(String message, Object... args) {
        prepareLog(Log.ERROR, null, message, args);
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    public void e(Throwable t, String message, Object... args) {
        prepareLog(Log.ERROR, t, message, args);
    }

    /**
     * Log an error exception.
     */
    public void e(Throwable t) {
        prepareLog(Log.ERROR, t, null);
    }

    /**
     * Return whether a message at {@code priority} or {@code tag} should be logged.
     */
    private boolean isLoggable(String tag, int priority) {
        return true;
    }

    private String getStackTraceString(Throwable t) {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * Formats a log message with optional arguments.
     */
    private String formatMessage(String message, Object[] args) {
        return String.format(message, args);
    }

    private void prepareLog(int priority, Throwable t, String message, Object... args) {
        // Consume tag even when message is not loggable so that next message is correctly tagged.
        String tag = getTag();

        if (!isLoggable(tag, priority)) {
            return;
        }
        if (message != null && message.length() == 0) {
            message = null;
        }
        if (message == null) {
            if (t == null) {
                return; // Swallow message if it's null and there's no throwable.
            }
            message = getStackTraceString(t);
        } else {
            if (args.length > 0) {
                message = formatMessage(message, args);
            }
            if (t != null) {
                message += "\n" + getStackTraceString(t);
            }
        }
        if (BuildConfig.openDebug) {
            Log.println(priority, tag, message);
        }
        log(priority, tag, message);
    }

    private String getPriorityString(int priority) {
        switch (priority) {
            case Log.VERBOSE:
                return "DV";
            case Log.DEBUG:
                return "DD";
            case Log.INFO:
                return "DI";
            case Log.WARN:
                return "DW";
            case Log.ERROR:
                return "DE";
            case Log.ASSERT:
                return "DA";
            default:
                return "DU";
        }
    }

    private void log(int priority, String tag, String message) {
        if (DiskLogManager.getInstance() == null) {
            if (BuildConfig.DEBUG) {
                Log.d("DiskLogTree", "Waiting init");
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(mDateFormat.format(new Date()))
                .append(" ")
                .append(getPriorityString(priority))
                .append("/")
                .append(tag)
                .append(": ")
                .append(message)
                .append("\n");
        DiskLogManager.getInstance().log(stringBuilder.toString());
    }


}
