package edu.fe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
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
}