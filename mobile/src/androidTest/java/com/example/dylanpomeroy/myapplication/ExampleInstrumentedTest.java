package com.example.dylanpomeroy.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;

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
    public void heartrateMeterButton() throws Exception {
        //Clickbutton
        onView(withId(R.id.imageButton1)).perform(click());
        //matchDisplay
        onView(withId(R.id.container_current)).check(matches(R.layout.activity_heartrate));
    }
    @Test
    public void pedometerButton() throws Exception {
        //Clickbutton
        onView(withId(R.id.imageButton2)).perform(click());
        //matchDisplay
        onView(withId(R.id.container_current)).check(matches(R.layout.activity_pedometer));
    }
    @Test
    public void historyButton() throws Exception {
        //Clickbutton
        onView(withId(R.id.imageButton3)).perform(click());
        //matchDisplay
        onView(withId(R.id.container_current)).check(matches(R.layout.activity_history));
    }

}
