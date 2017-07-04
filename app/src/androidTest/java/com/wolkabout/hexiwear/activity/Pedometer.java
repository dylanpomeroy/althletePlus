package com.wolkabout.hexiwear.activity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wolkabout.hexiwear.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Robert Thomas Jun.29th from example in story 3 branch
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Pedometer {

    @Rule
    public ActivityTestRule<PedometerActivity> mActivityRule = new ActivityTestRule<>(
            PedometerActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.wolkabout.hexiwear.activity", appContext.getPackageName());
    }

    @Test
    public void pedometerButton()throws Exception{
        onView(ViewMatchers.withId(R.id.btnReturnToMain)).perform(click());
        //onView(withId(R.id.container_current)).check(matches(R.layout.activity_main));
    }
    /*
    @Test
    public void checkHigh() throws Exception{
        onView(withId(R.id.high_input)).perform(typeText(String.valueOf("15")));
        onView(withId(R.id.high_input)).check(matches(withText("15")));
    }

    @Test
    public void reset()throws Exception{
        onView(withId(R.id.btnStepReset)).perform(click());
        onView(withId(R.id.text_steps)).check(matches(withText("Total Steps: 0")));
    }*/
}
