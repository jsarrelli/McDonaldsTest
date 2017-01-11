package com.example.julisarrelli.mcdonaldstest.JavaClases;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Answer {

    private String answer;
    private String observation;

    public Answer(String answer,String observation) {
        this.answer = answer;
        this.observation=observation;
    }

    public String getAnswer() {
        return answer;
    }

    public String getObservation(){
        return observation;
    }

}
