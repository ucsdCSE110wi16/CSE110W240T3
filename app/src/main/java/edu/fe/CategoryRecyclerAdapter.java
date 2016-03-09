package edu.fe;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.parse.ParseQueryAdapter;
import com.vorph.anim.AnimUtils;

import java.util.ArrayList;
import java.util.List;

import edu.fe.backend.Category;
import edu.fe.util.ResUtils;

/**
 * Created by david on 2/8/2016.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private final Context mContext;
    private final CategoryListFragment.OnCategorySelectedHandler mListener;
    private final List<View> mCategoryViewList;

    public CategoryRecyclerAdapter(CategoryListFragment.OnCategorySelectedHandler listener,
                                   final Context context) {

        mListener = listener;
        mContext = context;
        mCategoryViewList = new ArrayList<>();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_category,
                parent, false);
        return new CategoryViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        final Category category = Category.getCategories().get(position);

        holder.category = category;
        //holder.thumbnailView.setImageResource(category.getThumbnailResId());
        holder.thumbnailView.setImageBitmap(
                ResUtils.decodeSampledBitmapFromResource(mContext.getResources(), category.getThumbnailResId(),
                        136, 136));

        String name = category.getName();

        holder.nameView.setText(name != null ? name : "");
        mCategoryViewList.add(holder.view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Calling list-click-listener");
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
//                    performFadeout(holder.category, mListener);
                    mListener.onCategorySelected(category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Category.getCategories().size();
    }

    private void fadeOut
            (View view,
             int position,
             boolean callListener,
             final Category category,
             final CategoryListFragment.OnCategorySelectedHandler listener)
    {
        AnimUtils.AnimBuilder builder = AnimUtils.fadeOut(mContext)
                                                .view(view)
                                                .delay(position * 50)
                                                .duration(250);
        if (callListener) {
            builder.listener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override public void onAnimationEnd(Animation animation) {
                    listener.onCategorySelected(category);
                }
            });
        }

        view.setVisibility(View.INVISIBLE);
        builder.start();
    }

    // Unused
    private void performFadeout(Category category,
                                CategoryListFragment.OnCategorySelectedHandler listener) {
        boolean callListener = false;
        for (int i = 0; i < mCategoryViewList.size(); i++) {
            if (i == mCategoryViewList.size() - 1) callListener = true;

            View v = mCategoryViewList.get(i);
            fadeOut(v, i, callListener, category, listener);
        }
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // Layout containing all items.
        // This is a CardView in the xml file.
        public final View view;
        public final ImageView thumbnailView;
        public final TextView nameView;
        public Category category;

        // Makes it so that it does not fade the item in again
        public boolean isDisplayed = false;

        public CategoryViewHolder(View view) {
            super(view);
            this.view = view;
            this.thumbnailView = (ImageView) view.findViewById(R.id.category_thumbnail);
            this.nameView = (TextView) view.findViewById(R.id.category_name);
        }
    }
}
