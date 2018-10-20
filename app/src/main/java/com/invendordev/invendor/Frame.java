package com.invendordev.invendor;

public class Frame {

    String name;
    int timesUp;
    int timesDown;

    Frame(){
        name = "";
        timesUp = 0;
        timesDown = 0;
    }

    Frame(String frameName){
        name = frameName;
        timesUp = 0;
        timesDown = 0;
    }

    public int getTimesUp() {
        return timesUp;
    }
    public int getTimesDown(){
        return timesDown;
    }

    public void setTimesUp(int i){
        timesUp = i;
    }
    public void setTimesDown(int i){
        timesDown = i;
    }
}
