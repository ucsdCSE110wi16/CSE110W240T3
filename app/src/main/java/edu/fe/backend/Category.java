package edu.fe.backend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by david on 1/29/2016.
 */
@ParseClassName("Category")
public class Category extends ParseObject {

    public final static String THUMBNAIL = "thumbnail";
    public final static String NAME = "name";
    public final static String DESCRIPTION = "description";


    public Category() {

    }

    public Bitmap getImage() throws ParseException {
        final ParseFile file = getParseFile(THUMBNAIL);
        byte[] buffer = file.getData();
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    public ParseFile getThumbnailLazy() {
        return getParseFile(THUMBNAIL);
    }

    public Task<Bitmap> getThumbnailInBackground() {
        final ParseFile file = getParseFile(THUMBNAIL);
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

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public void setDescription(String description) {
        put(DESCRIPTION, description);
    }
}
