package com.wolkabout.hexiwear.dataAccess;

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

    private Date lastSynced;

    Map<ReadingType, DataAccessReading> allReadings;

    public DataAccess(){
        allReadings = new HashMap<>();
        lastSynced = new Date(Long.MIN_VALUE);
    }

    public void syncWithFirebase(){
        // push values to firebase
        Date newLastSynced = new Date();
        List<Reading> readingsToPush = new ArrayList<>();
        for (Entry<ReadingType, DataAccessReading> daReading: allReadings.entrySet())
            readingsToPush.addAll(daReading.getValue().getReadings(lastSynced, null));
        lastSynced = newLastSynced;

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
