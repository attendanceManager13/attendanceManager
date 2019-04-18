package com.example.android.attendancemanager;

public class MainModel {
    //private long priority;

    private String name,plus="P",minus="A",cancel="CANCEL",undo = "UNDO";

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

    String getPlus(){
        return plus;
    }
    String getMinus(){
        return minus;
    }

    String getCancel(){return cancel;}
    String getUndo(){return undo;}
}
