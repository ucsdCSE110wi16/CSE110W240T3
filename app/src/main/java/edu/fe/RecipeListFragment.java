package edu.fe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vorph.utils.Alert;

import edu.fe.backend.Recipe;
import edu.fe.widget.LoadingDialog;
import lib.material.dialogs.DialogAction;
import lib.material.dialogs.MaterialDialog;

public class RecipeListFragment extends Fragment
                implements
                    Recipe.OnRecipeRetrievedListener,
                    Recipe.OnUpdateQueryListener
{


    private LoadingDialog mLoadingDialog;
    private MaterialDialog mDialog;
    private RecyclerView mRecyclerView;
    private RecipeItemRecyclerAdapter mRecipeRecyclerAdapter;
    private Recipe.Finder.ParsingTask mParsingTask;

    // Used to initialize fragment
    public RecipeListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mParsingTask = Recipe.Finder.of(getActivity())
                                    .setOnRecipeRetrieveListener(this)
                                    .search();
        mLoadingDialog = (LoadingDialog) LayoutInflater
                            .from(getActivity())
                            .inflate(R.layout.view_loading_dialog, container, false);

        mRecipeRecyclerAdapter = new RecipeItemRecyclerAdapter(null);
        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setAdapter(mRecipeRecyclerAdapter);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDialog = new MaterialDialog.Builder(getActivity())
                .customView(mLoadingDialog, false)
                .negativeText("cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (mParsingTask != null) {
                            boolean mayInterruptIfRunning = true;
                            mParsingTask.cancel(mayInterruptIfRunning);
                            Log.d("DEBUG", "Canceling recipe task");
//                            if (mParsingTask.getStatus() == AsyncTask.Status.RUNNING
//                                    || mParsingTask.getStatus() == AsyncTask.Status.PENDING) {
//
//                            }
                        }
                    }
                })
                .show();
    }

    @Override
    public void onRecipeGet(final Recipe.Item[] items) {
        final String message = "Found a total of " + items.length + " recipes";
        Log.d("DEBUG", message);

        mRecipeRecyclerAdapter.setRecipeList(items);
        this.getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                mDialog.dismiss();
                mRecipeRecyclerAdapter.notifyDataSetChanged();
                View v = RecipeListFragment.this.getView();
                Alert.snackLong(v, message);
            }
        });
    }

    @Override
    public void onQueryUpdate(final String s) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
            }
        });
    }
}