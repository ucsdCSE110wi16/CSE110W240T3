package edu.fe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.fe.backend.RecipeFinder;
import edu.fe.backend.RecipeItem;

public class RecipeListFragment extends Fragment {

    private RecipeItemRecyclerAdapter adapter;

    public RecipeListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        RecipeItem[] recipeList;
        boolean errorFlag = false;
        try {
            RecipeFinder recipeFinder = new RecipeFinder();
            recipeList = recipeFinder.getRecipes(1);
        }
        catch(Exception e) {
            recipeList = new RecipeItem[1];
            errorFlag = true;
        }

        Log.d("DEBUG", "After try/catch");
        if(!errorFlag) {

            Log.d("DEBUG", "No errors");
            for (RecipeItem r : recipeList) {
                Log.d("DEBUG", r.getName());
            }

            adapter = new RecipeItemRecyclerAdapter(recipeList);
            if (view instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setAdapter(adapter);
            }
        }
        return view;
    }



}