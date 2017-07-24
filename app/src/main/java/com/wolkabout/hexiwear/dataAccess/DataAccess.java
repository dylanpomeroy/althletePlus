package com.wolkabout.hexiwear.dataAccess;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wolkabout.hexiwear.activity.ReadingsActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public class DataAccess implements IDataAccess {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;
    private Date lastSynced;

    private static Map<ReadingType, DataAccessReading> allReadings;


    /**
     * Default constructor for data access. Uses previous lists for data if already initialized before.
     * @param appContext the context of the activity. Used for Firebase initialization.
     */
    public DataAccess(Context appContext){
        if (allReadings == null) initReadings();

        FirebaseApp.initializeApp(appContext);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("Althlete Plus");
    }

    /**
     * Constructor used only by tests in order to add values. Cannot perform Firebase operations
     */
    public void initReadings(){
        allReadings = new HashMap<>();
        for (ReadingType type : ReadingType.values()) {
            DataAccessReading dAR = new DataAccessReading();
            //dAR.addReading(new Reading(type, null, new Date()));

            allReadings.put(type, dAR);
        }
        lastSynced = new Date(Long.MIN_VALUE);
    }

    /**
     * Wipes all the data in Firebase under a given reading type for the current user
     * @param readingType
     */
    public void wipeFirebaseData(ReadingType readingType){
        firebaseReference.child(ReadingsActivity.username).setValue("");
    }

    public void getFirebaseReadings(ReadingType readingType){
        final List<Reading> theseReadings = new ArrayList<>();

        ValueEventListener readingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Reading reading = dataSnapshot.getValue(Reading.class);
                theseReadings.add(reading);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                int i = 1;
            }
        };

        DatabaseReference readingReference = FirebaseDatabase.getInstance().getReference().child("Test").child(ReadingType.Steps.toString());
        readingReference.addListenerForSingleValueEvent(readingListener);



        int j = 1;
    }

    /**
     * Adds all the data in data access to firebase then wipes the local data
     */
    public void syncWithFirebase(){
        // push values to firebase
        Date newLastSynced = new Date();
        List<Reading> readingsToPush = new ArrayList<>();
        for (Entry<ReadingType, DataAccessReading> daReading: allReadings.entrySet())
            readingsToPush.addAll(daReading.getValue().getReadings(lastSynced, new Date()));
        lastSynced = newLastSynced;

        for (Reading reading: readingsToPush){
            firebaseReference.child(ReadingsActivity.username.split("@")[0]).child(reading.type.toString()).push().setValue(reading);
        }

        // wipe our readings now that they exist in Firebase
        initReadings();
    }

    public List<Reading> getReadings(ReadingType type, Date earliest, Date latest) {
        List<Reading> results = new ArrayList<>();

        for (Entry<ReadingType, DataAccessReading> dAReading: allReadings.entrySet())
            if (type == null || dAReading.getKey().equals(type))
                results.addAll(dAReading.getValue().getReadings(earliest, latest));

        return results;
    }

    public ArrayList<Reading> getCurrentReadings() {
        ArrayList<Reading> currentReadings = new ArrayList<>();

        for (Entry<ReadingType, DataAccessReading> entry: allReadings.entrySet())
            currentReadings.add(entry.getValue().getCurrentReading());

        return currentReadings;
    }

    public Reading getCurrentReading(ReadingType type) {
        return allReadings.get(type).getCurrentReading();
    }

    @Override
    public void addReading(Reading reading) {
        allReadings.get(reading.type).addReading(reading);
    }
}
