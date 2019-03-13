package com.example.android.attendancemanager;

public class Subject {
    private String name;
    private int priority;

    public Subject(){}
    public Subject(String subject,int priority) {
        this.name = subject;
        this.priority = priority;
    }


    public String getName()
    {
        return this.name;
    }
    public int getPriority(){
        return this.priority;
    }
}
