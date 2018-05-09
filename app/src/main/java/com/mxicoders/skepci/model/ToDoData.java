package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 24/7/17.
 */

public class ToDoData {

    public String name ,date;
    public int omg;
    public  String task_his_id;
    public  String timing;

    String d_date,d_day;

    public String getD_date() {
        return d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getD_day() {
        return d_day;
    }

    public void setD_day(String d_day) {
        this.d_day = d_day;
    }

    public String gettiming() {
        return timing;
    }

    public void settiming(String emotion) {
        this.timing = emotion;
    }

    public ToDoData() {

    }

    public String getTask_his_id() {
        return task_his_id;
    }

    public void setTask_his_id(String task_his_id) {
        this.task_his_id = task_his_id;
    }


    public ToDoData(String name, String date/*, int omg*/) {
        this.name = name;
        this.date = date;
        this.omg = omg;
    }

    public ToDoData(String name, String d_date, String d_day) {
        this.name = name;
        this.d_day= d_day;
        this.d_date = d_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String email) {
        this.date = email;
    }

    public int getOmg() {
        return omg;
    }

    public void setOmg(int omg) {
        this.omg = omg;
    }



}
