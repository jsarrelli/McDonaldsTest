package com.example.julisarrelli.mcdonaldstest.JavaClases;

import android.util.Log;

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
    public static User loggedUser;


    public static Platform instance=null;

    public static Platform getInstance() {
        if (instance == null) {
            instance = new Platform();
            forms=new HashMap<Integer, Form>();
            locals=new HashMap<Integer, Local>();
            users=new HashMap<Integer, User>();
            uservalidated=false;
            loggedUser=null;

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

    public String getLocalToEvaluateAdress(){
        return locals.get(idLocalToEvaluate).getAdress();
    }

    public String getLocalAdress(int idLocal){
        return locals.get(idLocal).getAdress();
    }

    public static void setUservalidated(boolean uservalidated) {
        Platform.uservalidated = uservalidated;
    }

    public static boolean isUservalidated() {
        return uservalidated;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        Platform.loggedUser = loggedUser;
    }

    public boolean ValidateUser(String username, String pass){

        Set<Integer> keys=users.keySet();

        for(Integer key:keys)
        {
            if((users.get(key).getUsername().equals(username)&&(users.get(key).getPass().equals(pass))))
            {
                uservalidated=true;
                loggedUser=users.get(key);
                return true;

            }

        }

        return false;

 }

    public boolean cheackUsername(String username)
    {
        Set<Integer> keys=users.keySet();

        for(Integer key:keys)
        {
            if((users.get(key).getUsername().equals(username)))
            {
                return true;

            }

        }

        return false;
    }


    public int lastUserId()
    {
        return users.size();
    }
    public int lastLocalId()
    {
        return locals.size();
    }


    public boolean checkLocalExist(String adress,String city)
    {
        Set<Integer> keys=locals.keySet();

        for (Integer key:keys)
        {
            if((locals.get(key).getAdress().equals(adress))&&(locals.get(key).getCity().equals(city)))
            {

                return true;
            }
        }
        return false;
    }

    public void deleteLocal(int idLocal) {
        locals.remove(idLocal);
    }
}
