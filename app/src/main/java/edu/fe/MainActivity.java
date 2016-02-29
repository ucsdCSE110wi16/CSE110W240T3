package edu.fe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.vorph.utils.Alert;
import com.vorph.utils.ExceptionHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import bolts.Continuation;
import bolts.Task;
import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;
import edu.fe.util.ResUtils;
import lib.material.picker.date.DatePickerDialog;

public class MainActivity
        extends AppCompatActivity
        implements ItemListFragment.OnListFragmentInteractionListener,
        CategoryListFragment.OnCategorySelectedHandler,
        NavigationView.OnNavigationItemSelectedListener,
        EntryFragment.OnFragmentInteractionListener {

    ViewGroup mContainerView;
    TextView loginNameView;
    TextView loginEmailView;
    MenuItem loginMenuItem;
    MenuItem signoutMenuItem;

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
                //showDialog();
                Intent i = new Intent(getApplicationContext(), EntryActivity.class);
                startActivity(i);
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

        // hack because of an AppCompat Change https://code.google.com/p/android/issues/detail?id=190786
        // http://stackoverflow.com/questions/33161345/android-support-v23-1-0-update-breaks-navigationview-get-find-header
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_dust, null);
        navigationView.addHeaderView(header);
        Menu menu = navigationView.getMenu();
        loginNameView = (TextView)header.findViewById(R.id.drawer_parse_name);
        loginEmailView = (TextView)header.findViewById(R.id.drawer_parse_email);
        loginMenuItem = menu.findItem(R.id.nav_login);
        signoutMenuItem = menu.findItem(R.id.nav_signout);

        checkLoginInformation();
	    FoodItem.cacheToLocalDBInBackground();
        Task<Void> loadCategoryTask = Category.cacheToLocalDBInBackground();
        Task<Void> waitTimeOutTask = Task.delay(500);
        Collection<Task<Void>> c = new ArrayList<>();
        c.add(loadCategoryTask);
        c.add(waitTimeOutTask);
        Task.whenAny(c).continueWith(new Continuation<Task<?>, Void>() {
            @Override
            public Void then(Task<Task<?>> task) throws Exception {
                loadCategories();
                return null;
            }
        });
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

    private void loadExpiringSoon() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment itemFragment = new ItemListFragment.Builder()
                                    .setQueryLimit(10) // set max date
                                    .build();
        transaction.replace(R.id.container, itemFragment, "expiringList").commit();
    }

    private void loadCategories() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.beginTransaction();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment categoryFragment = new CategoryListFragment();
        fragmentTransaction.replace(R.id.container, categoryFragment, "categoryList").
                commit();
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

    final static int LOGIN_REQUEST_CODE = 0;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_login: {
                // LOGIN BOYS
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), LOGIN_REQUEST_CODE);
                break;
            }
            case R.id.nav_signout: {
                new MaterialDialog.Builder(this)
                        .title("Are you sure?")
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.cancel)
                        .content("Your data will no longer be saved to the cloud")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ParseUser.logOut();
                                checkLoginInformation();
                            }
                        }).build().show();
                break;
            }
            case R.id.nav_about: {
                showAboutDialog();
                break;
            }
            case R.id.nav_categories: {
                // clear fragment stacks and replace
                loadCategories();
                break;
            }
            case R.id.nav_expiring: {
                // clear fragment stacks and replace
                loadExpiringSoon();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case LOGIN_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    checkLoginInformation();
                }
        }
    }

    public boolean isLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser != null && currentUser.isAuthenticated() && !ParseAnonymousUtils.isLinked(currentUser);
    }

    private void checkLoginInformation() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(isLoggedIn()) {
            // we are logged in
            loginNameView.setText("Hello " + currentUser.getString("name"));
            loginEmailView.setText(currentUser.getEmail());
            loginMenuItem.setVisible(false);
            signoutMenuItem.setVisible(true);
            FoodItem.cacheToLocalDBInBackground();
        }
        else {
            loginNameView.setText(R.string.navigation_drawer_default_name);
            loginEmailView.setText(R.string.navigation_drawer_default_email);
            loginMenuItem.setVisible(true);
            signoutMenuItem.setVisible(false);
        }
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

    void showAboutDialog() {
        new MaterialDialog.Builder(this)
                .title("About Project FE")
                .content(R.string.about_popup_content)
                .positiveText("Close")
                .show();
    }

    void showDialog() {
        final View customView;
        try {
            customView = LayoutInflater.from(this).inflate(R.layout.fragment_entry, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }

        if (customView == null) return;

        final Spinner spinner = (Spinner) customView.findViewById(R.id.spinner);
        final EditText nameField = (EditText)customView.findViewById(R.id.editText);

        final SpinAdapter adapter = new SpinAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final TextView textView = (TextView) customView.findViewById(R.id.editText2);
        final DatePickerDialog.OnDateSetListener onDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog.DateAttributeSet set) {
                String date = String.format("%d/%d/%d", set.day, set.month + 1, set.year);
                textView.setText(date);
            }
        };

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog.Builder(MainActivity.this)
                        .listener(onDateSetListener)
                        .setCalendar(Calendar.getInstance())
                        .show();
            }
        });

        new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title(R.string.entryPopUp)
                .customView(customView, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        FoodItem f = new FoodItem();
                        Category c = adapter.getCategory(spinner.getSelectedItemPosition());
                        f.setCategory(c);
                        f.setName(nameField.getText().toString());
                        f.pinInBackground();
                        f.saveEventually();
                    }
                })
                .show();

    }

    @Override
    public void onCategorySelected(Category category) {
        ItemListFragment fragment = new ItemListFragment.Builder().setCategory(category).build();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment, "item-list")
                .addToBackStack(null)
                .commit();
    }
}
