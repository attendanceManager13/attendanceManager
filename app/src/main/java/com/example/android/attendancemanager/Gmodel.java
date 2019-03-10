package com.example.android.attendancemanager;

public class Gmodel {
    private int id;
    private String item,nameTime;
    Gmodel(int id, String item) {
        this.id = id;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    String getItem() {
        return item;
    }

}

