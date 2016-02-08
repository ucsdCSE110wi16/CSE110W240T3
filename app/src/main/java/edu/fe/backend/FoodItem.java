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
    public final static String NAME = "name";
    public final static String DESCRIPTION = "description";
    public final static String CATEGORY = "category";
    public final static String CREATION_DATE = "creationDate";
    public final static String EXPIRATION_DATE = "expirationDate";
    public final static String IMAGE = "image";

    public FoodItem() {

    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public Task<Category> getCategoryInBackground() {
        return getParseObject(CATEGORY).fetchInBackground();
    }

    public Category getCategoryLazy() {
        return (Category)getParseObject(CATEGORY);
    }

    public Category getCategory() throws ParseException {
        Category o = (Category)getParseObject(CATEGORY);
        o.fetchIfNeeded();
        return o;
    }

    public Date getCreationDate() {
        return getDate(CREATION_DATE);
    }

    public void setCreationDate(Date date) {
        put(CREATION_DATE, date);
    }

    public Date getExpirationDate() {
        return getDate(EXPIRATION_DATE);
    }

    public void setExpirationDate(Date date) {
        put(EXPIRATION_DATE, date);
    }

    public Bitmap getImage() throws ParseException {
        final ParseFile file = getParseFile(IMAGE);
        byte[] buffer = file.getData();
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    public ParseFile getImageLazy() {
        return getParseFile(IMAGE);
    }

    public Task<Bitmap> getImageInBackground() {
        final ParseFile file = getParseFile(IMAGE);
        if(file == null) {
            return Task.forError(new ParseException(ParseException.OBJECT_NOT_FOUND, "image field is null"));
        }
        Task<Bitmap> bitmapTask = file.getDataInBackground().onSuccess(new Continuation<byte[], Bitmap>() {
            @Override
            public Bitmap then(Task<byte[]> task) throws Exception {
                return BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
            }
        });
        return bitmapTask;
    }

}