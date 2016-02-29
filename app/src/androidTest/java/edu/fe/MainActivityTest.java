package edu.fe;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.design.internal.NavigationMenuItemView;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import junit.framework.Assert;

import java.util.Timer;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;

/**
 * Created by david on 2/28/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    // https://github.com/ucsdCSE110wi16/CSE110W240T3/wiki/Scenarios

    @Before
    public void setup() {
        Context ctx = mMainActivityRule.getActivity().getApplication();

    }

    // scenario 1
    @Test
    public void TestScenario1() throws Exception {
        // Given is Logged in
        Assert.assertTrue(mMainActivityRule.getActivity().isLoggedIn());
        Thread.sleep(2000);
        // and categories are loaded
        onView(withId(R.id.category_list)).check(matches(isDisplayed()));
        // When I click the category
        onView(withId(R.id.category_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //  Then the items under that category should be shown
        onView(withId(R.id.item_list)).check(matches(isDisplayed()));
        // When I click on an item
        //onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(0).perform(click());
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Then edit dialog pops up.
        // right now its just a snackbar
        onView(allOf(withId(android.support.design.R.id.snackbar_text))).check(matches(isDisplayed()));

    }

    // scenario 2
    @Test
    public void TestScenario2() throws Exception {
        // Given the app is loaded
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        // When I click the drawer button
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        // Then the drawer slides open
        // When I click on Expiring Soon
        //onView(withId(R.id.nav_expiring)).perform(click());
        //onView(allOf(withId(R.id.nav_view), has))
        //onView(withId(R.layout.fragment_item_list)).check(matches(isDisplayed()));

    }

    @Test
    public void TestScenario3() throws Exception {
        // Given I am logged in
        Assert.assertTrue(mMainActivityRule.getActivity().isLoggedIn());
        // When I click the add button
        onView(withId(R.id.fab)).perform(click());
        // The new item popup shows
        onView(withId(R.id.activity_entry)).check(matches(isDisplayed()));

        // When I click the done button
        // Then the item is saved
    }

}