package edu.fe.backend;

import java.util.ArrayList;
import java.util.List;

import edu.fe.R;

/**
 * Created by david on 1/29/2016.
 */
public class Category {
    private String mName;
    private int mThumbnailResId;

    public int colorId;
    public int colorDarkId;

    private static List<Category> categories;

    public static List<Category> getCategories() {
        if(categories == null) {
            categories = new ArrayList<>();
            categories.add(new Category("Fruits", R.drawable.fruit, R.color.green_300, R.color.green_500));
            categories.add(new Category("Vegetables", R.drawable.vegetables, R.color.green_500, R.color.green_700));
            categories.add(new Category("Dairy", R.drawable.dairy_2, R.color.grey_400, R.color.grey_500));
            categories.add(new Category("Meat", R.drawable.meat, R.color.red_300, R.color.red_500));
            categories.add(new Category("Alcohol", R.drawable.alcohol, R.color.brown_200, R.color.brown_500));
            categories.add(new Category("Sugars", R.drawable.sweets, R.color.cyan_300, R.color.cyan_500));
            categories.add(new Category("Beverages", R.drawable.coffee, R.color.brown_400, R.color.brown_600));
            categories.add(new Category("Condiments", R.drawable.condiments, R.color.teal_400, R.color.teal_600));
            categories.add(new Category("Grains/Carbs", R.drawable.carbs, R.color.grey_400, R.color.grey_600));
            categories.add(new Category("Fish", R.drawable.fish, R.color.indigo_400, R.color.indigo_600));
            categories.add(new Category("Miscellaneous", R.drawable.misc, R.color.grey_600, R.color.grey_800));
        }

        return categories;
    }

    public static String[] getCategoryNames() {
        List<Category> cats = getCategories();
        String[] result = new String[cats.size()];
        for(int i = 0; i < cats.size(); ++i) {
            result[i] = cats.get(i).getName();
        }
        return result;
    }

    protected Category(String name, int resId, int colorId, int colorDarkId) {
        mName = name;
        mThumbnailResId = resId;
        this.colorId = colorId;
        this.colorDarkId = colorDarkId;
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
