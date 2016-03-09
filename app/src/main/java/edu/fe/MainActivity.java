package edu.fe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.vorph.anim.AnimUtils;
import com.vorph.utils.Alert;
import com.vorph.utils.ExceptionHandler;

import java.util.Calendar;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;
import edu.fe.util.ResUtils;
import edu.fe.util.ThemeUtils;
import lib.material.dialogs.DialogAction;
import lib.material.dialogs.GravityEnum;
import lib.material.dialogs.MaterialDialog;

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
    Category mSelectedCategory = null;
    Toolbar mToolbar;
    FloatingActionButton mFab;


    final static int LOGIN_REQUEST_CODE = 0;
    final static int NEW_ITEM_REQUEST_CODE = 1;

    String[] mCategoriesStringArray;

    int mPrimaryColor = 0;
    int mPrimaryColorDark = 0;
    int mLastTranslationColor = 0;
    int mLastTranslationColorDark = 0;

    boolean mIsCategorySelected = false;
    boolean mToolbarHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Captures and forwards all log calls to DEBUG tagged in LogCat.
        ExceptionHandler.Embed(ExceptionHandler.DEFAULT_LOG_TYPE);
        setContentView(R.layout.activity_main);

        mPrimaryColor = R.color.colorPrimary;
        mPrimaryColorDark = R.color.colorPrimaryDark;
        mLastTranslationColor = mPrimaryColor;
        mLastTranslationColorDark = mPrimaryColorDark;

        Log.d("DEBUG", "Initializing variables");
        // Initialize and resolves variables
        mContainerView = (ViewGroup) findViewById(R.id.container_content);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Load all sample resources.
        // TODO New Thread to initialize resources, since loading bitmaps and refactoring
        // TODO them to be a certain size takes a bit of time.
        ResUtils.initialize(this);
        ThemeUtils.setDefaultTheme(this);


        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                // try and get hint
                if(mSelectedCategory != null) {
                    intent.putExtra(EntryActivity.ITEM_CATEGORY_HINT, mSelectedCategory.getName());
                }
                startActivityForResult(intent, NEW_ITEM_REQUEST_CODE);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                    this, drawer, mToolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // hack because of an AppCompat Change https://code.google.com/p/android/issues/detail?id=190786
        // http://stackoverflow.com/questions/33161345/android-support-v23-1-0-update-breaks-navigationview-get-find-header
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        navigationView.addHeaderView(header);
        Menu menu = navigationView.getMenu();
        loginNameView = (TextView)header.findViewById(R.id.drawer_parse_name);
        loginEmailView = (TextView)header.findViewById(R.id.drawer_parse_email);
        loginMenuItem = menu.findItem(R.id.nav_login);
        signoutMenuItem = menu.findItem(R.id.nav_signout);

        checkLoginInformation();
	    FoodItem.cacheToLocalDBInBackground();
        loadCategories();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // OnOrientationChanges:
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        int count = getFragmentManager().getBackStackEntryCount();
        boolean hasAtLeastOneBackEntry = count > 0;
        if (hasAtLeastOneBackEntry) {
            this.resetToolbar();
            getFragmentManager().popBackStackImmediate();
            mSelectedCategory = null;
            return;
        }

        new MaterialDialog.Builder(this)
                        .content("You're about to exit the application")
                        .contentGravity(GravityEnum.CENTER)
                        .typeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD)
                        .positiveText("Ok")
                        .negativeText("Dismiss")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                MainActivity.super.onBackPressed();
                            }
                        }).show();
    }

    private void loadExpiringSoon() {
        this.translateToolbar();

        mSelectedCategory = null;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR, 1);
        Fragment itemFragment = new ItemListFragment.Builder()
                                            .setMaxDate(c.getTime())
                                            .build();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.replace(R.id.container, itemFragment, "expiringList").commit();


    }

    private void loadCategories() {
        this.resetToolbar();

        mSelectedCategory = null;
        FragmentManager fragmentManager = getFragmentManager();
        Fragment categoryFragment = new CategoryListFragment();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.popup_enter_obj, R.anim.popup_exit_obj)
                .replace(R.id.container, categoryFragment, "categoryList")
                .commit();
    }

    private void loadRecipes() {
        FragmentManager fragmentManager = getFragmentManager();

        Fragment recipeFragment = new RecipeListFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, recipeFragment, "recipeList").commit();
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
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
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
            case R.id.nav_find_recipes: {
                // clear fragment stacks and replace
                loadRecipes();
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
                break;
            case NEW_ITEM_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    Alert.snackLong(mContainerView, getString(R.string.new_item_success));
                } else if(resultCode == EntryActivity.RESULT_FAIL) {
                    Alert.snackLong(mContainerView, getString(R.string.new_item_fail));
                } else if(resultCode == EntryActivity.RESULT_DELETED) {
                    Alert.snackLong(mContainerView, getString(R.string.edit_item_deleted));
                }
                break;
        }
        Fragment f = getFragmentManager().findFragmentByTag("itemList");
        if(f instanceof ItemListFragment) {
            ItemListFragment ilf = (ItemListFragment)f;
            ilf.refreshObjects();
        }
        f  = getFragmentManager().findFragmentByTag("expiringList");
        if(f instanceof  ItemListFragment) {
            ItemListFragment ilf = (ItemListFragment)f;
            ilf.refreshObjects();
        }
    }

    public boolean isLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser != null
                && currentUser.isAuthenticated()
                && !ParseAnonymousUtils.isLinked(currentUser);
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

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onListFragmentInteraction(FoodItem item) {
        Log.d("DEBUG", "Item " + item.getName());
        //Alert.snackLong(mContainerView, "Item: " + item.getName());
        Intent intent = new Intent(MainActivity.this, EntryActivity.class);
        intent.putExtra(EntryActivity.EDIT_ITEM_ID, item.getObjectId());
        startActivityForResult(intent, NEW_ITEM_REQUEST_CODE);
    }

    @Override
    public void onDialogFragmentInteraction(Uri uri) {
        Log.d("DEBUG", "onFragmentInteraction");
    }

    void showAboutDialog() {
        new MaterialDialog.Builder(this)
                .title("About " + getString(R.string.app_name))
                .content(R.string.about_popup_content)
                .positiveText("Close")
                .show();
    }

    @Override
    public void onCategorySelected(Category category) {
        int fromColorAttr = mPrimaryColor;
        int fromColorDarkAttr = mPrimaryColorDark;
        mToolbarHasChanged = true;
        mLastTranslationColor = category.colorId;
        mLastTranslationColorDark = category.colorDarkId;
        AnimUtils.translateColor(this, mToolbar, fromColorAttr, category.colorId, 300);
        AnimUtils.translateWindowStatusBarColor(this, fromColorDarkAttr, category.colorDarkId, 300);

        mSelectedCategory = category;
        ItemListFragment fragment = new ItemListFragment.Builder().setCategory(category).build();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.popup_enter_obj,
                        R.anim.popup_exit_obj,
                        R.anim.popup_enter_obj,
                        R.anim.popup_exit_obj)
                .add(R.id.container, fragment, "itemList")
                .addToBackStack(null)
                .commit();
    }

    private void resetToolbar() {
        if (mToolbarHasChanged) {
            AnimUtils.translateColor(this, mToolbar, mLastTranslationColor, mPrimaryColor, 300);
            ValueAnimator colorAnimator =
                    AnimUtils.getWindowStatusBarTranslator(
                            this,
                            mLastTranslationColorDark,
                            mPrimaryColorDark,
                            300
                    );
            if (colorAnimator != null) {
                colorAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        if (mFab.getVisibility() == View.GONE) mFab.show();
                    }
                });
                colorAnimator.start();
            }
            mToolbarHasChanged = false;
        }
    }

    private void translateToolbar() {

        mToolbarHasChanged = true;
        AnimUtils.translateColor(this, mToolbar, mLastTranslationColor, R.color.red_700, 300);
        ValueAnimator colorAnimator =
                AnimUtils.getWindowStatusBarTranslator(
                        this,
                        mLastTranslationColorDark,
                        R.color.red_900,
                        300
                );
        if (colorAnimator != null) {
            colorAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mFab.getVisibility() == View.VISIBLE) mFab.hide();
                }
            });
            colorAnimator.start();
            mLastTranslationColorDark = R.color.red_900;
        }
        mLastTranslationColor = R.color.red_700;
    }
}
