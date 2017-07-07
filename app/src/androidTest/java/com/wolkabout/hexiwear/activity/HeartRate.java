package com.wolkabout.hexiwear.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.activity.MainActivity_;
import com.wolkabout.hexiwear.activity.ReadingsActivity_;
import com.wolkabout.hexiwear.activity.HeartRateActivity_;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

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
    public void skipToHeart() throws Exception{
        onView(withId(R.id.btnSkipPairing)).perform(click());
        onView(withId(R.id.btnHeartRate)).perform(click());

        // checks for unique item that exists on the activity that should be open
        onView(withId(R.id.text_heartRate)).check(matches(isDisplayed()));
    }

    @Test
    public void pedometerButton()throws Exception{
        skipToHeart();
        assertEquals(inHeart, true);

        //onData(ViewMatchers.withId(R.id.btnReturnToMain)).perform(click());
        //onView(withId(R.id.container_current)).check(matches(withId(R.layout.activity_readings)));
    }

    @Test
    public void checkHigh() throws Exception{
        skipToHeart();
        assertEquals(inHeart, true);

        onView(withId(R.id.high_input)).perform(typeText(String.valueOf("100")));
        onView(withId(R.id.high_input)).check(matches(withText("100")));
    }

    @Test
    public void checkLow() throws Exception{
        skipToHeart();
        assertEquals(inHeart, true);

        onView(withId(R.id.low_input)).perform(typeText(String.valueOf("10")));
        onView(withId(R.id.low_input)).check(matches(withText("10")));
    }

}
