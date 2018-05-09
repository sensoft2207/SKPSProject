package com.mxicoders.skepci.utils;

/**
 * Created by kuliza-195 on 11/29/16.
 */

public class ItemModel {

    public String name;
    private int imagePath;
    String body;
    String noteid;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }


    public ItemModel(){

    }

    public ItemModel(String name, int imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }
}
