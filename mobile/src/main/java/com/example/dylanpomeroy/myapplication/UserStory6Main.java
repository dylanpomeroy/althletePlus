package com.example.dylanpomeroy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class that connects to firebase and pulls data
 * @author aqeb, Josh
 */
public class UserStory6Main extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private AppData app;
    String number1 = "4", number2 = "6";
    Button click, click2;
    TextView pullData;
    String text;
    Spinner spinner_1;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);


        spinner_1 = (Spinner) findViewById(R.id.spinzy);
        pullData = (TextView) findViewById(R.id.sampletext);
        app = ((AppData) getApplicationContext());
        click = (Button) findViewById(R.id.pushButton);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        app.fireBaseDBInstance = FirebaseDatabase.getInstance();
        app.fireBaseReference = app.fireBaseDBInstance.getReference("Name");

        pullDataFromFire();

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
     * displays the firebase data in the spinner
     */
    public void showData() {
        Query q = app.fireBaseReference.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(String.valueOf(ds.getKey()));
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UserStory6Main.this, android.R.layout.simple_spinner_item, list);
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
     * Used for pushing data to firebase
     * generating timestamps as keys
     * @param v View
     */
    public void submitInfoButton(View v) {
        String key = DateFormat.getDateTimeInstance().format(new Date());
        //app.fireBaseReference.setValue(key); //pushing data to firebase
        FireApp data = new FireApp(number1, number2);
        app.fireBaseReference.child(key).setValue(data);
    }


    /**
     *     pulling data from fireBase

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
