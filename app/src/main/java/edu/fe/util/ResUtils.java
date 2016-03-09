package edu.fe.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.TypedValue;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.fe.R;

public class ResUtils {

    final static int SEVENDAYS = 604800;
    final static int ONEDAY = 86400;
    final static int SEVENDAYS_MS = SEVENDAYS * 1000;
    final static int ONEDAY_MS = ONEDAY * 1000;

    public static void formatExpirationDateEx(Context context, TextView textView, Date date) {
        Calendar current = new GregorianCalendar();
        Calendar expiration = new GregorianCalendar();
        expiration.setTime(date);

        if(current.after(expiration)) {
            // expired already
            if(current.get(Calendar.DAY_OF_YEAR) > expiration.get(Calendar.DAY_OF_YEAR)) {
                textView.setText("Expired");
            } else {
                textView.setText("Expires today");
            }
            textView.setTextColor(getColor(context, R.color.red_500));
            return;
        }

        // check if more than 1 week
        int daysLeft = expiration.get(Calendar.DAY_OF_YEAR) - current.get(Calendar.DAY_OF_YEAR);
        if(daysLeft > 7) {
            // more than a week
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            textView.setText("Expires on " + sdf.format(date));
            textView.setTextColor(getColor(context, R.color.grey_500));
            return;
        } else if(daysLeft > 1) {
            // format days until expiration
            textView.setText("Expires in " + daysLeft + " days");
            textView.setTextColor(getColor(context, R.color.grey_500));
        } else if(daysLeft > 0) {
            textView.setText("Expires Tomorrow");
            textView.setTextColor(getColor(context, R.color.grey_500));
        } else {
            textView.setText("Expires Today");
            textView.setTextColor(getColor(context, R.color.red_300));
        }


    }

    @Deprecated
    public static void formatExpirationDate(Context context, TextView textView, Date date) {
        Date current = new Date();

        if(current.after(date)) {
            // expired already
            textView.setText("Expired");
            textView.setTextColor(getColor(context, R.color.red_500));
            return;
        }

        long delta = date.getTime() - current.getTime();
        if(delta > SEVENDAYS_MS) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            textView.setText("Expires on " + sdf.format(date));
            textView.setTextColor(getColor(context, R.color.grey_500));
            return;
        } else if (delta > ONEDAY_MS){
            // find how many days we have
            long numDays = delta / ONEDAY_MS;
            if(numDays > 1)
                textView.setText("Expires in " + numDays + " days");
            else
                textView.setText("Expires in Tomorrow");
            textView.setTextColor(getColor(context, R.color.grey_500));
            return;
        } else {

            textView.setText("Expires Today");
            textView.setTextColor(getColor(context, R.color.red_300));
        }
    }

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

    public static int getColor(Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //noinspection deprecation
            return context.getResources().getColor(colorId);
        } else {
            return context.getColor(colorId);
        }
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
