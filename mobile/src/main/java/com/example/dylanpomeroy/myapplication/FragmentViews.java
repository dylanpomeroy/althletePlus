package com.example.dylanpomeroy.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.*;

/**
 * Created by thomasrs on 2017-06-01. Fragement class taken from https://developer.android.com/training/basics/fragments/creating.html
 *
 */

public class FragmentViews extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_header, container, false);
    }
}
