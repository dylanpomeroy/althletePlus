package com.wolkabout.hexiwear.activity;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.dagger.Component;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wolkabout.hexiwear.ElapsedTimeIdlingResource;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.activity.PedometerActivity;
import com.wolkabout.hexiwear.activity.MainActivity_;
import com.wolkabout.hexiwear.activity.ReadingsActivity_;
import com.wolkabout.hexiwear.dataAccess.DataAccess;
import com.wolkabout.hexiwear.dataAccess.Reading;
import com.wolkabout.hexiwear.dataAccess.ReadingType;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
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
import static org.junit.Assert.*;

/**
 * Robert Thomas Jun.29th from example in story 3 branch
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Pedometer {

    public boolean inPedo = false;

    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(
            MainActivity_.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = getTargetContext();

        assertEquals("com.wolkabout.hexiwear", appContext.getPackageName());
    }

    @Test
    // For Acceptance Test 3.1
    public void skipToPedometer() throws Exception{
        onView(withId(R.id.btnSkipPairing)).perform(click());
        onView(ViewMatchers.withId(R.id.btnPedometer)).perform(click());

        // checks for unique item that exists on the activity that should be open
        onView(withId(R.id.text_steps)).check(matches(isDisplayed()));
    }

    @Test
    // For Acceptance Test 3.2
    public void readingUpdates() throws Exception{
        skipToPedometer();

        // mock heart rate data
        DataAccess dA = new DataAccess();
        dA.addReading(new Reading(ReadingType.Steps, "123", new Date()));

        // wait for 1000 milliseconds... a crazy espresso way
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);

        // validate value is updated and correct
        onView(withId(R.id.text_steps)).check(matches(withText("Total Steps: 123")));

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    // For Acceptance Test 3.3
    public void outOfRangeVibrate() throws Exception {
        skipToPedometer();

        // set upper range we will be exceeding
        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("120")), closeSoftKeyboard());
        onView(withId(R.id.updateRange)).perform(click());

        // assure that these are false initially
        assertFalse(ReadingsActivity.vibrateHasBeenTriggered);

        // send in a mocked reading
        DataAccess dataAccess = new DataAccess();
        dataAccess.addReading(new Reading(ReadingType.Steps, "123", new Date()));

        // wait 1000 milliseconds
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.text_steps)).check(matches(isDisplayed()));

        // check to see range has been exceeded and acted upon
        assertTrue(ReadingsActivity.vibrateHasBeenTriggered);

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    // For Acceptance Test 4.1
    public void outOfRangeAlert() throws Exception {
        skipToPedometer();

        // set upper range we will be exceeding
        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("120")), closeSoftKeyboard());
        onView(withId(R.id.updateRange)).perform(click());

        // assure that these are false initially
        assertFalse(ReadingsActivity.notifyHasBeenTriggered);

        // send in a mocked reading
        DataAccess dataAccess = new DataAccess();
        dataAccess.addReading(new Reading(ReadingType.Steps, "123", new Date()));

        // wait 1000 milliseconds
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.text_steps)).check(matches(isDisplayed()));

        // check to see range has been exceeded and acted upon
        assertTrue(ReadingsActivity.notifyHasBeenTriggered);

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void pedometerButton()throws Exception{
        skipToPedometer();
        assertEquals(inPedo, true);

        onView(withId(R.id.btnReturnToMain)).perform(closeSoftKeyboard(), click());
        //intended(hasComponent(new ComponentName(getTargetContext(), ReadingsActivity.class)));
    }

    @Test
    public void checkHigh() throws Exception{
        skipToPedometer();
        assertEquals(inPedo, true);

        onView(withId(R.id.high_input)).perform(clearText(), typeText(String.valueOf("15")), closeSoftKeyboard());
        onView(withId(R.id.high_input)).check(matches(withText("15")));
    }

    @Test
    public void testRange() throws Exception{
        skipToPedometer();
        assertEquals(inPedo, true);

        onView(withId(R.id.high_input)).perform(clearText(), typeText("15"), closeSoftKeyboard());

        onView(withId(R.id.updateRange)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.high_input)).check(matches(withText("15")));

    }

    @Test
    public void reset()throws Exception{
        skipToPedometer();
        assertEquals(inPedo, true);

        onView(withId(R.id.btnStepReset)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.text_steps)).check(matches(withText("Total Steps: 0")));
    }
}
