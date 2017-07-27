package com.wolkabout.hexiwear.dataAccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public class DataAccessReading implements IDataAccessReading {
    private List<Reading> readings;

    public DataAccessReading(){
        readings = new ArrayList<>();
    }

    public void addReading(ReadingType type, String value, Date timestamp){
        readings.add(new Reading(type, value, timestamp));
    }

    public void addReading(Reading reading){
        readings.add(reading);
    }

    public Reading getCurrentReading() {
        if (readings.size() == 0){
            return null;
        }

        return readings.get(readings.size() - 1);
    }

    public List<Reading> getReadings(Date earliest, Date latest) {
        List<Reading> results = new ArrayList<>();

        if (earliest == null) earliest = new Date(Long.MIN_VALUE);
        if (latest == null) latest = new Date(Long.MAX_VALUE);
        for (Reading reading: readings)
            if (!reading.timestamp.before(earliest) && !reading.timestamp.after(latest))
                results.add(reading);

        return results;
    }
}
