package edu.fe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.fe.backend.Recipe;

/**
 * Created by wongk on 3/2/2016.
 */
public class RecipeItemRecyclerAdapter extends RecyclerView.Adapter<RecipeItemRecyclerAdapter.RecipeViewHolder> {

    private Recipe.Item[] mRecipeItems;

    public RecipeItemRecyclerAdapter(Recipe.Item[] items) {
        if (items == null) items = new Recipe.Item[0];
        setRecipeList(items);
    }

    public void setRecipeList(Recipe.Item[] items) {
        mRecipeItems = items;
    }

    @Override
    public int getItemCount() {
        return mRecipeItems.length;
    }

    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int i) {
        Recipe.Item recipe = mRecipeItems[i];
        recipeViewHolder.uri.setText(recipe.url);
        recipeViewHolder.name.setText(recipe.name);
        recipeViewHolder.cookingTime.setText(recipe.cookingTime);
        recipeViewHolder.missingIngredients.setText(recipe.missingIngredients);
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
        public TextView uri;
        public TextView cookingTime;

        public RecipeViewHolder(View v) {
            super(v);
            uri = (TextView) v.findViewById(R.id.txtUrl);
            name = (TextView) v.findViewById(R.id.txtName);
            missingIngredients = (TextView) v.findViewById(R.id.txtMissingIngredients);
            cookingTime = (TextView) v.findViewById(R.id.txtCookingTime);

        }
    }
}
