package com.example.julisarrelli.mcdonaldstest.JavaClases;

import java.util.HashMap;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Form {
    private int idForm;
    private String name;
    private HashMap<Integer,Question> questions;

    public Form(int idForm,String name,HashMap<Integer,Question> questions) {
        this.idForm=idForm;
        this.questions = questions;
        this.name=name;
    }


    public int getIdForm()
    {
        return idForm;
    }

    public String getName()
    {
        return name;
    }

    public HashMap<Integer,Question> getQuestions()
    {
        return questions;
    }

    public void setQuestions(HashMap<Integer,Question>questions)
    {
        this.questions=questions;
    }
}
