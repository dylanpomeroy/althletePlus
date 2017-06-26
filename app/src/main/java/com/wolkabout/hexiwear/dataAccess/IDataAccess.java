package com.wolkabout.hexiwear.dataAccess;

import java.util.ArrayList;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public interface IDataAccess {
    void updateFirebase();
    ArrayList<Reading> getCurrentReadings();
    Reading getCurrentReading(ReadingType type);
    void addReading(ReadingType type, )
}
