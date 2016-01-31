package edu.fe.backend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

import bolts.Continuation;
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

    public Date getCreationDate() {
        return getDate("creationDate");
    }

    public void setCreationDate(Date date) {
        put("creationDate", date);
    }

    public Date getExpirationDate() {
        return getDate("expirationDate");
    }

    public void setExpirationDate(Date date) {
        put("expirationDate", date);
    }

    public Bitmap getImage() throws ParseException {
        final ParseFile file = getParseFile("image");
        byte[] buffer = file.getData();
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    public ParseFile getImageLazy() {
        return getParseFile("image");
    }

    public Task<Bitmap> getImageInBackground() {
        final ParseFile file = getParseFile("image");
        Task<Bitmap> bitmapTask = file.getDataInBackground().onSuccess(new Continuation<byte[], Bitmap>() {
            @Override
            public Bitmap then(Task<byte[]> task) throws Exception {
                return BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
            }
        });
        return bitmapTask;
    }

}
