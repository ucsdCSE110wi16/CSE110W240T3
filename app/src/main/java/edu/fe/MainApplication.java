package edu.fe;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;

/**
 * Created by david on 1/28/2016.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        ParseUser.enableAutomaticUser();
        ParseACL.setDefaultACL(new ParseACL(), true);

        ParseObject.registerSubclass(FoodItem.class);
        ParseObject.registerSubclass(Category.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("general");

    }
}
