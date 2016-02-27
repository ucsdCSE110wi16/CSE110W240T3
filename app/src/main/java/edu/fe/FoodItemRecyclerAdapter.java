package edu.fe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.parse.ParseQueryAdapter;

import java.util.Random;

import edu.fe.backend.FoodItem;

/**
 * Created by david on 1/29/2016.
 */
public class FoodItemRecyclerAdapter extends ParseRecyclerQueryAdapter<FoodItem, FoodItemRecyclerAdapter.FoodItemViewHolder> {

    private final ItemListFragment.OnListFragmentInteractionListener mListener;
    private final Random mRandom = new Random();
    private final Context mContext;

    private static final int ANIM_DELAY_INCREMENTER = 200;
    private static final int ANIM_DURATION = 400;


    public FoodItemRecyclerAdapter(ParseQueryAdapter.QueryFactory<FoodItem> factory,
                                   boolean hasStableIds,
                                   final ItemListFragment.OnListFragmentInteractionListener listener,
                                   final Context context
                                   ) {
        super(factory, hasStableIds);
        mListener = listener;
        mContext = context;
    }

    @Override
    public FoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.entity_food_item, parent, false);
        return new FoodItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final FoodItemViewHolder holder, int position) {
        FoodItem item = getItem(position);
        holder.foodItem = item;
        holder.imageView.setParseFile(item.getImageLazy());
        holder.imageView.loadInBackground();
        holder.nameView.setText(item.getName());
        if(item.getExpirationDate() != null)
            holder.expirationView.setText(item.getExpirationDate().toString());
        holder.extraInfoView.setText("Extra Info");


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
    /*
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
        */
    }

    /**
     * Created by davidzech on 1/30/16.
     */
    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        // Layout containing all items.
        // This is a CardView in the xml file.
        public final View view;
        public final ParseImageView imageView;
        public final TextView nameView;
        public final TextView expirationView;
        public final TextView extraInfoView;
        public edu.fe.backend.FoodItem foodItem;

        // Makes it so that it does not fade the item in again
        public boolean isDisplayed = false;

        public FoodItemViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (ParseImageView) view.findViewById(R.id.food_image);
            this.nameView = (TextView) view.findViewById(R.id.header);
            this.expirationView = (TextView) view.findViewById(R.id.header_2);
            this.extraInfoView = (TextView) view.findViewById(R.id.header_3);
        }

    }
}
