package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 13/7/17.
 */

public class ArticlesData {

    public String name;
    public String id;
    public String count_question;
    public String category_like_count;
    public String photo;

    public String getCategory_like_count() {
        return category_like_count;
    }

    public void setCategory_like_count(String category_like_count) {
        this.category_like_count = category_like_count;
    }



    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCount_question() {
        return count_question;
    }

    public void setCount_question(String count_question) {
        this.count_question = count_question;
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


}
