package com.example.julisarrelli.mcdonaldstest.JavaClases;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.julisarrelli.mcdonaldstest.ListedLocals;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Platform {

    public static HashMap<Integer,Form>forms;
    public static HashMap<Integer,Local>locals;
    public static HashMap<Integer,User>users;
    public static HashMap<Integer,CompletedForm> completedforms;
    public static int idLocalToEvaluate ;
    public static int idFormToComplete;
    public static boolean uservalidated;
    public static User loggedUser;
    public static CompletedForm formToShow;

    public static CompletedForm getFormToShow() {
        return formToShow;
    }

    public static void setFormToShow(CompletedForm formToShow) {
        Platform.formToShow = formToShow;
    }

    public static Database db;



    public static Platform instance=null;

    public static Platform getInstance() {
        if (instance == null) {
            instance = new Platform();
            forms=new HashMap<Integer, Form>();
            locals=new HashMap<Integer, Local>();
            users=new HashMap<Integer, User>();
            completedforms=new HashMap<Integer, CompletedForm>();
            uservalidated=false;
            loggedUser=null;
            db=Database.getInstance();

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

    public String getFormName(int idForm){

        return forms.get(idForm).getName();
    }

    public int getLocalId(String adress,String city)
    {
        Set<Integer>keys=locals.keySet();

        for(Integer key:keys)
        {
            if(locals.get(key).getAdress().equals(adress)&&locals.get(key).getCity().equals(city))
            {
                return key;
            }
        }
        return 0;
    }

    public int getUserIdByUsername(String Username)
    {
        Set<Integer>keys=users.keySet();

        for(Integer key:keys)
        {
            if(users.get(key).getUsername().equals(Username))
            {
                return key;
            }
        }
        return 0;
    }

    public Form getFormByName(String name)
    {
        Set<Integer>keys=forms.keySet();

        for(Integer key:keys)
        {
            if(forms.get(key).getName().equals(name))
            {
                return forms.get(key);
            }
        }
        return null;
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
        Set<Integer> keys=users.keySet();

        int max=0;
        for (Integer key:keys)
        {
            if(key>max)
            {
                max=key;
            }
        }
        return max;
    }
    public int lastLocalId()
    {
        Set<Integer> keys=locals.keySet();

        int max=0;
        for (Integer key:keys)
        {
          if(key>max)
          {
              max=key;
          }
        }
        return max;

    }

    public int lastFormId()
    {
        Set<Integer> keys=forms.keySet();

        int max=0;
        for (Integer key:keys)
        {
            if(key>max)
            {
                max=key;
            }
        }
        return max+1;
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

    public boolean checkFormExist(String name)
    {
        Set<Integer> keys=forms.keySet();

        for (Integer key:keys)
        {
            if(forms.get(key).getName().equals(name))
            {

                return true;
            }
        }
        return false;
    }

    public void deleteLocal(int idLocal) {
        locals.remove(idLocal);
    }

    public void deleteForm(int idForm) {
        forms.remove(idForm);

    }

    public void UpdateForms()
    {
        db.UpdateForms();
    }

    public void addCompletedForms(CompletedForm cp)
    {
        completedforms.put(cp.getIdForm(),cp);
    }



}
