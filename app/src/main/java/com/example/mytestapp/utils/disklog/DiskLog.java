package com.example.mytestapp.utils.disklog;

/**
 * 本地存储日志打印,debug环境下会同时打印到Logcat
 *
 * @author Zhanghb
 * Email: 2573475062@qq.com
 * Date : 2019-08-13 10:37
 */
public class DiskLog {
    private static final DiskLogTree DISK_LOG = new DiskLogTree();

    /**
     * Log a verbose message with optional format args.
     */
    public static void v(String message, Object... args) {
        DISK_LOG.v(message, args);
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    public static void v(Throwable t, String message, Object... args) {
        DISK_LOG.v(t, message, args);
    }

    /**
     * Log a verbose exception.
     */
    public static void v(Throwable t) {
        DISK_LOG.v(t, null);
    }

    /**
     * Log a debug message with optional format args.
     */
    public static void d(String message, Object... args) {
        DISK_LOG.d(null, message, args);
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    public static void d(Throwable t, String message, Object... args) {
        DISK_LOG.d(t, message, args);
    }

    /**
     * Log a debug exception.
     */
    public static void d(Throwable t) {
        DISK_LOG.d(t, null);
    }

    /**
     * Log an info message with optional format args.
     */
    public static void i(String message, Object... args) {
        DISK_LOG.i(null, message, args);
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    public static void i(Throwable t, String message, Object... args) {
        DISK_LOG.i(t, message, args);
    }

    /**
     * Log an info exception.
     */
    public static void i(Throwable t) {
        DISK_LOG.i(t, null);
    }

    /**
     * Log a warning message with optional format args.
     */
    public static void w(String message, Object... args) {
        DISK_LOG.w(null, message, args);
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    public static void w(Throwable t, String message, Object... args) {
        DISK_LOG.w(t, message, args);
    }

    /**
     * Log a warning exception.
     */
    public static void w(Throwable t) {
        DISK_LOG.w(t, null);
    }

    /**
     * Log an error message with optional format args.
     */
    public static void e(String message, Object... args) {
        DISK_LOG.e(null, message, args);
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    public static void e(Throwable t, String message, Object... args) {
        DISK_LOG.e(t, message, args);
    }

    /**
     * Log an error exception.
     */
    public static void e(Throwable t) {
        DISK_LOG.e(t, null);
    }

}
