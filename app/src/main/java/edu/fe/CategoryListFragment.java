package edu.fe;

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
                mAdapter.notifyDataSetChanged();
                // TODO onFinishDataChanged, call
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.category_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, COLUMN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Push new Activity Here
                        Category category = mAdapter.getItem(position);

                    }
                }));

        mAdapter = new CategoryRecyclerAdapter(
                new ParseQueryAdapter.QueryFactory<Category>() {
                    @Override
                    public ParseQuery<Category> create() {
                        ParseQuery<Category> query = new ParseQuery<>(Category.class);
                        query.orderByAscending(Category.NAME);

                        return query;
                    }
                }, false, this.getActivity());
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}
