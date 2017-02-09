package com.example.julisarrelli.mcdonaldstest.JavaClases;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class User {
    private Drawable photo;
    private String username;
    private String pass;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    public User (String username, String pass, String type, Drawable photo)
    {
        this.username=username;
        this.pass=pass;
        this.type=type;
        this.photo=photo;

    }

    public String getPass() {
        return pass;
    }

    public String getUsername() {

        return username;
    }


    public String getType() {

        return type;
    }

}
