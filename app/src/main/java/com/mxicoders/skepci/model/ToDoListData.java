package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 21/8/17.
 */

public class ToDoListData {

    String name;
    String task_id;
    String assign_date;
    String timing;
    String d_date;
    String d_day;
    String full_date;
    String emotion_level;
    String situation;
    String thoughts;

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getSchedule_status() {
        return schedule_status;
    }

    public void setSchedule_status(String schedule_status) {
        this.schedule_status = schedule_status;
    }

    String schedule_status;


    public String getEmotion_level() {
        return emotion_level;
    }

    public void setEmotion_level(String emotion_level) {
        this.emotion_level = emotion_level;
    }


    public String getFull_date() {
        return full_date;
    }

    public void setFull_date(String full_date) {
        this.full_date = full_date;
    }



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }


}
