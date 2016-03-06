package edu.fe.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.view.View;

import lib.material.dialogs.util.DialogUtils;


public class ThemeUtils {

    private static ThemeUtils instance = new ThemeUtils();
    private ThemeUtils() {}

    Resources.Theme theme;
    Context context;

    public static void setDefaultTheme(Context context) {
        instance.context = context;
        instance.theme = context.getTheme();
    }

    View view;
    public ThemeUtils(Context context) {
        this.context = context;
    }

    public ThemeUtils view(View view) {
        this.view = view;
        return this;
    }

    public ThemeUtils setColor(@ColorRes int res) {
        view.setBackgroundColor(DialogUtils.getColor(context, res));
        return this;
    }



}
