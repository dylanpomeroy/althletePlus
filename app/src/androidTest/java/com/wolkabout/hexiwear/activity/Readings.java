package com.wolkabout.hexiwear.activity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.activity.MainActivity_;
import com.wolkabout.hexiwear.activity.ReadingsActivity_;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Robert Thomas Jun.29th from example in story 3 branch
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Readings {

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
    public void skipPedoButton()throws Exception{
        onView(withId(R.id.btnSkipPairing)).perform(click());
        //assertEquals(ReadingsActivity.skippingHexiConnection, true);

        onView(ViewMatchers.withId(R.id.btnPedometer)).perform(click());
        //onView(withId(R.id.container_current)).check(matches(withId(R.layout.activity_pedometer)));
    }

    @Test
    public void skipHeartButton()throws Exception{
        onView(withId(R.id.btnSkipPairing)).perform(click());
        //assertEquals(ReadingsActivity.skippingHexiConnection, true);

        onView(ViewMatchers.withId(R.id.btnHeartRate)).perform(click());
        //onView(withId(R.id.container_current)).check(matches(withId(R.layout.activity_pedometer)));
    }

    @Test
    public void skipAlertButton()throws Exception{
        onView(withId(R.id.btnSkipPairing)).perform(click());
        //assertEquals(ReadingsActivity.skippingHexiConnection, true);

        int notificationNum = ReadingsActivity.vibrateDuration;

        onView(ViewMatchers.withId(R.id.btnAlertAlthlete)).perform(click());
        assertEquals(notificationNum>0, true);
    }
}
