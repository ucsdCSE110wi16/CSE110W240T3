package com.vorph.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    public final static String DEFAULT_LOG_TYPE = "DEBUG";
    private String mLogType = DEFAULT_LOG_TYPE;

    public static void Embed() {
        Embed(DEFAULT_LOG_TYPE);
    }

    public static void Embed(final String type) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(type));
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        // For debugging
        Log.d(DEFAULT_LOG_TYPE, "Message: " + e.getMessage());
        Log.d(DEFAULT_LOG_TYPE, "\nStackTrace:\n" + sw.toString());

    }

    private ExceptionHandler() {}
    private ExceptionHandler(String type) {
        mLogType = type;
    }
}
