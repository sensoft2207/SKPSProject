package com.mxicoders.skepci.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mxicoders on 28/7/17.
 */

public class ItemModelTwo {

    public String name;
    public String lname;
    public String id;
    public String p_birthdate;
    private int imagePath;
    public String name2;
    public String email;
    public String city;
    public String gender;
    public String order;
    public String count_emotion;
    public String list_emotion;
    public String list_emotion_array;

    public String getList_emotion_array() {
        return list_emotion_array;
    }

    public void setList_emotion_array(String list_emotion_array) {
        this.list_emotion_array = list_emotion_array;
    }




    public String getList_emotion() {
        return list_emotion;
    }

    public void setList_emotion(String list_emotion) {
        this.list_emotion = list_emotion;
    }



    public String getCount_emotion() {
        return count_emotion;
    }

    public void setCount_emotion(String count_emotion) {
        this.count_emotion = count_emotion;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }




    public String getP_birthdate() {
        return p_birthdate;
    }

    public void setP_birthdate(String p_birthdate) {
        this.p_birthdate = p_birthdate;
    }

    public ItemModelTwo(){

    }

    public ItemModelTwo(String name,String lname,String id,int imagePath) {
        this.name = name;
        this.lname = lname;
        this.id = id;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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
