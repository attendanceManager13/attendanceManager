package com.example.android.attendancemanager;

public class Subject2 {
    private String name,marked;
    private int lecture;
    /*private int attended_lectures;
    private int total_lectures;
    private float percentage;*/
    private float percentage;
    public Subject2(){}
    public Subject2(String subject,String marked,int lecture) {
        this.name = subject;
        this.marked = marked;
        this.lecture = lecture;
        //this.percentage = percentage;
        /*this.attended_lectures = attended_lectures;
        this.total_lectures = total_lectures;
        this.percentage =percentage;*/

    }



    public String getName()
    {
        return this.name;
    }

    //public int getPriority(){
    //return this.priority;
    //}

    /*public int getAttended_lectures() {
        return attended_lectures;
    }

    public float getPercentage() {
        return percentage;
    }

    public int getTotal_lectures() {
        return total_lectures;
    }*/
    public float getPercentage() {
        return percentage;
    }

    public String getMarked(){return marked; }

    public int getLecture(){return lecture;}
}
