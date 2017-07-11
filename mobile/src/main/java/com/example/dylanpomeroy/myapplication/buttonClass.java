package com.example.dylanpomeroy.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Intro button class to access historical database page
 * @author Aqeb, Josh
 */
public class buttonClass extends AppCompatActivity {

    Button historicalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_layout);

        historicalData = (Button)findViewById(R.id.historicalData);
        historicalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(buttonClass.this, UserStory6Main.class);
                startActivity(i);
            }
        });


    }
}
