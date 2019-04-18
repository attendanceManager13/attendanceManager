package com.example.android.attendancemanager;

import java.util.Date;

public class History {
    private String name,date,plus="P",minus="A",cancel="CANCEL",undo = "UNDO";

    private int lecture;
    public History(){}
    public History(String name, int lecture, String date) {
        this.date = date;
        this.name = name;
        this.lecture  = lecture;

    }
    public History(String date)
    {
        this.date = date;
    }


    public String getName()
    {
        return this.name;
    }
    public String getDate()
    {
        return this.date;
    }
    public int getLecture() {
        return lecture;
    }
    String getCancel() {
        return cancel;
    }

    String getMinus() {
        return minus;
    }

    String getPlus() {
        return plus;
    }

    String getUndo() {
        return undo;
    }
}