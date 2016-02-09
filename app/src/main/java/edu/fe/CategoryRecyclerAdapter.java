package edu.fe;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Random;

import bolts.Continuation;
import bolts.Task;
import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;

/**
 * Created by david on 2/8/2016.
 */
public class CategoryRecyclerAdapter extends ParseRecyclerQueryAdapter<Category, CategoryRecyclerAdapter.CategoryViewHolder> {

    private final Context mContext;

    public CategoryRecyclerAdapter(ParseQueryAdapter.QueryFactory<Category> factory,
                                   boolean hasStableIds,
                                   final Context context) {
        super(factory, hasStableIds);
        mContext = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_category,
                                            parent, false);
        return new CategoryViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        Category category = getItem(position);
        holder.category = category;
        holder.thumbnailView.setImageResource(0);
        category.getThumbnailInBackground().onSuccess(new Continuation<Bitmap, Object>() {
            @Override
            public Object then(Task<Bitmap> task) throws Exception {
                holder.thumbnailView.setImageBitmap(task.getResult());
                return null;
            }
        });

        String name = category.getName();
        String description = category.getDescription();

        holder.nameView.setText(name != null ? name : "");
        holder.descriptionView.setText(description != null ? description : "");
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // Layout containing all items.
        // This is a CardView in the xml file.
        public final View view;
        public final ImageView thumbnailView;
        public final TextView nameView;
        public final TextView descriptionView;
        public Category category;

        // Makes it so that it does not fade the item in again
        public boolean isDisplayed = false;

        public CategoryViewHolder(View view) {
            super(view);
            this.view = view;
            this.thumbnailView = (ImageView) view.findViewById(R.id.category_thumbnail);
            this.nameView = (TextView) view.findViewById(R.id.category_name);
            this.descriptionView = (TextView) view.findViewById(R.id.category_description);
        }
    }
}
