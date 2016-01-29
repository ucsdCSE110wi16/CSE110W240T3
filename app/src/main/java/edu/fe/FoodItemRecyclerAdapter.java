package edu.fe;

import android.view.ViewGroup;

import com.parse.ParseQueryAdapter;

import edu.fe.backend.FoodItem;

/**
 * Created by david on 1/29/2016.
 */
public class FoodItemRecyclerAdapter extends ParseRecyclerQueryAdapter<FoodItem, ItemRecyclerAdapter.ItemViewHolder> {

    public FoodItemRecyclerAdapter(ParseQueryAdapter.QueryFactory<FoodItem> factory, boolean hasStableIds) {
        super(factory, hasStableIds);
    }

    @Override
    public ItemRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ItemRecyclerAdapter.ItemViewHolder holder, int position) {

    }
}
