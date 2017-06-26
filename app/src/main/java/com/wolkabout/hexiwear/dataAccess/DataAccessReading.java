package com.wolkabout.hexiwear.dataAccess;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public class DataAccessReading implements IDataAccessReading {
    private ArrayList<Reading> readings;

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
}
