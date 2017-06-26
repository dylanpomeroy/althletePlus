package com.wolkabout.hexiwear.dataAccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public interface IDataAccessReading {
    void addReading(ReadingType type, String value, Date timestamp);
    Reading getCurrentReading();
    List<Reading> getReadings(Date earliest, Date latest);
}
