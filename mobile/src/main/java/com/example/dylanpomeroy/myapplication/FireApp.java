package com.example.dylanpomeroy.myapplication;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireApp implements Serializable {

    public String uid;
    public double number1 ,number2, number3, number4;

    public FireApp(){
    }

    public FireApp(String uid, double number1, double number2, double number3, double number4){
        this.uid = uid;
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
        this.number4 = number4;

    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("number1",number1);
        result.put("number2",number2 );
        result.put("number3",number3 );
        result.put("number4",number4 );

        return result;
    }

    }

