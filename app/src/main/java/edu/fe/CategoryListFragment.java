package edu.fe;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private CategoryRecyclerAdapter mAdapter;

    public CategoryListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        if(view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            LinearLayoutManager lm = new LinearLayoutManager(context);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(lm);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // Push new Activity Here
                    Category category = (Category)mAdapter.getItem(position);

                }
            }));

            mAdapter = new CategoryRecyclerAdapter(
                    new ParseQueryAdapter.QueryFactory<Category>() {
                        @Override
                        public ParseQuery<Category> create() {
                            ParseQuery<Category> query = new ParseQuery<Category>(Category.class);
                            query.orderByAscending(Category.NAME);

                            return query;
                        }
                    }, false, this.getActivity());
        }
        return view;
    }
}
