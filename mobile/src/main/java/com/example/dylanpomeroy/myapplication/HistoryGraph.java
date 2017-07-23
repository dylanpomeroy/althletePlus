package com.example.dylanpomeroy.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;

/**
 * This class connects to FireBase and pulls the
 * @author aqeb, Josh
 */
public class HistoryGraph extends AppCompatActivity{
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private AppData app;
    Button click;
    String text;
    Spinner spinner_1, spinner_2;
    GraphView graph;
    List<String> list = new ArrayList<>();
    Intent intent = getIntent();
    //String graphInfoType = intent.getStringExtra("graphInfoType");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_1 = (Spinner) findViewById(R.id.spinzy);
        spinner_2 = (Spinner) findViewById(R.id.spinzy2);
        app = ((AppData) getApplicationContext());
        click = (Button) findViewById(R.id.pushButton);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        app.fireBaseDBInstance = FirebaseDatabase.getInstance();
        app.fireBaseReference = app.fireBaseDBInstance.getReference("Name");

        pullDataFromFire();

        graph = (GraphView) findViewById(R.id.graph);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataValue(dataSnapshot, selection());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

    }

    /**
     *
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    /**
     * displays the firebase data in the spinner
     */
    public void showData() {
        Query q = app.fireBaseReference.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(String.valueOf(ds.getKey()));
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(HistoryGraph.this, android.R.layout.simple_spinner_item, list);
                    spinner_1.setAdapter(myAdapter);
                    spinner_2.setAdapter(myAdapter);

                }
                spinner_2.setSelection(list.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    /**
     *
     * @return spinner selection
     */
    public String selection() {
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return text;
    }

    /**
     * This function takes a DataSnapshot and creates a new instance of a DataSnapshot starting at
     * the node described in the key parameter. The function loops through the new DataSnapshot and
     * adds each key value and number field as a DataPoint to the LineGraphSeries object. The series
     * is added to the graph and then the graph is give formatting.
     * @param dataSnapshot
     * @param key
     */
    private void dataValue(DataSnapshot dataSnapshot, String key) {

        graph.removeAllSeries();
        DataPoint[] points = new DataPoint[list.size()];
        int pointCount = 0;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Date dateBegin = null;
        Date dateEnd = null;
        Date currSnapshotDate = null;
        SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd");
        String upto = spinner_2.getSelectedItem().toString();
        /*
         *
         * uncomment the next line and comment or remove the line after that when the functionality
         * for sending a variable to this intent so that the datasnapshot goes to thr correct
         * place in firebase
         *
         */
        //DataSnapshot temp = dataSnapshot.child(graphInfoType);
        DataSnapshot temp = dataSnapshot.child("Name");



        //Converting the format for the selected dates from spinners
        try {
            dateBegin = originalFormat.parse(key);
            dateEnd = originalFormat.parse(upto);
        } catch(ParseException e) {}
        String dateTargetFormat = targetFormat.format(dateBegin);
        String dateTargetFormat2 = targetFormat.format(dateEnd);
        try {
            dateBegin = targetFormat.parse(dateTargetFormat);
            dateEnd = targetFormat.parse(dateTargetFormat2);
        } catch(ParseException e) {}


        for (DataSnapshot ds : temp.getChildren()) { //gets data from firebase

            FireApp uInfo = new FireApp();
            uInfo.setNumber1(ds.getValue(FireApp.class).getNumber1());
            uInfo.setNumber2(ds.getKey());

            int data1 = Integer.valueOf(uInfo.getNumber1());

            try {
                currSnapshotDate = originalFormat.parse(uInfo.getNumber2());
            } catch(ParseException e) {}

            String formattedDate = targetFormat.format(currSnapshotDate);

            try {
                currSnapshotDate = targetFormat.parse(formattedDate);
            } catch(ParseException e) {}

            if(dateBegin.getTime() <= dateEnd.getTime() && currSnapshotDate.getTime() >= dateBegin.getTime() && currSnapshotDate.getTime() <= dateEnd.getTime()){
                points[pointCount] = new DataPoint(currSnapshotDate, data1);
                series.appendData(points[pointCount], true, list.size());
                pointCount ++;
            }
        }
        if(pointCount > 0) {
            graph.addSeries(series);

            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), targetFormat));
            graph.getGridLabelRenderer().setNumHorizontalLabels(2);
            graph.getGridLabelRenderer().setNumVerticalLabels(list.size());

            graph.getViewport().setMinX(points[0].getX());
            graph.getViewport().setMaxX(points[0].getX() + (86000000 * 3));

            graph.getViewport().setMaxXAxisSize(86400000);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScrollableY(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setYAxisBoundsManual(true);
        }
    }

    /**
     *
     *
     */
    public void pullDataFromFire(){
        selection();
        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                showData();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
