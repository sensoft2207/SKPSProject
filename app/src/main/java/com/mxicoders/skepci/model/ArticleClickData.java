package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 13/7/17.
 */

public class ArticleClickData {

    public String name;
    public String email;
    public String que_id;

    public String article_category_id;
    public String body;
    public int omg;
    public String photo;
    public String author;

    public String user_like_count;
    public String article_liked_count;

    public String is_send_before;

    public String getIs_send_before() {
        return is_send_before;
    }

    public void setIs_send_before(String is_send_before) {
        this.is_send_before = is_send_before;
    }



    public ArticleClickData(String name, String email, int omg) {
        this.name = name;
        this.email = email;
        this.omg = omg;
    }

    public String getArticle_category_id() {
        return article_category_id;
    }

    public void setArticle_category_id(String article_category_id) {
        this.article_category_id = article_category_id;
    }



    public String getUser_like_count() {
        return user_like_count;
    }

    public void setUser_like_count(String user_like_count) {
        this.user_like_count = user_like_count;
    }

    public String getArticle_liked_count() {
        return article_liked_count;
    }

    public void setArticle_liked_count(String article_liked_count) {
        this.article_liked_count = article_liked_count;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }




    public ArticleClickData() {

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
