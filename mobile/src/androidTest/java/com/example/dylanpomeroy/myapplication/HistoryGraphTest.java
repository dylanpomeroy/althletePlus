package com.example.dylanpomeroy.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
public class HistoryGraphTest {
    @Rule
    public ActivityTestRule<HistoryGraph> mainActivityActivityTestRule = new ActivityTestRule<HistoryGraph>(HistoryGraph.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.dylanpomeroy.myapplication", appContext.getPackageName());
    }

    /*@Test
    public void test() throws Exception{
        Thread.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.spinzy)).atPosition(0).perform(click());
        onView(withId(R.id.pushButton)).perform(click());
    }*/
}
