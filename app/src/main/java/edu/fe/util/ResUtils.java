package edu.fe.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

import edu.fe.R;

public class ResUtils {

    public static int getPrimaryColor(Context context) {
        return fetchColor(context, R.attr.colorPrimary);
    }

    public static int getPrimaryDarkColor(Context context) {
        return fetchColor(context, R.attr.colorPrimaryDark);
    }

    public static int getPrimaryTextColor(Context context) {
        return fetchColor(context, android.R.attr.textColorPrimary);
    }

    public static int getSecondaryTextColor(Context context) {
        return fetchColor(context, android.R.attr.textColorSecondary);
    }

    public static int fetchColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { attr });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    // Calculated Required sampled image
    public static int calculateInSampleSize
        (BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // Turn large bitmaps into efficient ones that won't take up too much memory
    public static Bitmap decodeSampledBitmapFromResource
        (Resources res, int resId, int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }






    public static final List<Bitmap> LOADED_BITMAPS = new ArrayList<>();
    public static void initialize(Context context) {
        InitResources(context);
    }

    // Load material wallpapers into a list from drawable folder.
    private static void InitResources(Context context) {
        final int DP_WIDTH_SIZE = 128;
        final int DP_HEIGHT_SIZE = 96;

        final Resources res = context.getResources();
        final Bitmap one = decodeSampledBitmapFromResource
                (res, R.drawable.wallpaper_1, DP_WIDTH_SIZE, DP_HEIGHT_SIZE);
        final Bitmap two = decodeSampledBitmapFromResource
                (res, R.drawable.wallpaper_2, DP_WIDTH_SIZE, DP_HEIGHT_SIZE);
        final Bitmap three = decodeSampledBitmapFromResource
                (res, R.drawable.wallpaper_3, DP_WIDTH_SIZE, DP_HEIGHT_SIZE);
        final Bitmap four = decodeSampledBitmapFromResource
                (res, R.drawable.wallpaper_4, DP_WIDTH_SIZE, DP_HEIGHT_SIZE);

        LOADED_BITMAPS.add(one);
        LOADED_BITMAPS.add(two);
        LOADED_BITMAPS.add(three);
        LOADED_BITMAPS.add(four);
    }

















}
