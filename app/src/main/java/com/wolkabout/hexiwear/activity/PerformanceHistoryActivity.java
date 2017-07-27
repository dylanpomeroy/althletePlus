package com.wolkabout.hexiwear.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wolkabout.hexiwear.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by elber on 7/24/2017.
 */

@EActivity(R.layout.activity_performance_history)
public class PerformanceHistoryActivity extends Activity {


    /**
     * Launches the Historical Data Table activity on button click
     * @param view the current view
     */
    @Click(R.id.btnViewDataTable)
    public void switchToDataTable(View view){
        Intent intent = new Intent(getBaseContext(), HistoricalDataTableActivity_.class);
        startActivity(intent);
    }


    /**
     * Launches the Historical Data Graph activity on button click
     * @param view the current view
     */
    @Click(R.id.btnViewDataGraph)
    public void switchToDataGraph(View view){
        Intent intent = new Intent(getBaseContext(), HistoricalDataGraphActivity_.class);
        startActivity(intent);
    }
}
