package com.wolkabout.hexiwear.dataAccess;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        return readings.get(readings.size() - 1);
    }

    public List<Reading> getReadings(final Date earliest, Date latest) {
        List<Reading> results = new ArrayList<>();

        for (Reading reading: readings)
            if (!reading.timestamp.before(earliest) && !reading.timestamp.after(latest))
                results.add(reading);

        return results;
    }
}
