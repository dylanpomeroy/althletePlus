package com.wolkabout.hexiwear.dataAccess;

import java.util.Date;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public interface IDataAccessReading {
    void addReading(ReadingType type, String value, Date timestamp);
    Reading getCurrentReading();
}
