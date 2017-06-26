package com.wolkabout.hexiwear.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public class DataAccess implements IDataAccess {

    Map<ReadingType, DataAccessReading> allReadings;

    public DataAccess(){
        allReadings = new HashMap<>();
    }

    public void updateFirebase() {

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
}
