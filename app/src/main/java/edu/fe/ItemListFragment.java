package edu.fe;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemListFragment extends Fragment {

    public static class Builder {
        private String mCategoryId = null;
        private int mLimit = 0;
        private String mSearchTerm = null;
        private String mMaxDate = null;

        public Builder() {
        }

        public Builder setCategory(Category category) {
            if(category != null)
                mCategoryId = category.getObjectId();

            return this;
        }

        public Builder setQueryLimit(int limit) {
            if(limit > 0)
                mLimit = limit;

            return this;
        }

        public Builder setSearchTerm(String search) {
            mSearchTerm = search;
            return this;
        }

        public Builder setMaxDate(Date date) {
            mMaxDate = DateFormat.getDateInstance().format(date);
            return this;
        }

        public ItemListFragment build() {
            ItemListFragment fragment = new ItemListFragment();
            Bundle args = new Bundle();
            args.putString(ARG_CATEGORY_ID, mCategoryId);
            args.putInt(ARG_QUERY_LIMIT, mLimit);
            args.putString(ARG_SEARCH_TERM, mSearchTerm);
            args.putString(ARG_MAX_DATE, mMaxDate);
            fragment.setArguments(args);
            return fragment;
        }
    }

    private static final String ARG_CATEGORY_ID = "category-id";
    private static final String ARG_QUERY_LIMIT = "query-limit";
    private static final String ARG_SEARCH_TERM = "search-term";
    private static final String ARG_MAX_DATE = "max-date";

    private String mCategoryId = null;
    private String mSearchTerm = null;
    private Date mMaxDate = null;
    private int mQueryLimit = 0;
    private FoodItemRecyclerAdapter mAdapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
            */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            mCategoryId = getArguments().getString(ARG_CATEGORY_ID);
            mQueryLimit = getArguments().getInt(ARG_QUERY_LIMIT);
            mSearchTerm = getArguments().getString(ARG_SEARCH_TERM);
            try {
                mMaxDate = DateFormat.getDateInstance().parse(getArguments().getString(ARG_MAX_DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void refreshObjects() {
        if(mAdapter != null) {
            mAdapter.loadObjects();
        }
    }

    private View getRecyclerView() {
        return getView().findViewById(R.id.item_list);
    }

    private View getEmptyTextView() {
        return getView().findViewById(R.id.empty_text);
    }

    @Override
    public View onCreateView
            (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mAdapter = new FoodItemRecyclerAdapter(new ParseQueryAdapter.QueryFactory<FoodItem>() {

            @Override
            public ParseQuery<FoodItem> create() {
                ParseQuery<FoodItem> query = new ParseQuery<FoodItem>(FoodItem.class);
                Category c = ParseObject.createWithoutData(Category.class, mCategoryId);
                query.fromLocalDatastore();
                if(mCategoryId != null && !mCategoryId.isEmpty())
                    query.whereEqualTo(FoodItem.CATEGORY, c);
                if(mQueryLimit > 0) {
                    query.setLimit(mQueryLimit);
                }
                if(mSearchTerm != null && !mSearchTerm.isEmpty())
                    query.whereMatches(FoodItem.NAME, mSearchTerm);
                if(mMaxDate != null) {
                    query.whereLessThanOrEqualTo(FoodItem.EXPIRATION_DATE, mMaxDate);
                }
                query.orderByAscending(FoodItem.EXPIRATION_DATE);
                return query;
            }
        }, false, mListener, getActivity());

        mAdapter.addOnDataSetChangedListener(new ParseRecyclerQueryAdapter.OnDataSetChangedListener() {
            @Override
            public void onDataSetChanged() {
                if(mAdapter.getItemCount() > 0) {
                    View emptyTextView = getEmptyTextView();
                    if(emptyTextView != null) {
                        emptyTextView.setVisibility(View.GONE);
                    }
                } else {
                    View emptyTextView = getEmptyTextView();
                    if(emptyTextView != null) {
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        View recycler = view.findViewById(R.id.item_list);
        if (recycler instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) recycler;
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        //
        super.onResume();
    }

    @Override
    public void onStart() {
        //mAdapter.fireOnDataSetChanged();
        super.onStart();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("DEBUG", "Attach[context] - Class: " + context.getClass());
        if (context instanceof OnListFragmentInteractionListener) {
            Log.d("DEBUG", "Listener from MainActivity is connected to adapter");
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    // The above attach is so new, that not everything implements it, so
    // we must use the deprecated version when those calls aren't being reached.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("DEBUG", "Attach[activity] - Class: " + activity.getClass());

        if (activity instanceof OnListFragmentInteractionListener) {
            Log.d("DEBUG", "Listener from MainActivity is connected to adapter");
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mAdapter = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FoodItem item);
    }
}