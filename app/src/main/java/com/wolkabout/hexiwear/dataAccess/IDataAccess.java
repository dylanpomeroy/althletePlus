package com.wolkabout.hexiwear.dataAccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public interface IDataAccess {
    void syncWithFirebase();
    List<Reading> getReadings(ReadingType type, Date earliest, Date latest);
    List<Reading> getCurrentReadings();
    Reading getCurrentReading(ReadingType type);
    void addReading(Reading reading);
}
