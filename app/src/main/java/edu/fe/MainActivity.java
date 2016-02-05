package edu.fe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vorph.utils.Alert;
import com.vorph.utils.ExceptionHandler;

import java.nio.channels.FileLock;

import edu.fe.util.FoodItem;
import edu.fe.util.ResUtils;

public class MainActivity
        extends AppCompatActivity
        implements ItemListFragment.OnListFragmentInteractionListener, EntryFragment.OnFragmentInteractionListener {

    ViewGroup mContainerView;

    boolean mIsCategorySelected = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Captures and forwards all log calls to DEBUG tagged in LogCat.
        ExceptionHandler.Embed(ExceptionHandler.DEFAULT_LOG_TYPE);

        setContentView(R.layout.activity_main);
        Log.d("DEBUG", "Initializing variables");
        // Initialize and resolves variables
        mContainerView = (ViewGroup) findViewById(R.id.container_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load all sample resources.
        // TODO New Thread to initialize resources, since loading bitmaps and refactoring
        // TODO them to be a certain size takes a bit of time.
        ResUtils.initialize(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = EntryFragment.newInstance();
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void onCategorySelected() {
        Log.d("DEBUG", "Opening list fragment");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mIsCategorySelected) {
            mIsCategorySelected = false;
            Fragment fragment = fragmentManager.findFragmentByTag("list");
            fragmentTransaction.remove(fragment).commit();
            return;
        }
        mIsCategorySelected = true;

        Fragment fragment = ItemListFragment.newInstance(1);
        fragmentTransaction.add(R.id.container, fragment, "list")
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
        } else if (id == R.id.action_show_list) {
            onCategorySelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(FoodItem item) {
        Log.d("DEBUG", "Item " + item.getHeaderText());
        Alert.snackLong(mContainerView, "Item: " + item.getHeaderText());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("DEBUG", "onFragmentInteraction");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.fe/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.fe/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
