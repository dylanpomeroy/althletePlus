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
import com.wolkabout.hexiwear.dataAccess.ReadingType;

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

    @ViewById(R.id.spinzy)
    Spinner spinner_1;

    String text;

    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_historical_data_table);

        FirebaseApp.initializeApp(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Althlete Plus");

        showData();

        //pullDataFromFire();

//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        dataValue(dataSnapshot, selection());
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//            }
//        });

    }

    /**
     * displays the firebase data in the spinner
     */
    public void showData() {
        Query q = myRef.orderByKey();

        DataAccess dataAccess = new DataAccess(this);
        dataAccess.getFirebaseReadings(ReadingType.Steps);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(String.valueOf(ds.getKey()));
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(HistoricalDataTableActivity.this, android.R.layout.simple_spinner_item, list);
                    spinner_1.setAdapter(myAdapter);
                }
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


    /**
     *     pulling data from Firebase
     */
    public void pullDataFromFire(){
        //selection();
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
