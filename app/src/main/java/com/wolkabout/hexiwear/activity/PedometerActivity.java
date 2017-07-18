package com.wolkabout.hexiwear.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.dataAccess.DataAccess;
import com.wolkabout.hexiwear.dataAccess.ReadingType;
import com.wolkabout.hexiwear.model.Characteristic;
import com.wolkabout.hexiwear.model.Mode;
import com.wolkabout.hexiwear.view.Reading;
import com.wolkabout.hexiwear.view.SingleReading;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by thomasrs on 2017-06-13.
 */
@EActivity(R.layout.activity_pedometer)
public class PedometerActivity extends Activity {
    public static int rangeHigh,rangeLow;
    public static int preSessionSteps = 0;
    private DataAccess dataAccess = new DataAccess();
    private boolean toastNotification = false;
    private boolean shouldCall = false;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toastNotification = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
        rangeHigh = 0;
        shouldCall = true;
    }

    /**
     * function that is automatically called and calls its self every 3000ms
     * It gets the latest reading form the hexiware and if out of range, lanched notification
     */
    @AfterViews
    protected void setPedometerVisibility() {
        int totalSteps = Integer.parseInt(dataAccess.getCurrentReading(ReadingType.Steps).value.trim());
        int sessionSteps = totalSteps - preSessionSteps;

        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: "+sessionSteps);

        if (shouldCall) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setPedometerVisibility();
                }
            }, 3000);
        }
        //updateRange(new View(this));

        // vibrate if not in range
        if(toastNotification) {
            toastNotification = false;
            if (sessionSteps > rangeHigh && rangeHigh > 0) {
                ReadingsActivity.shouldNotify = true;
                if (sessionSteps > rangeHigh) {
                    ReadingsActivity.notifyText = "He's hustling hard!";
                }
            }
        }
        if (sessionSteps > rangeHigh && rangeHigh > 0) {
            ReadingsActivity.vibrateDuration = 500;
            ReadingsActivity.shouldVibrate = true;
        }
    }


    /**
     * resets the pedo-meter count
     *
     * @param view
     */
    @Click(R.id.btnStepReset)
    protected void reset(View view){
        int totalSteps = Integer.parseInt(dataAccess.getCurrentReading(ReadingType.Steps).value.trim());
        preSessionSteps = totalSteps;

        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: 0");
    }

    /**
     * returns the user back to readingsActivity
     *
     * @param view
     */
    @Click(R.id.btnReturnToMain)
    public void returnToMain(View view) {
        shouldCall = false;
        Intent intent = new Intent(this, ReadingsActivity_.class);
        startActivity(intent);
    }

    /**
     * gets the range from the input box that the user entered
     *
     * @param view
     */
    @Click(R.id.updateRange)
    public void updateRange(View view){

        //read text from high_input and convert to string
        EditText textHigh = (EditText) findViewById(R.id.threshold);
        String highString = textHigh.getText().toString();
        toastNotification = true;

        //if the string is not empty, set the static range variable to the parsed int
        //only numbers are able to be entered becasue the input type is number
        if(!highString.equals("")) rangeHigh=Integer.parseInt(highString);
        else rangeHigh=0;


        //take text and enter in database
        //enter variables rangeHigh and rangeLow into database
    }

}
