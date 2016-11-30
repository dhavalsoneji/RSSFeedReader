package com.dhavalsoneji.rssfeedreader.utils;

import android.util.Log;

public class Applog {
    public static boolean DEBUG = true;

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (DEBUG) {
            Log.e(tag, message, e);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void wtf(String tag, String message) {
        if (DEBUG) {
            Log.wtf(tag, message);
        }
    }

    public static void wtf(String tag, String message, Exception e) {
        if (DEBUG) {
            Log.wtf(tag, message, e);
        }
    }

    public static void e(String tag, int value) {
        Applog.e(tag, String.valueOf(value));
    }

    public static void e(String tag, float value) {
        Applog.e(tag, String.valueOf(value));
    }

    public static void e(String tag, double value) {
        Applog.e(tag, String.valueOf(value));
    }

    public static void e(String tag, boolean value) {
        Applog.e(tag, String.valueOf(value));
    }

    public static void w(String tag, int value) {
        Applog.w(tag, String.valueOf(value));
    }

    public static void w(String tag, float value) {
        Applog.w(tag, String.valueOf(value));
    }

    public static void w(String tag, double value) {
        Applog.w(tag, String.valueOf(value));
    }

    public static void w(String tag, boolean value) {
        Applog.w(tag, String.valueOf(value));
    }

    public static void d(String tag, int value) {
        Applog.d(tag, String.valueOf(value));
    }

    public static void d(String tag, float value) {
        Applog.d(tag, String.valueOf(value));
    }

    public static void d(String tag, double value) {
        Applog.d(tag, String.valueOf(value));
    }

    public static void d(String tag, boolean value) {
        Applog.d(tag, String.valueOf(value));
    }

    public static void i(String tag, int value) {
        Applog.i(tag, String.valueOf(value));
    }

    public static void i(String tag, float value) {
        Applog.i(tag, String.valueOf(value));
    }

    public static void i(String tag, double value) {
        Applog.i(tag, String.valueOf(value));
    }

    public static void i(String tag, boolean value) {
        Applog.i(tag, String.valueOf(value));
    }

    public static void wtf(String tag, int value) {
        Applog.wtf(tag, String.valueOf(value));
    }

    public static void wtf(String tag, float value) {
        Applog.wtf(tag, String.valueOf(value));
    }

    public static void wtf(String tag, double value) {
        Applog.wtf(tag, String.valueOf(value));
    }

    public static void wtf(String tag, boolean value) {
        Applog.wtf(tag, String.valueOf(value));
    }

    public static void enableLogging() {
        DEBUG = true;
    }
}
