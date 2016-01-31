package edu.fe;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.vorph.anim.AnimUtils;

import java.util.List;
import java.util.Random;

import edu.fe.ItemListFragment.OnListFragmentInteractionListener;
import edu.fe.util.FoodItem;
import edu.fe.util.ResUtils;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FoodItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
@Deprecated
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {

    private final List<FoodItem> mFoodList;
    private final OnListFragmentInteractionListener mListener;
    private final Random mRandom;
    private final Context mContext;

    private static final int ANIM_DELAY_INCREMENTER = 200;
    private static final int ANIM_DURATION = 400;

    public ItemRecyclerAdapter
            (final List<FoodItem> items,
             final OnListFragmentInteractionListener listener,
             final Context context)
    {
        mFoodList = items;
        mListener = listener;
        mRandom = new Random(System.nanoTime());
        mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View rootView =
                LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.entity_food_item, parent, false);
        return new ItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        FoodItem item = mFoodList.get(position);
        holder.foodItem = item;

        // use sample images for now:
        final List<Bitmap> imageList = ResUtils.LOADED_BITMAPS;
        final int maxSize = imageList.size();
        holder.imageView.setImageBitmap(imageList.get(mRandom.nextInt(maxSize)));

        // Load all information to card
        holder.nameView.setText(item.getHeaderText());
        holder.expirationView.setText(item.getHeaderText2());
        holder.extraInfoView.setText(item.getHeaderText3());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Calling list-click-listener");
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.foodItem);
                }
            }
        });

        if (!holder.isDisplayed) {
            holder.isDisplayed = true;
            holder.view.setVisibility(View.INVISIBLE);

            // Card/item is ready to be displayed to screen: load animation
            AnimUtils.fadeIn(mContext)
                    .delay(ANIM_DELAY_INCREMENTER)
                    .duration(ANIM_DURATION)
                    .view(holder.view)
                    .listener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            holder.view.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    })
                    .start();
        }
    }

    // Must be implemented to get end of list.
    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    /**
     * Add to the list of fooditems without having to use the list.
     * Remember to call notifyDataSetHasChanged afterwards.
     * @param item - the item to be added to the list.
     */
    public void addItem(FoodItem item) {
        mFoodList.add(item);
    }


    // View holder holding the contents of an item in the list.
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // Layout containing all items.
        // This is a CardView in the xml file.
        public final View view;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView expirationView;
        public final TextView extraInfoView;
        public FoodItem foodItem;

        // Makes it so that it does not fade the item in again
        public boolean isDisplayed = false;

        public ItemViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.food_image);
            this.nameView = (TextView) view.findViewById(R.id.header);
            this.expirationView = (TextView) view.findViewById(R.id.header_2);
            this.extraInfoView = (TextView) view.findViewById(R.id.header_3);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
