package edu.fe;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

import edu.fe.backend.Recipe;

/**
 * Created by wongk on 3/2/2016.
 */
public class RecipeItemRecyclerAdapter extends RecyclerView.Adapter<RecipeItemRecyclerAdapter.RecipeViewHolder> {

    private Recipe.Item[] mRecipeItems;
    private RecipeListFragment.OnRecipeSelectedHandler mListener;

    public RecipeItemRecyclerAdapter(Recipe.Item[] items, RecipeListFragment.OnRecipeSelectedHandler listener) {
        if (items == null) items = new Recipe.Item[0];
        setRecipeList(items);
        mListener = listener;
    }

    public void setRecipeList(Recipe.Item[] items) {
        mRecipeItems = items;
    }

    @Override
    public int getItemCount() {
        return mRecipeItems.length;
    }

    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int i) {
        final Recipe.Item recipe = mRecipeItems[i];
        recipeViewHolder.fat.setText(recipe.fat);
        recipeViewHolder.name.setText(recipe.name);
        recipeViewHolder.cookingTime.setText(recipe.cookingTime);
        recipeViewHolder.missingIngredients.setText(recipe.missingIngredients);
        recipeViewHolder.image.setImageBitmap(recipe.image);
        recipeViewHolder.calories.setText(recipe.calories);

        recipeViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Calling list-click-listener");
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onRecipeSelected(recipe);
                }
                else {
                    Log.d("DEBUG", "mListener is null");
                }
            }
        });

    }

    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                            .from(viewGroup.getContext())
                            .inflate(R.layout.entity_recipe_item, viewGroup, false);

        return new RecipeViewHolder(itemView);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView missingIngredients;
        public TextView fat;
        public TextView cookingTime;
        public TextView calories;
        public ImageView image;
        public View view;

        public RecipeViewHolder(View v) {
            super(v);
            view = v;
            fat = (TextView) v.findViewById(R.id.txtFat);
            name = (TextView) v.findViewById(R.id.txtName);
            missingIngredients = (TextView) v.findViewById(R.id.txtMissingIngredients);
            cookingTime = (TextView) v.findViewById(R.id.txtCookingTime);
            image = (ImageView) v.findViewById(R.id.imgRecipe);
            calories = (TextView) v.findViewById(R.id.txtCalories);

        }
    }
}
