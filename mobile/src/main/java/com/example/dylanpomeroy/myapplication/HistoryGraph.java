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

import static android.app.PendingIntent.getActivity;

/**
 * Created by Owner on 2017-07-13.
 */

public class HistoryGraph extends AppCompatActivity{
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private AppData app;
    String number1 = "4", number2 = "6";
    Button click, click2;
    TextView pullData;
    String text;
    Spinner spinner_1;
    GraphView graph;// = (GraphView) findViewById(R.id.graph);
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_1 = (Spinner) findViewById(R.id.spinzy);
        //pullData = (TextView) findViewById(R.id.sampletext);
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

                graph.removeAllSeries();//reset graph
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(String.valueOf(ds.getKey()));
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(HistoryGraph.this, android.R.layout.simple_spinner_item, list);
                    spinner_1.setAdapter(myAdapter);

                    /**
                    * Do graph stuff here
                    */

                }
                graph.addSeries(series);
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
     * obtains the data value and sets it to strings
     * @param dataSnapshot
     * @param key
     */
    /*
    private void dataValue(DataSnapshot dataSnapshot, String key) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) { //gets data from firebase
            FireApp uInfo = new FireApp();
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
            uInfo.setNumber1(ds.child(key).getValue(FireApp.class).getNumber1());
            uInfo.setNumber2(ds.child(key).getValue(FireApp.class).getNumber2());
            uInfo.setNumber3(ds.child(key).getValue(FireApp.class).getNumber3());


            int data1 = Integer.valueOf(uInfo.getNumber1());
            int data2 = Integer.valueOf(uInfo.getNumber2());
            int data3 = Integer.valueOf(uInfo.getNumber3());
            //String data2 = String.valueOf(uInfo.getNumber2());
            //String data3 = String.valueOf(uInfo.getNumber3());

            Date date1 = null;
            Date date2 = null;
            Date date3 = null;
            Date date4 = null;

            try {
                //date1 = originalFormat.parse(data1);
                date1 = originalFormat.parse("2000/01/23");
            } catch(ParseException e) {}
            try {
                //date2 = originalFormat.parse(data2);
                date2 = originalFormat.parse("2000/01/24");
            } catch(ParseException e) {}
            try {
                //date3 = originalFormat.parse(data3);
                date3 = originalFormat.parse("2000/01/25");
            } catch(ParseException e) {}

            try {
                //date3 = originalFormat.parse(data3);
                date4 = originalFormat.parse("2000/01/26");
            } catch(ParseException e) {}



            //ArrayList<String> array = new ArrayList<>();
            //array.add(data1);
           // array.add(data2);

//            pullData.setText(array.toString());


           graph.removeAllSeries();

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(date1, 1),
                    new DataPoint(date2, 2),
                    new DataPoint(date3, 3)
                    //new DataPoint(data1, 1),
                    //new DataPoint(data2, 2),
                    //new DataPoint(data3, 3)
                    //new DataPoint(3, 6)
            });



            //graph.addSeries(series);


            // set date label formatter
            //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(app));
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), originalFormat));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(date1.getTime());
            graph.getViewport().setMaxX(date3.getTime());
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScrollableY(true);
            graph.getViewport().setXAxisBoundsManual(true);

            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            //graph.getGridLabelRenderer().setHumanRounding(false);


        }
    }*/

    private void dataValue(DataSnapshot dataSnapshot, String key) {
        graph.removeAllSeries();
        DataPoint[] points = new DataPoint[list.size()];
        int pointCount = 0;
        //SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd");
          // 20120821

        DataSnapshot temp = dataSnapshot.child("Name");


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (DataSnapshot ds : temp.getChildren()) { //gets data from firebase
            FireApp uInfo = new FireApp();

            int size = list.size();

            uInfo.setNumber1(ds.getValue(FireApp.class).getNumber1());
            uInfo.setNumber2(ds.getKey());
            String t1 = uInfo.getNumber1();
            String t2 = uInfo.getNumber2();
            //uInfo.setNumber1(ds.child(key).getValue(FireApp.class).getNumber1());
            //uInfo.setNumber2(ds.child(key).getValue(FireApp.class).getNumber2());


            int data1 = Integer.valueOf(uInfo.getNumber1());

            Date date1 = null;

            try {
                //date1 = originalFormat.parse(data1);
                date1 = originalFormat.parse(uInfo.getNumber2());
            } catch(ParseException e) {}
            //String formattedDate = targetFormat.format(date1);
            //Date1 = formattedDate.p

            points[pointCount] = new DataPoint(date1, data1);
            //points[pointCount] = new DataPoint(formattedDate, data1);


            series.appendData(points[pointCount], false, list.size());
            //series.appendData(points[pointCount], true, list.size());

            pointCount ++;
        }


        /*for(int i = 0; i < 0; i++){
            FireApp uInfo = new FireApp();

            int size = list.size();
            String teststring = list.get(i);
            uInfo.setNumber1(temp.child(list.get(i)).getValue(FireApp.class).getNumber1());
            //uInfo.setNumber1(dataSnapshot.child(list.get(i)).getValue(FireApp.class).getNumber1());
            uInfo.setNumber2(list.get(i));
            String t1 = uInfo.getNumber1();
            String t2 = uInfo.getNumber2();
            //uInfo.setNumber1(ds.child(key).getValue(FireApp.class).getNumber1());
            //uInfo.setNumber2(ds.child(key).getValue(FireApp.class).getNumber2());


            int data1 = Integer.valueOf(uInfo.getNumber1());

            Date date1 = null;

            try {
                //date1 = originalFormat.parse(data1);
                date1 = originalFormat.parse(uInfo.getNumber2());
            } catch(ParseException e) {}
            points[pointCount] = new DataPoint(date1, data1);

            //series.appendData(points[pointCount], false, list.size());
            series.appendData(points[pointCount], true, list.size());

            pointCount ++;
        }*/

        graph.addSeries(series);

        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(app));
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), originalFormat));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(list.size()/2); // was 3. only 4 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(list.size()/2);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);


        // set manual x bounds to have nice steps
        //graph.getViewport().setMaxY(30);
        graph.getViewport().setMinY(-1);
        graph.getViewport().setMinX(points[0].getX());
        graph.getViewport().setMaxX(points[list.size()-1].getX());
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setXAxisBoundsManual(true);
    }

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
