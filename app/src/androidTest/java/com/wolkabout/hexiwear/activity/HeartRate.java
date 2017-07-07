package com.wolkabout.hexiwear.activity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.wolkabout.hexiwear.R.id.btnReturnToMain;
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

    public void skipToHeart() throws Exception{
        onView(withId(R.id.btnSkipPairing)).perform(click());
        onView(withId(R.id.btnHeartRate)).perform(click());
        inHeart = true;
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
        assertEquals(inHeart, true);

        onView(withId(R.id.high_input)).perform(typeText(String.valueOf("100")), closeSoftKeyboard());
        onView(withId(R.id.high_input)).check(matches(withText("100")));
    }

    @Test
    public void checkLow() throws Exception{
        skipToHeart();
        assertEquals(inHeart, true);

        onView(withId(R.id.low_input)).perform(closeSoftKeyboard(), typeText(String.valueOf("0")), closeSoftKeyboard());
        onView(withId(R.id.low_input)).check(matches(withText("0")));
    }

    @Test
    public void testRange() throws Exception{
        skipToHeart();
        assertEquals(inHeart, true);

        onView(withId(R.id.high_input)).perform(typeText(String.valueOf("100")), closeSoftKeyboard());
        onView(withId(R.id.low_input)).perform(typeText(String.valueOf("0")), closeSoftKeyboard());

        onView(withId(R.id.updateRange)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.high_input)).check(matches(withText("100")));
        onView(withId(R.id.low_input)).check(matches(withText("0")));

    }

}
