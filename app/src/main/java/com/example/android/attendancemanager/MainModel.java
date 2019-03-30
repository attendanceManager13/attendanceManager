package com.example.android.attendancemanager;

public class MainModel {
    private int id,progress;
    private String subname,status;
    public MainModel(int id, String subname,String status,int progress) {
        this.id = id;
        this.subname = subname;
        this.status=status;
        this.progress=progress;
    }

    public int getId() {
        return id;
    }

    public String getSubname() {
        return subname;
    }
    public String getStatus(){
        return status;
    }
    public int getProgress(){
        return progress;
    }
}
