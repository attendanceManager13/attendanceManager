package com.example.android.attendancemanager;

public class MainModel {
    private long id,progress;
    private String subname,status,plus,minus,progtext;
    public MainModel(long id, String subname,String status,long progress,String plus,String minus,String progtext) {
        this.id = id;
        this.subname = subname;
        this.status=status;
        this.progress=progress;
        this.plus=plus;
        this.minus=minus;
        this.progtext=progtext;
    }

    public long getId() {
        return id;
    }

    public String getSubname() {
        return subname;
    }
    public String getStatus(){
        return status;
    }
    public long getProgress(){
        return progress;
    }
    String getPlus(){
        return plus;
    }
    String getMinus(){
        return minus;
    }
    String getProgtext(){
        return progtext;
    }
}
