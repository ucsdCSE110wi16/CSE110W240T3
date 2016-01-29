package edu.fe;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

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
    }
}
