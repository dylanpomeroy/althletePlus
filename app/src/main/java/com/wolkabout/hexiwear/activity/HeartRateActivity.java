package com.wolkabout.hexiwear.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.activity.ReadingsActivity;
import com.wolkabout.hexiwear.activity.ReadingsActivity_;
import com.wolkabout.hexiwear.model.Characteristic;
import com.wolkabout.hexiwear.model.Mode;
import com.wolkabout.hexiwear.view.SingleReading;
import com.wolkabout.hexiwear.dataAccess.*;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Map;

/**
 * Created by thomasrs on 2017-06-13.
 */
@EActivity(R.layout.activity_heartrate)
public class HeartRateActivity extends Activity {
    static int rangeHigh,rangeLow;
    private DataAccess dataAccess = new DataAccess(this);
    private boolean toastNotification = false;
    private boolean shouldCall = false;

    /**
     * Create the view, and sets a bool to let the polling function be called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);
        shouldCall = true;
    }

    //reading the steps from hexiwear from readings activity

    /**
     * function that is continiusly called, checks if data is out of range, and updates the on
     * screen live data
     */
    @AfterViews
    protected void setHeartRateVisibility() {
        if (dataAccess.getCurrentReading(ReadingType.HeartRate) == null)
            dataAccess.addReading(new Reading(ReadingType.HeartRate, "0 bpm", new Date()));
        int heartRate = Integer.parseInt(dataAccess.getCurrentReading(ReadingType.HeartRate).value.split(" ")[0]);

        TextView textView = (TextView) findViewById(R.id.text_heartRate);
        textView.setText("Heartrate: "+ heartRate);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                setHeartRateVisibility();
            }
        }, 3000);

        if(shouldCall) updateRange(new View(this));

        // vibrate if not in range
        if ((heartRate > rangeHigh || heartRate < rangeLow) && rangeHigh > 0 && heartRate != 0){
            ReadingsActivity.vibrateDuration = 500;
            ReadingsActivity.shouldVibrate = true;
            if(toastNotification) {
                toastNotification = false;
                ReadingsActivity.shouldNotify = true;

                if (heartRate > rangeHigh) {
                    ReadingsActivity.notifyText = "Heart rate exceeding range!";
                } else if (heartRate < rangeLow) {
                    ReadingsActivity.notifyText = "Heart rate is too low, get out dem whips";
                }
            }
        }

    }

    /**
     * returns to the readings activity view
     *
     * @param view
     */
    @Click(R.id.returnToMain)
    public void returnToMain(View view) {
        ReadingsActivity.shouldVibrate = false;
        ReadingsActivity.shouldNotify = false;
        shouldCall = false;
        Intent intent = new Intent(this, ReadingsActivity_.class);
        startActivity(intent);
    }

    /**
     * gets the most recent range from the input box, this methode is called when the button is
     * click and ain setHeartRate Visibility
     *
     * @param view
     */
    @Click(R.id.updateRange)
    public void updateRange(View view){

        //read text from high_input and convert to string
        EditText textHigh = (EditText) findViewById(R.id.high_input);
        String highString = textHigh.getText().toString();

        //read text from low_input and convert to string
        EditText textLow = (EditText) findViewById(R.id.low_input);
        String lowString = textLow.getText().toString();

        toastNotification = true;
        //if the string is not empty, set the static range variable to the parsed int
        //only numbers are able to be entered becasue the input type is number
        if(!highString.equals("")) rangeHigh=Integer.parseInt(highString);
        else rangeHigh=0;
        if(!lowString.equals("")) rangeLow=Integer.parseInt(lowString);
        else rangeLow=0;

        //take text and enter in database
        //enter variables rangeHigh and rangeLow into database
    }

}