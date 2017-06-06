package com.example.dylanpomeroy.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.dylanpomeroy.myapplication", appContext.getPackageName());
    }

    @Test
    public void pedometerBuntton()throws Exception{
        onView(withId(R.id.pedometer_to_main)).perform(click());
        //onView(withId(R.id.container_current)).check(matches(R.layout.activity_main));


    }

    @Test
    public void checHigh() throws Exception{
        onView(withId(R.id.high_input)).perform(typeText(String.valueOf("15")));

        onView(withId(R.id.high_input)).check(matches(withText("15")));
    }

    @Test
    public void checkLow() throws Exception{
        onView(withId(R.id.low_input)).perform(typeText(String.valueOf("15")));

        onView(withId(R.id.low_input)).check(matches(withText("15")));

    }

    @Test
    public void reset()throws Exception{
        onView(withId(R.id.reset)).perform(click());
        onView(withId(R.id.text_steps)).check(matches(withText("0 Steps")));
    }





}

