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
    private final List<Category> categories = new ArrayList<Category>();

    final Activity mActivity;

    public SpinAdapter(Activity activity, int resource) {
        super(activity.getApplicationContext(), resource);
        this.mActivity = activity;
        this.loadCategories();
    }

    private void loadCategories() {
        ParseQuery<Category> q = new ParseQuery<Category>(Category.class);
        q.findInBackground().onSuccess(new Continuation<List<Category>, Object>() {
            @Override
            public Object then(Task<List<Category>> task) throws Exception {
                categories.clear();
                categories.addAll(task.getResult());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SpinAdapter.this.notifyDataSetChanged();
                    }
                });
                return null;
            }
        });
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public String getItem(int position) {
        return categories.get(position).getName();
    }

    public Category getCategory(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
