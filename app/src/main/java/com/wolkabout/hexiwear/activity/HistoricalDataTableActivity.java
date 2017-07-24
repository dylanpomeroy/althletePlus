package com.wolkabout.hexiwear.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wolkabout.hexiwear.R;
import com.wolkabout.hexiwear.dataAccess.AppData;
import com.wolkabout.hexiwear.dataAccess.DataAccess;
import com.wolkabout.hexiwear.dataAccess.FireApp;
import com.wolkabout.hexiwear.dataAccess.Reading;
import com.wolkabout.hexiwear.dataAccess.ReadingType;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by elber on 7/24/2017.
 */

@EActivity(R.layout.activity_historical_data_table)
public class HistoricalDataTableActivity extends Activity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @ViewById(R.id.pushButton)
    Button click;

    @ViewById(R.id.sampletext)
    TextView pullData;

    @ViewById(R.id.spnReadingTypes)
    Spinner readingTypesSpinner;

    @ViewById(R.id.spnReadings)
    Spinner readingsSpinner;

    String currentReadingType;

    List<String> readingTypesList = new ArrayList<>();
    List<Reading> readingsList = new ArrayList<>();
    List<String> readingTimestamps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data_table);

        FirebaseApp.initializeApp(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Althlete Plus/Test");

        getReadingTypes();
    }

    @Click(R.id.btnGetReadings)
    public void getReadings(View view){
        String readingType = readingTypesSpinner.getSelectedItem().toString();

        getReadings(readingType);
    }

    @Click(R.id.pushButton)
    public void pushButtonClicked(View view){
        String readingTimestamp = readingsSpinner.getSelectedItem().toString();

        for (Reading reading : readingsList){
            if (reading.timestamp.toString().equals(readingTimestamp)){
                pullData.setText(reading.value);
            }
        }
    }

    public void getReadings(String readingType){
        myRef = mFirebaseDatabase.getReference("Althlete Plus/Test/"+readingType);
        Query q = myRef.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Reading reading = ds.getValue(Reading.class);
                    readingsList.add(reading);
                    readingTimestamps.add(reading.timestamp.toString());
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(HistoricalDataTableActivity.this, android.R.layout.simple_spinner_item, readingTimestamps);
                    readingsSpinner.setAdapter(myAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(HistoricalDataTableActivity.this, android.R.layout.simple_spinner_item, readingTypesList);
                    readingTypesSpinner.setAdapter(myAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * obtains the data value and sets it to strings
     * @param dataSnapshot
     * @param key
     */
    private void dataValue(DataSnapshot dataSnapshot, String key) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) { //gets data from firebase
            FireApp uInfo = new FireApp();
            uInfo.setNumber1(ds.child(key).getValue(FireApp.class).getNumber1());
            uInfo.setNumber2(ds.child(key).getValue(FireApp.class).getNumber2());
            String data1 = String.valueOf(uInfo.getNumber1());
            String data2 = String.valueOf(uInfo.getNumber2());

            ArrayList<String> array = new ArrayList<>();
            array.add(data1);
            array.add(data2);

            pullData.setText(array.toString());
        }
    }
}
