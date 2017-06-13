package com.wolkabout.hexiwear.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wolkabout.hexiwear.R;

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

    protected void reset(View view){
        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("Total Steps: 0");
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
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
