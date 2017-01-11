package com.example.julisarrelli.mcdonaldstest.JavaClases;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class User {
    private String username;
    private String pass;
    private String type;

    public User (String username,String pass,String type)
    {
        this.username=username;
        this.pass=pass;
        this.type=type;
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
