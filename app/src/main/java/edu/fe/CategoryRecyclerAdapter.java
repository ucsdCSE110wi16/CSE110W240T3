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

import com.parse.ParseImageView;
import com.parse.ParseQueryAdapter;

import bolts.Continuation;
import bolts.Task;
import edu.fe.backend.Category;

/**
 * Created by david on 2/8/2016.
 */
public class CategoryRecyclerAdapter extends ParseRecyclerQueryAdapter<Category, CategoryRecyclerAdapter.CategoryViewHolder> {

    private final Context mContext;
    private final CategoryListFragment.OnCategorySelectedHandler mListener;

    public CategoryRecyclerAdapter(ParseQueryAdapter.QueryFactory<Category> factory,
                                   boolean hasStableIds,
                                   CategoryListFragment.OnCategorySelectedHandler listener,
                                   final Context context) {
        super(factory, hasStableIds);
        mListener = listener;
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
        // TODO
        //holder.thumbnailView.setPlaceholder()
        holder.thumbnailView.setParseFile(category.getThumbnailLazy());
        holder.thumbnailView.loadInBackground();

        String name = category.getName();
        String description = category.getDescription();

        holder.nameView.setText(name != null ? name : "");
        holder.descriptionView.setText(description != null ? description : "");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Calling list-click-listener");
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCategorySelected(holder.category);
                }
            }
        });
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // Layout containing all items.
        // This is a CardView in the xml file.
        public final View view;
        public final ParseImageView thumbnailView;
        public final TextView nameView;
        public final TextView descriptionView;
        public Category category;

        // Makes it so that it does not fade the item in again
        public boolean isDisplayed = false;

        public CategoryViewHolder(View view) {
            super(view);
            this.view = view;
            this.thumbnailView = (ParseImageView) view.findViewById(R.id.category_thumbnail);
            this.nameView = (TextView) view.findViewById(R.id.category_name);
            this.descriptionView = (TextView) view.findViewById(R.id.category_description);
        }
    }
}
