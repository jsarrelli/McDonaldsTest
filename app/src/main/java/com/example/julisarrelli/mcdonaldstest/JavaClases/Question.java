package com.example.julisarrelli.mcdonaldstest.JavaClases;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Question {
    private int idQuestion;
    private String question;
    Answer answer;

    public Question(int idQuestion,String question) {
        this.idQuestion = idQuestion;
        this.question=question;
        this.answer=null;
    }

    public int getIdQuestion(){
        return idQuestion;
    }

    public String getQuestion(){
        return question;
    }

    public Answer getAnswer(){
        return answer;
    }
}
