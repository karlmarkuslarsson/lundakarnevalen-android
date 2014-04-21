package se.lundakarnevalen.extern.util;

import android.util.Log;

/**
 * Created by Filip on 2014-03-29.
 */
public class Logf {
    public static void wtf(Object src, String fmt, Object... args) {
        Log.wtf(className(src), String.format(fmt, args));
    }

    public static void d(Object src, String fmt, Object... args) {
        Log.d(className(src), String.format(fmt, args));
    }

    public static void i(Object src, String fmt, Object... args) {
        Log.i(className(src), String.format(fmt, args));
    }

    private static String className(Object src) {
        return String.valueOf(src).replace("se.lundakarnevalen.extern.","");
    }

    public static void w(Object src, String fmt, Object... args) {
        Log.w(className(src), String.format(fmt, args));
    }

    public static void e(Object src, String fmt, Object... args) {
        Log.e(className(src), String.format(fmt, args));
    }

    public static void wtf(Object src, Throwable th, String fmt, Object... args) {
        Log.wtf(className(src), String.format(fmt, args), th);
    }

    public static void e(Object src, Throwable th, String fmt, Object... args) {
        Log.e(className(src), String.format(fmt, args), th);
    }
}
