package com.example.dylanpomeroy.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PedometerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
    }

    protected void reset(View view){
        TextView textView = (TextView) findViewById(R.id.text_steps);
        textView.setText("0 Steps");
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateRange(View view){
        TextView textViewlow = (TextView) findViewById(R.id.low_input);
        TextView textViewhigh = (TextView) findViewById(R.id.high_input);

        //take text and enter in database
    }
}
