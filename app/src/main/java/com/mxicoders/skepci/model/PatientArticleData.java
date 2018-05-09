package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 20/7/17.
 */

public class PatientArticleData {

    public String name;
    public String email;
    public String aId;
    public String photo;
    public int omg;
    public String aCount;
    public String body;
    public String author;
    public String isLike;

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getaCount() {
        return aCount;
    }

    public void setaCount(String aCount) {
        this.aCount = aCount;
    }

    public PatientArticleData() {

    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOmg() {
        return omg;
    }

    public void setOmg(int omg) {
        this.omg = omg;
    }



}

