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
    private static int preSessionSteps = 0;
    private DataAccess dataAccess = new DataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
    }

    //reading the steps from hexiwear from readings activity

    @AfterViews
    protected void setPedometerVisibility() {
        int totalSteps = Integer.parseInt(dataAccess.getCurrentReading(ReadingType.Steps).value);
        int sessionSteps = totalSteps - preSessionSteps;

        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: "+sessionSteps);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                setPedometerVisibility();
            }
        }, 500);

        updateRange(new View(this));

        // vibrate if not in range
        if (sessionSteps > rangeHigh ){
            ReadingsActivity.vibrateDuration = 500;
            ReadingsActivity.shouldVibrate = true;
        }
    }

    @Click(R.id.btnStepReset)
    protected void reset(View view){
        int totalSteps = Integer.parseInt(dataAccess.getCurrentReading(ReadingType.Steps).value);
        preSessionSteps = totalSteps;

        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: 0");
    }

    @Click(R.id.btnReturnToMain)
    public void returnToMain(View view) {
        Intent intent = new Intent(this, ReadingsActivity_.class);
        startActivity(intent);
    }

    public void updateRange(View view){

        //read text from high_input and convert to string
        EditText textHigh = (EditText) findViewById(R.id.high_input);
        String highString = textHigh.getText().toString();

        //read text from low_input and convert to string
        //EditText textLow = (EditText) findViewById(R.id.low_input);
        //String lowString = textLow.getText().toString();

        //if the string is not empty, set the static range variable to the parsed int
        //only numbers are able to be entered becasue the input type is number
        if(!highString.equals("")) rangeHigh=Integer.parseInt(highString);
        else rangeHigh=0;

        //commenting out low end since that is not helpful for the client with pedometer
       // if(!lowString.equals("")) rangeLow=Integer.parseInt(lowString);
        //else rangeLow=0;

        //take text and enter in database
        //enter variables rangeHigh and rangeLow into database
    }

}
