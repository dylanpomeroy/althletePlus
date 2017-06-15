package com.wolkabout.hexiwear.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.model.Characteristic;
import com.wolkabout.hexiwear.model.Mode;
import com.wolkabout.hexiwear.view.Reading;
import com.wolkabout.hexiwear.view.SingleReading;

import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by thomasrs on 2017-06-13.
 */

public class PedometerActivity extends ReadingsActivity{
    static int rangeHigh,rangeLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
    }

    //reading the steps from hexiwear from readings activity

    private void setPedometerVisibility(final Mode mode) {
        final Map<String, Boolean> displayPreferences = hexiwearDevices.getDisplayPreferences(device.getAddress());
        final Reading pedometer = readingSteps;
        pedometer.setVisibility(displayPreferences.get(readingSteps.getReadingType().name()) && mode.hasCharacteristic(readingSteps.getReadingType()) ? View.VISIBLE : View.GONE);
        //puts value in text_steps
        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: "+pedometer.toString());
    }

    protected void reset(View view){
        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: 0");
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, ReadingsActivity.class);
        startActivity(intent);
    }

    public void updateRange(View view){

        //read text from high_input and convert to string
        EditText textHigh = (EditText) findViewById(R.id.high_input);
        String highString = textHigh.getText().toString();

        //read text from low_input and convert to string
        EditText textLow = (EditText) findViewById(R.id.low_input);
        String lowString = textLow.getText().toString();

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
