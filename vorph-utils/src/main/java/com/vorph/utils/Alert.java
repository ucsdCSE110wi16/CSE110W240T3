package com.vorph.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Alert {
    public static void showExceptionAlert(Activity activity, String operation, Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Exception: " + e.getLocalizedMessage())
                .setTitle(operation + " failed")
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showExceptionAlert(Activity activity, Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Exception: " + e.getLocalizedMessage())
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlert(Activity activity, String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Problem: " + error)
                .setTitle("Error Reporting")
                .setPositiveButton("Dismiss", null)
                .show();
    }

    public static void toastLong(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(Context context, String text) {
        toastLong((Activity) context, text);
    }

    public static void toastShort(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String text) {
        toastShort((Activity) context, text);
    }
}
