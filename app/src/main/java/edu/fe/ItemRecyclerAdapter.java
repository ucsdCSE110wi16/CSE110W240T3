package edu.fe;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.fe.ItemListFragment.OnListFragmentInteractionListener;
import edu.fe.util.FoodItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FoodItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {

    private final List<FoodItem> mFoodList;
    private final OnListFragmentInteractionListener mListener;

    public ItemRecyclerAdapter(List<FoodItem> items, OnListFragmentInteractionListener listener) {
        mFoodList = items;
        mListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView =
                LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.entity_food_item, parent, false);
        return new ItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        FoodItem item = mFoodList.get(position);

        holder.foodItem = item;
        if (item.getImage() != null)
            holder.imageView.setImageBitmap(item.getImage());
        holder.nameView.setText(item.getName());

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
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    public void addItem(FoodItem item) {
        mFoodList.add(item);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public final ImageView imageView;
        public final TextView nameView;
        public final TextView expirationView;

        public FoodItem foodItem;

        public ItemViewHolder(View view) {
            super(view);
            this.view = view;
            nameView = (TextView) view.findViewById(R.id.food_name);
            imageView = (ImageView) view.findViewById(R.id.food_image);
            expirationView = (TextView) view.findViewById(R.id.food_expiration_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
