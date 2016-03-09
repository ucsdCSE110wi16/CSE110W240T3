package edu.fe;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import edu.fe.backend.Category;

/**
 * Created by david on 2/17/2016.
 */
public class SpinAdapter extends ArrayAdapter<String> {
    final Activity mActivity;

    public SpinAdapter(Activity activity, int resource) {
        super(activity.getApplicationContext(), resource);
        this.mActivity = activity;
    }


    @Override
    public int getCount() {
        return Category.getCategories().size();
    }

    @Override
    public String getItem(int position) {
        return Category.getCategories().get(position).getName();
    }

    public Category getCategory(int position) {
        return Category.getCategories().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
