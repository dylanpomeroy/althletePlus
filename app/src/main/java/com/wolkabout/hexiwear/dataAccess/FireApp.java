package com.wolkabout.hexiwear.dataAccess;

public class FireApp {

    public String number1 ,number2, number3;

    public FireApp(){
    }

    public FireApp(String number1, String number2, String number3){
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    public String getNumber3() {
        return number3;
    }

    public void setNumber3(String number3) {
        this.number3 = number3;
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