package com.example.dylanpomeroy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserStory6Main extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private AppData app;
    String number1 = "4", number2= "6";
    Button click, click2;
    TextView pullData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        pullData = (TextView) findViewById(R.id.Pulltext);
        app = ((AppData) getApplicationContext());
        click = (Button)findViewById(R.id.lolbutton);
        click2 = (Button)findViewById(R.id.push_button);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfoButton(v);
            }



//            graph.addSeries(series);
        });

        click2.setOnClickListener(new View.OnClickListener() { //under test at the moment
            @Override
            public void onClick(View v) {

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        showData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });

    }


    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){ //gets data from firebase
            FireApp uInfo = new FireApp();
            uInfo.setNumber1(ds.child("-KnoWzTc7s9vs5nzCqLm").getValue(FireApp.class).getNumber1());
            uInfo.setNumber2(ds.child("-KnoWzTc7s9vs5nzCqLm").getValue(FireApp.class).getNumber2());
            String data1 = String.valueOf(uInfo.getNumber1());
            String data2 = String.valueOf(uInfo.getNumber2());

            ArrayList<String> array  = new ArrayList<>();
            array.add(data1);
            array.add(data2);

            pullData.setText(array.toString());

        }
    }


    public void submitInfoButton(View v) {
        //set up firebase
        app.fireBaseDBInstance = FirebaseDatabase.getInstance();
        app.fireBaseReference = app.fireBaseDBInstance.getReference("Name");

        String dataID = app.fireBaseReference.push().getKey(); //pushing data to firebase
        FireApp data = new FireApp(number1, number2);

        app.fireBaseReference.child(dataID).setValue(data);
    }

}
