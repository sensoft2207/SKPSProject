package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 21/8/17.
 */

public class RpdData {

    String rpd_name;
    String rpd_name2;
    String hot_yes;
    String hot_no;
    String situation;
    boolean hot_selected;

    public boolean isHot_selected() {
        return hot_selected;
    }

    public void setHot_selected(boolean hot_selected) {
        this.hot_selected = hot_selected;
    }


    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getHot_yes() {
        return hot_yes;
    }

    public void setHot_yes(String hot_yes) {
        this.hot_yes = hot_yes;
    }

    public String getHot_no() {
        return hot_no;
    }

    public void setHot_no(String hot_no) {
        this.hot_no = hot_no;
    }



    public String getRpd_name2() {
        return rpd_name2;
    }

    public void setRpd_name2(String rpd_name2) {
        this.rpd_name2 = rpd_name2;
    }



    public String getRpd_name() {
        return rpd_name;
    }

    public void setRpd_name(String rpd_name) {
        this.rpd_name = rpd_name;
    }



}
