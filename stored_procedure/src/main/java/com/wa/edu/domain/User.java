package com.wa.edu.domain;

/**
 * Created by dell on 15-11-6.
 **/
public class User {

    private int id;
    private String username;
    private String userpass;

    public User() {

    }

    public User(String username, String userpass) {
        super();
        this.username = username;
        this.userpass = userpass;
    }


    public User(int id, String username, String userpass) {
        super();
        this.id = id;
        this.username = username;
        this.userpass = userpass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", userpass="
                + userpass + "]";
    }



}
