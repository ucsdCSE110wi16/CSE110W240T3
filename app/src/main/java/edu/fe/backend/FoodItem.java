package edu.fe.backend;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import bolts.Task;

/**
 * Created by david on 1/29/2016.
 */
@ParseClassName("FoodItem")
public class FoodItem extends ParseObject {
    public FoodItem() {

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public Task<Category> getCategoryInBackground() {
        return getParseObject("category").fetchInBackground();
    }

    public Category getCategoryLazy() {
        return (Category)getParseObject("category");
    }

    public Category getCategory() throws ParseException {
        Category o = (Category)getParseObject("category");
        o.fetchIfNeeded();
        return o;
    }
}
