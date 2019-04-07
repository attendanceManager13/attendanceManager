package com.example.android.attendancemanager;

public class MainModel {
    //private long priority;
    private int progress=45;
    private String name,status="50",plus="+",minus="-",progtext="on track";
    public MainModel(){}
    public MainModel( String name) {
        //this.priority = priority;
        this.name = name;

    }

    /*public long getId() {
        return priority;
    }*/
    public String getName() {
        return name;
    }
    public String getStatus(){
        return status;
    }
    public int getProgress(){
        return progress;
    }
    String getPlus(){
        return plus;
    }
    String getMinus(){
        return minus;
    }
    String getProgtext(){ return progtext; }
}
