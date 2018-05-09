package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 19/7/17.
 */

public class DateModel {

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String day, month, year;

    public DateModel() {

    }

    public DateModel(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = month;
    }
}
