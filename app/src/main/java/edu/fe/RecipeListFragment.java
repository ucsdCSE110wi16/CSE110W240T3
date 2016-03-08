package edu.fe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.fe.backend.Recipe;

public class RecipeListFragment extends Fragment implements Recipe.OnRecipeRetrievedListener {

    private RecyclerView mRecyclerView;
    private RecipeItemRecyclerAdapter mRecipeRecyclerAdapter;

    // Used to initialize fragment
    public RecipeListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        Recipe.Finder.of(getActivity())
                    .setOnRecipeRetrieveListener(this)
                    .search();

        mRecipeRecyclerAdapter = new RecipeItemRecyclerAdapter(null);
        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setAdapter(mRecipeRecyclerAdapter);
        }
        return view;
    }


    @Override
    public void onRecipeGet(Recipe.Item[] items) {
        Log.d("DEBUG", "Found a total of " + items.length + " recipes");

        mRecipeRecyclerAdapter.setRecipeList(items);
        this.getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                mRecipeRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}