package edu.fe;

import android.content.Context;
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
    private final List<String> categories = new ArrayList<String>();

    public SpinAdapter(Context context, int resource) {
        super(context, resource);
        loadCategories();
    }

    private void loadCategories() {
        ParseQuery<Category> q = new ParseQuery<Category>(Category.class);
        q.findInBackground().onSuccess(new Continuation<List<Category>, Object>() {
            @Override
            public Object then(Task<List<Category>> task) throws Exception {
                categories.clear();
                for (Category c : task.getResult()) {
                    categories.add(c.getName());
                }
                notifyDataSetChanged();
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
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
