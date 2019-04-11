package com.example.android.attendancemanager;

public class History {
    private String date,name;
    public History(String date) {
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
}