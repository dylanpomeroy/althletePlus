package com.wolkabout.hexiwear.dataAccess;

import java.util.Date;

/**
 * Created by dylanpomeroy on 6/25/17.
 */

public class Reading {
    public ReadingType type;
    public String value;
    public Date timestamp;

    public Reading(ReadingType type, String value, Date timestamp){
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }
}
