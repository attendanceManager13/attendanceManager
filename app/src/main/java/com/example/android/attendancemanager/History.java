package com.example.android.attendancemanager;

import java.util.Date;

public class History {
    private String name,marked,date;

    private int lecture;
    public History(){}
    public History(String name, int lecture, String date,String mark) {
        this.date = date;
        this.name = name;
        this.lecture  = lecture;
        this.marked = mark;
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

    public String getMark() {
        return marked;
    }
}