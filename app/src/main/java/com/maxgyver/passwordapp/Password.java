package com.maxgyver.passwordapp;

public class Password {
    public int id;
    public String website, login, password;

    public Password(String website, String login, String password) {
        this.website = website;
        this.login = login;
        this.password = password;
    }
}
