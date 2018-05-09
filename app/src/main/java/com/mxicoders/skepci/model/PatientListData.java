package com.mxicoders.skepci.model;

/**
 * Created by aksahy on 10/8/17.
 */

public class PatientListData {

    String unread_msg,id,name,last_name_initial,message;

    public String getUnread_msg() {
        return unread_msg;
    }

    public void setUnread_msg(String unread_msg) {
        this.unread_msg = unread_msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name_initial() {
        return last_name_initial;
    }

    public void setLast_name_initial(String last_name_initial) {
        this.last_name_initial = last_name_initial;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
