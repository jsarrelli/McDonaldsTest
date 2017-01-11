package com.example.julisarrelli.mcdonaldstest.JavaClases;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Platform {

    public static HashMap<Integer,Form>forms;
    public static HashMap<Integer,Local>locals;
    public static HashMap<Integer,User>users;
    public static int idLocalToEvaluate ;
    public static int idFormToComplete;
    public static boolean uservalidated;


    public static Platform instance=null;

    public static Platform getInstance() {
        if (instance == null) {
            instance = new Platform();
            forms=new HashMap<Integer, Form>();
            locals=new HashMap<Integer, Local>();
            users=new HashMap<Integer, User>();
            uservalidated=false;

        }

        return instance;
    }

    public void addLocal(Local local)
    {
        locals.put(local.getIdLocal(),local);
    }

    public void addForm(Form form)
    {
        forms.put(form.getIdForm(),form);
    }
    public void addUser(int idUser,User user)
    {
        users.put(idUser,user);
    }

    public static int getIdFormToComplete() {
        return idFormToComplete;
    }

    public static void setIdFormToComplete(int idFormToComplete) {
        Platform.idFormToComplete = idFormToComplete;
    }

    public static int getIdLocalToEvaluate() {

        return idLocalToEvaluate;
    }

    public static void setIdLocalToEvaluate(int idLocalToEvaluate) {
        Platform.idLocalToEvaluate = idLocalToEvaluate;
    }

    public String getLocalAdress(){
        return locals.get(idLocalToEvaluate).getAdress();
    }

    public static void setUservalidated(boolean uservalidated) {
        Platform.uservalidated = uservalidated;
    }

    public static boolean isUservalidated() {
        return uservalidated;
    }

    public boolean ValidateUser(String username, String pass){

        Set<Integer> keys=users.keySet();

        for(Integer key:keys)
        {
            if((users.get(key).getUsername().equals(username)&&(users.get(key).getPass().equals(pass))))
            {
                uservalidated=true;
                return true;
            }

        }

        return false;

 }


}
