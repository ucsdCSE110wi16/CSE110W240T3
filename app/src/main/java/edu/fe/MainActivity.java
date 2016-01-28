package edu.fe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vorph.utils.Alert;
import com.vorph.utils.ExceptionHandler;

import edu.fe.util.FoodItem;

public class MainActivity
        extends AppCompatActivity
        implements ItemListFragment.OnListFragmentInteractionListener {

    ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Captures and forwards all log calls to DEBUG tag in LogCat.
        ExceptionHandler.Embed(ExceptionHandler.DEFAULT_LOG_TYPE);

        setContentView(R.layout.activity_main);

        Log.d("DEBUG", "Initializing variables");
        // Initialize and resolves variables
        mContainerView = (ViewGroup) findViewById(R.id.container_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void onCategorySelected() {
        Log.d("DEBUG", "Opening list fragment");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = ItemListFragment.newInstance(1);
        fragmentTransaction.replace(R.id.container, fragment, "list")
                           .addToBackStack("list")
                           .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_show_list) {
            onCategorySelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(FoodItem item) {
        Log.d("DEBUG", "Item " + item.getName());
        Alert.snackLong(mContainerView, "Item: " + item.getName());
    }
}
