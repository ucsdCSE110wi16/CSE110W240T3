package edu.fe;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import edu.fe.backend.Category;


/**
 * Created by david on 2/8/2016.
 */
public class CategoryListFragment extends Fragment {

    public static final int COLUMN_COUNT = 2;
    private CategoryRecyclerAdapter mAdapter;

    OnCategorySelectedHandler mListener;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public CategoryListFragment() {

    }

    @Override
    public View onCreateView
            (LayoutInflater inflater,
             ViewGroup container,
             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        mSwipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.category_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("DEBUG", "Refreshing categories");
                // TODO data has changed
                mAdapter.loadObjects();
                mAdapter.notifyDataSetChanged();
                mAdapter.fireOnDataSetChanged();
                // TODO onFinishDataChanged, call
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.category_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, COLUMN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        mAdapter = new CategoryRecyclerAdapter(
                new ParseQueryAdapter.QueryFactory<Category>() {
                    @Override
                    public ParseQuery<Category> create() {
                        ParseQuery<Category> query = new ParseQuery<>(Category.class);
                        query.fromLocalDatastore();
                        query.orderByAscending(Category.NAME);
                        return query;
                    }
                }, false, mListener, this.getActivity());
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("DEBUG", "Attach[context] - Class: " + context.getClass());
        if (context instanceof OnCategorySelectedHandler) {
            Log.d("DEBUG", "Listener from MainActivity is connected to adapter");
            mListener = (OnCategorySelectedHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement " + OnCategorySelectedHandler.class.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("DEBUG", "Attach[activity] - Class: " + activity.getClass());
        if (activity instanceof OnCategorySelectedHandler) {
            Log.d("DEBUG", "Listener from MainActivity is connected to adapter");
            mListener = (OnCategorySelectedHandler)activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement " + OnCategorySelectedHandler.class.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCategorySelectedHandler {
        public void onCategorySelected(Category category);
    }
}
