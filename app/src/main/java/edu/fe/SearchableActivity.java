package edu.fe;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import java.lang.Override;import java.lang.String;import java.text.ParseException;
import java.util.List;

import edu.fe.ItemListFragment;import edu.fe.backend.FoodItem;

public class SearchableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //Declare progressDialog
           // ProgressDialog progressDialog = new ProgressDialog(this);
           // progressDialog.setMessage("Searching Food ...");
           // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
           // progressDialog.show();

            //doMySearch(query);
            new ItemListFragment.Builder().setSearchTerm(query).build();

           // progressDialog.hide();
        }
    }

    private void doMySearch(String queryPassed){
/*
        ParseQuery<FoodItem> query = ParseQuery.getQuery(queryPassed);
        query.findInBackground(new FindCallback<FoodItem>() {
            public void done(List<FoodItem> objects, ParseException e) {
                if (e == null) {
                    objectsWereRetrievedSuccessfully(objects);
                } else {
                    objectRetrievalFailed();
                }
            }
        }*/
        new ItemListFragment.Builder().setSearchTerm(queryPassed).build();


    }


    }


