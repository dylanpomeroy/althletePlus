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

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by elber on 7/24/2017.
 */

/**
 * This class displays a graph of all data for a specified reading type
 * @author Aqeb Josh
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
    List<Reading> readingsList;
    List<Reading> displayList;

    double maxY = 0.0;
    String user = ReadingsActivity.username;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data_graph);


        FirebaseApp.initializeApp(this);
        if(user.contains("@")){
            user = user.substring(0, user.indexOf("@"));
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Althlete Plus/"+user);//Test

        getReadingTypes();
    }

    /**
     * populates the graph with the contents of the readingsList
     */
    public void populateGraph(){
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        
        for (Reading reading : displayList){
            String temp = reading.value;
            if(temp.contains(" ")){
                temp = temp.substring(0, temp.indexOf(" "));
            }
            if(Double.parseDouble(temp) > maxY)
                maxY = Double.parseDouble(temp);
            DataPoint dp = new DataPoint(reading.timestamp, Double.parseDouble(temp));
            series.appendData(dp, true, displayList.size());
        }

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), new SimpleDateFormat("MMM dd, yyyy")));
        graph.getGridLabelRenderer().setNumHorizontalLabels(2);
        graph.getGridLabelRenderer().setNumVerticalLabels(5);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(0);
       // graph.getViewport().setMaxXAxisSize(86400000);
        graph.getViewport().setMaxX(86000000 * 3);


        graph.getViewport().setMaxY(maxY+1);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
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

    /**
     * This function gets the readings based on the reading type and places them in the
     * readingsList. it then goes through the readingList and adds the average of each day
     * to the displayList
     */
    @Click(R.id.pushButton)
    public void getReadings(){
        maxY = 0.0;
        readingsList = new ArrayList<>();
        displayList = new ArrayList<>();

        String readingType = readingTypesSpinner.getSelectedItem().toString();
        myRef = mFirebaseDatabase.getReference("Althlete Plus/"+user+"/"+readingType);//Test
        Query q = myRef.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reading reading = ds.getValue(Reading.class);
                    readingsList.add(reading);
                }
                double currday = 0;
                for(int i = 0; i < readingsList.size(); i++){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(readingsList.get(i).timestamp);
                    currday = cal.get(cal.DAY_OF_MONTH);

                    Reading tempr = new Reading();
                    tempr = readingsList.get(i);
                    double average = 0.0;
                    if(readingsList.get(i).value.contains(" "))
                        average = Double.parseDouble(readingsList.get(i).value.substring(0, readingsList.get(i).value.indexOf(" ")));
                    else
                        average = Double.parseDouble(readingsList.get(i).value);
                    double count = 1.0;
                    i++;

                    if(i<readingsList.size())
                        cal.setTime(readingsList.get(i).timestamp);

                    while(currday == (int)cal.get(cal.DAY_OF_MONTH) && i<readingsList.size()){
                        count+=1.0;
                        if(readingsList.get(i).value.contains(" "))
                            average += Double.parseDouble(readingsList.get(i).value.substring(0, readingsList.get(i).value.indexOf(" ")));
                        else
                            average += Double.parseDouble(readingsList.get(i).value);
                        i++;
                        if(i<readingsList.size())
                            cal.setTime(readingsList.get(i).timestamp);
                    }

                    tempr.value = average/count+"";
                    displayList.add(tempr);
                    i--;
                }

                populateGraph();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
