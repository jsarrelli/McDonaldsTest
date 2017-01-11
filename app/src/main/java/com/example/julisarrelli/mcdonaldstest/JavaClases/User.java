package com.example.julisarrelli.mcdonaldstest.JavaClases;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class User {
    private String username;
    private String pass;

    public User (String username,String pass)
    {
        this.username=username;
        this.pass=pass;
    }

    public String getPass() {
        return pass;
    }

    public String getUsername() {

        return username;
    }


}
