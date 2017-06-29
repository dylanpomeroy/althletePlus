package com.example.dylanpomeroy.myapplication;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireApp {

    public String number1 ,number2;

    public FireApp(){
    }

    public FireApp(String number1, String number2){
        this.number1 = number1;
        this.number2 = number2;
    }


    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
     }
}

