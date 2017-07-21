package com.wolkabout.hexiwear.dataAccess;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private DatabaseReference firebaseReference;
    private FirebaseDatabase firebaseDBInstance;

    private Date lastSynced;

    private static Map<ReadingType, DataAccessReading> allReadings;

    public DataAccess(){
        if (allReadings == null) {
            allReadings = new HashMap<>();
            for (ReadingType type : ReadingType.values()) {
                DataAccessReading dAR = new DataAccessReading();
                dAR.addReading(new Reading(type, null, new Date()));

                allReadings.put(type, dAR);
            }
            lastSynced = new Date(Long.MIN_VALUE);
        }
    }

    public void syncWithFirebase(){
        // push values to firebase
        Date newLastSynced = new Date();
        List<Reading> readingsToPush = new ArrayList<>();
        for (Entry<ReadingType, DataAccessReading> daReading: allReadings.entrySet())
            readingsToPush.addAll(daReading.getValue().getReadings(lastSynced, null));
        lastSynced = newLastSynced;

        for (Reading reading: readingsToPush){
            firebaseReference.setValue(reading.type.toString());
            firebaseReference.child(reading.type.toString()).setValue(reading.timestamp.toString());
            firebaseReference.child(reading.type.toString()).child(reading.timestamp.toString()).setValue(reading.value);
        }

        // pull firebase values and replace our old ones
        allReadings = null; // this will be some DA from firebase
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
