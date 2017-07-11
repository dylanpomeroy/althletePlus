package com.example.dylanpomeroy.myapplication;

import android.app.Application;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppData extends Application {

    public DatabaseReference fireBaseReference;
    public FirebaseDatabase fireBaseDBInstance;

}
