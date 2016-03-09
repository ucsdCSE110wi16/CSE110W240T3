package edu.fe.backend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import edu.fe.R;

/**
 * Created by david on 1/29/2016.
 */
public class Category {
    private String mName;
    private int mThumbnailResId;

    private static List<Category> categories;

    public static List<Category> getCategories() {
        if(categories == null) {
            categories = new ArrayList<>();
            categories.add(new Category("Fruits",R.drawable.fruits1));
            categories.add(new Category("Vegetables", R.drawable.vegetables1));
            categories.add(new Category("Dairy", R.drawable.dairy1));
            categories.add(new Category("Meat", R.drawable.meat1));
            categories.add(new Category("Alcohol", R.drawable.alcohol1));
            categories.add(new Category("Sugars", R.drawable.sweets1));
            categories.add(new Category("Beverages", R.drawable.beverages1));
            categories.add(new Category("Condiments", R.drawable.condiments1));
            categories.add(new Category("Grains/Carbs", R.drawable.carbs1));
            categories.add(new Category("Fish", R.drawable.fish1));
            categories.add(new Category("Miscellaneous", R.drawable.misc1));
        }

        return categories;
    }

    protected Category(String name, int resId) {
        mName = name;
        mThumbnailResId = resId;
    }

    public static Category getCategoryByName(String name) {
        for(Category c : categories) {
            if(c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return mName;
    }

    public int getThumbnailResId() {
        return mThumbnailResId;
    }
}
