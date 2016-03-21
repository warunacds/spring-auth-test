package com.waruna.authtest.dao;

/**
 * Created by waruna on 3/1/16.
 */

public class User {

    Integer user_id;
    String fb_id;
    String fb_auth_token;
    String username;
    String password;

    public User() {
    }


    public User(User user) {
        super();
        this.user_id = user.getUser_id();
        this.username = user.getUsername();
        this.fb_auth_token = user.getFb_auth_token();
        this.fb_id = user.getFb_id();
        this.password = user.getPassword();
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getFb_auth_token() {
        return fb_auth_token;
    }

    public void setFb_auth_token(String fb_auth_token) {
        this.fb_auth_token = fb_auth_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
