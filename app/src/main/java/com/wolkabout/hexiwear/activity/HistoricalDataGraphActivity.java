package com.wolkabout.hexiwear.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.dataAccess.Reading;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elber on 7/24/2017.
 */

@EActivity(R.layout.activity_historical_data_graph)
public class HistoricalDataGraphActivity extends Activity {
    @ViewById(R.id.spnReadingTypes)
    Spinner readingTypesSpinner;

    @ViewById(R.id.graph)
    GraphView graph;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    List<String> readingTypesList = new ArrayList<>();
    List<Reading> readingsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data_graph);

        FirebaseApp.initializeApp(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Althlete Plus/Test");

        getReadingTypes();
    }

    public void populateGraph(){
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        for (Reading reading : readingsList){
            DataPoint dp = new DataPoint(reading.timestamp, Double.parseDouble(reading.value));
            series.appendData(dp, true, readingsList.size());
        }

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), new SimpleDateFormat("HH:mm:ss Z")));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setNumVerticalLabels(5);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(86000000 * 3);

        graph.getViewport().setMaxXAxisSize(86400000);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        int i = 1;
    }

    /**
     * displays the firebase data in the spinner
     */
    public void getReadingTypes() {
        Query q = myRef.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    readingTypesList.add(String.valueOf(ds.getKey()));
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(HistoricalDataGraphActivity.this, android.R.layout.simple_spinner_item, readingTypesList);
                    readingTypesSpinner.setAdapter(myAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Click(R.id.pushButton)
    public void getReadings(){
        String readingType = readingTypesSpinner.getSelectedItem().toString();
        myRef = mFirebaseDatabase.getReference("Althlete Plus/Test/"+readingType);
        Query q = myRef.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reading reading = ds.getValue(Reading.class);
                    readingsList.add(reading);
                    populateGraph();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
