package com.wolkabout.hexiwear.dataAccess;

import android.app.Application;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by elber on 7/23/2017.
 */

public class AppData extends Application {
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;
}
