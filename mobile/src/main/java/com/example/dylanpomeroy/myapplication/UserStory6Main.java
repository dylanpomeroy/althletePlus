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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.*;

public class UserStory6Main extends AppCompatActivity {

    private TextView msgTxt;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mChildReference = mRootReference.child("message");
    private AppData app;
    public FireApp fire;
    double number1 = 1, number2= 3, number3=5, number4= 20;
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

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfoButton(v);
            }

            GraphView graph = (GraphView) findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(number1, number2),
                    new DataPoint(number3, number4),
            });

//            graph.addSeries(series);
        });

        click2.setOnClickListener(new View.OnClickListener() { //under test at the moment
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dsp: dataSnapshot.getChildren()){
                            FireApp post = (FireApp) dsp.getValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        });



        }

    public void submitInfoButton(View v) {
        //set up firebase
        app.fireBaseDBInstance = FirebaseDatabase.getInstance();
        app.fireBaseReference = app.fireBaseDBInstance.getReference("Name");

        String dataID = app.fireBaseReference.push().getKey();
        FireApp data = new FireApp(dataID, number1, number2, number3, number4);

        app.fireBaseReference.child(dataID).setValue(data);
        finish();
    }

}

