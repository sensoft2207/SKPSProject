package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 18/8/17.
 */

public class CopingData {

    String message;
    String id;
    boolean isSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
