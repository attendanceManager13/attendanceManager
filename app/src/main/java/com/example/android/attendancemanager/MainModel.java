package com.example.android.attendancemanager;

public class MainModel {
    private int id,progress;
    private String subname,status;
    MainModel(int id, String subname,String status,int progress) {
        this.id = id;
        this.subname = subname;
        this.status=status;
        this.progress=progress;
    }

    public int getId() {
        return id;
    }

    String getSubname() {
        return subname;
    }
    String getStatus(){
        return status;
    }
    int getProgress(){
        return progress;
    }
}
