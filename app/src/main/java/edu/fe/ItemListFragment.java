package edu.fe;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemListFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "category-id";

    private String mCategoryId = "";
    private FoodItemRecyclerAdapter mAdapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
            */
    public ItemListFragment() {
    }

    public static ItemListFragment newInstance(Category category) {
        return newInstance(category.getObjectId());
    }

    public static ItemListFragment newInstance(String categoryId ) {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();

        args.putString(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            mCategoryId = getArguments().getString(ARG_CATEGORY_ID);
        }

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
                query.whereEqualTo(FoodItem.CATEGORY, c);
                query.orderByAscending(FoodItem.EXPIRATION_DATE);
                return query;
            }
        }, false, mListener, getActivity());


        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
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