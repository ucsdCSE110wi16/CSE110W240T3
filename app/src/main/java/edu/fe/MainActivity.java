package edu.fe;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.vorph.utils.Alert;
import com.vorph.utils.ExceptionHandler;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;
import edu.fe.util.ResUtils;

public class MainActivity
        extends AppCompatActivity
        implements ItemListFragment.OnListFragmentInteractionListener,
        CategoryListFragment.OnCategorySelectedHandler,
        NavigationView.OnNavigationItemSelectedListener,
        EntryFragment.OnFragmentInteractionListener {

    ViewGroup mContainerView;

    boolean mIsCategorySelected = false;
    Category mSelectedCategory = null;

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
                showDialog();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                    this, drawer, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadCategories();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        int count = getFragmentManager().getBackStackEntryCount();
        if(count > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
}

    private void loadCategories() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment categoryFragment = new CategoryListFragment();
        fragmentTransaction.replace(R.id.container, categoryFragment, "categoryList").
                commit();
    }

    private void onCategorySelected() {
        Log.d("DEBUG", "Opening list fragment");
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Log.d("DEBUG", "Drawer: camera");
        } else if (id == R.id.nav_gallery) {
            Log.d("DEBUG", "Drawer: gallery");
        } else if (id == R.id.nav_slideshow) {
            Log.d("DEBUG", "Drawer: slideshow");
        } else if (id == R.id.nav_manage) {
            Log.d("DEBUG", "Drawer: manage");
        } else if (id == R.id.nav_share) {
            Log.d("DEBUG", "Drawer: share");
        } else if (id == R.id.nav_send) {
            Log.d("DEBUG", "Drawer: send");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(FoodItem item) {
        Log.d("DEBUG", "Item " + item.getName());
        Alert.snackLong(mContainerView, "Item: " + item.getName());
    }

    @Override
    public void onDialogFragmentInteraction(Uri uri) {
        Log.d("DEBUG", "onFragmentInteraction");
    }

    void showDialog() {
        //mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = EntryFragment.newInstance(DialogFragment.STYLE_NORMAL);
        newFragment.show(ft, "dialog");
        ft.addToBackStack(null);
    }

    @Override
    public void onCategorySelected(Category category) {
        ItemListFragment fragment = ItemListFragment.newInstance(category);
        getFragmentManager().beginTransaction().add(R.id.container, fragment, "item-list").
                addToBackStack(null).commit();
    }
}
