package com.wolkabout.hexiwear.activity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wolkabout.hexiwear.ElapsedTimeIdlingResource;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.dataAccess.DataAccess;
import com.wolkabout.hexiwear.dataAccess.Reading;
import com.wolkabout.hexiwear.dataAccess.ReadingType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Robert Thomas Jun.29th from example in story 3 branch
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HeartRate {

    public boolean inHeart = false;

    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(
            MainActivity_.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.wolkabout.hexiwear", appContext.getPackageName());
    }

    @Test
    // For Acceptance Test 1.1
    public void skipToHeart() throws Exception{
        if(!inHeart) {
            // perform button clicks to get to heart rate activity
            onView(withId(R.id.btnSkipPairing)).perform(click());
            onView(withId(R.id.btnHeartRate)).perform(click());
        }
        // checks for unique item that exists on the activity that should be open
        onView(withId(R.id.text_heartRate)).check(matches(isDisplayed()));
        inHeart = true;
    }

    @Test
    // For Acceptance Test 1.2
    public void readingUpdates() throws Exception {
        skipToHeart();

        // mock heart rate data
        DataAccess dA = new DataAccess();
        dA.addReading(new Reading(ReadingType.HeartRate, "123", new Date()));

        // wait for 1000 milliseconds... a crazy espresso way
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);

        // validate value is updated and correct
        onView(withId(R.id.text_heartRate)).check(matches(withText("Heartrate: 123")));

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    // For Acceptance Test 10.1
    public void outOfRangeVibrate() throws Exception {
        skipToHeart();

        // set upper range we will be exceeding
        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("120")), closeSoftKeyboard());
        onView(withId(R.id.updateRange)).perform(click());

        // assure that these are false initially
        //assertFalse(ReadingsActivity.vibrateHasBeenTriggered);

        // send in a mocked reading
        DataAccess dataAccess = new DataAccess();
        dataAccess.addReading(new Reading(ReadingType.HeartRate, "123", new Date()));

        // wait 1000 milliseconds
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.text_heartRate)).check(matches(isDisplayed()));

        // check to see range has been exceeded and acted upon
        assertTrue(ReadingsActivity.vibrateHasBeenTriggered);

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    // For Acceptance Test 10.2
    public void inRangeNoVibrate() throws Exception {
        skipToHeart();

        // set upper range we will be exceeding
        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("123")), closeSoftKeyboard());
        onView(withId(R.id.updateRange)).perform(click());

        // assure that these are false initially
        //assertFalse(ReadingsActivity.vibrateHasBeenTriggered);

        // send in a mocked reading
        DataAccess dataAccess = new DataAccess();
        dataAccess.addReading(new Reading(ReadingType.HeartRate, "120", new Date()));

        // wait 1000 milliseconds
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.text_heartRate)).check(matches(isDisplayed()));

        // check to see range has been exceeded and acted upon
        assertTrue(ReadingsActivity.vibrateHasBeenTriggered);

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    // For Acceptance Test 4.2
    public void outOfRangeAlert() throws Exception {
        skipToHeart();

        // set upper range we will be exceeding
        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("120")), closeSoftKeyboard());
        onView(withId(R.id.updateRange)).perform(click());

        // assure that these are false initially
        //assertFalse(ReadingsActivity.notifyHasBeenTriggered);

        // send in a mocked reading
        DataAccess dataAccess = new DataAccess();
        dataAccess.addReading(new Reading(ReadingType.HeartRate, "123", new Date()));

        // wait 1000 milliseconds
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.text_heartRate)).check(matches(isDisplayed()));

        // check to see range has been exceeded and acted upon
        assertTrue(ReadingsActivity.notifyHasBeenTriggered);

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void pedometerButton()throws Exception{
        skipToHeart();
        assertEquals(inHeart, true);

        //onView(withId(R.id.btnReturnToMain)).perform(closeSoftKeyboard(), click());
        //onView(withId(R.id.container_current)).check(matches(withId(R.layout.activity_readings)));
    }

    @Test
    public void checkHigh() throws Exception{
        skipToHeart();

        onView(withId(R.id.high_input)).perform(typeText(String.valueOf("100")), closeSoftKeyboard());
        onView(withId(R.id.high_input)).check(matches(withText("100")));
    }

    @Test
    public void checkLow() throws Exception{
        skipToHeart();
        //assertEquals(inHeart, true);

        onView(withId(R.id.low_input)).perform(closeSoftKeyboard(), typeText(String.valueOf("0")), closeSoftKeyboard());
        onView(withId(R.id.low_input)).check(matches(withText("0")));
    }

    @Test
    public void testRange() throws Exception{
        skipToHeart();
        //assertEquals(inHeart, true);

        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("100")), closeSoftKeyboard());
        onView(withId(R.id.low_input)).perform(clearText(), typeText(String.valueOf("0")), closeSoftKeyboard());

        onView(withId(R.id.updateRange)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.high_input)).check(matches(withText("100")));
        onView(withId(R.id.low_input)).check(matches(withText("0")));

    }

}
