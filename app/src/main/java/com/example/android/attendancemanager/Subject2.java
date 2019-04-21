package com.example.android.attendancemanager;

public class Subject2 {
    private String name,marked;
    private int lecture;

    public Subject2(){}
    public Subject2(String subject,String marked,int lecture) {
        this.name = subject;
        this.marked = marked;
        this.lecture = lecture;
    }
    public String getName()
    {
        return this.name;
    }
    public String getMarked(){return marked; }
    public int getLecture(){return lecture;}
}
