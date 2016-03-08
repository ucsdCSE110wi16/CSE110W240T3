package edu.fe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.fe.backend.RecipeItem;

/**
 * Created by wongk on 3/2/2016.
 */
public class RecipeItemRecyclerAdapter extends RecyclerView.Adapter<RecipeItemRecyclerAdapter.RecipeViewHolder> {

    private RecipeItem[] recipeList;

    public RecipeItemRecyclerAdapter(RecipeItem[] recList) {
        recipeList = recList;
    }

    @Override
    public int getItemCount() {
        return recipeList.length;
    }

    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int i) {
        RecipeItem recipe = recipeList[i];
        recipeViewHolder.vName.setText(recipe.getName());
        recipeViewHolder.vUrl.setText(recipe.getUrl());
        recipeViewHolder.vCookingTime.setText(recipe.getCookingTime());
        recipeViewHolder.vMissIngs.setText(recipe.getMissingIngredients());
    }

    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                            .from(viewGroup.getContext())
                            .inflate(R.layout.entity_recipe_item, viewGroup, false);

        return new RecipeViewHolder(itemView);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vMissIngs;
        protected TextView vUrl;
        protected TextView vCookingTime;

        public RecipeViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.txtName);
            vMissIngs = (TextView) v.findViewById(R.id.txtMissingIngredients);
            vUrl = (TextView) v.findViewById(R.id.txtUrl);
            vCookingTime = (TextView) v.findViewById(R.id.txtCookingTime);

        }
    }
}
