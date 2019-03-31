package com.example.android.attendancemanager;

public class Subject {
    private String name;
    //private int priority;
    private int attended_lectures;
    private int total_lectures;
    private int percentage;

    public Subject(){}
    public Subject(String subject,int attended_lectures,int total_lectures,int percentage) {
        this.name = subject;

        this.attended_lectures = attended_lectures;
        this.total_lectures = total_lectures;
        this.percentage =percentage;

    }



    public String getName()
    {
        return this.name;
    }

    //public int getPriority(){
        //return this.priority;
    //}

    public int getAttended_lectures() {
        return attended_lectures;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getTotal_lectures() {
        return total_lectures;
    }
}
