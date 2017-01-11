package com.example.julisarrelli.mcdonaldstest.JavaClases;

import java.util.HashMap;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Platform {

   public static HashMap<Integer,Form>forms;
   public static HashMap<Integer,Local>locals;
   public static int idLocalToEvaluate ;
    public static int idFormToComplete;


    public static Platform instance=null;

    public static Platform getInstance() {
        if (instance == null) {
            instance = new Platform();
            forms=new HashMap<Integer, Form>();
            locals=new HashMap<Integer, Local>();

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
}
