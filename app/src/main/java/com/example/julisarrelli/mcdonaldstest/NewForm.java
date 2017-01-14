package com.example.julisarrelli.mcdonaldstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.InjectView;

public class NewForm extends AppCompatActivity {

//    @InjectView(R.id.input_username) EditText;
//    @InjectView(R.id.input_password) EditText passText;
//    @InjectView(R.id.input_repeatpassword) EditText repeatPasswordText;

    ArrayList<View> questions;
    int buttonCount=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_form);

        questions=new ArrayList<View>();

        loadQuestions();
        final Button button = (Button) findViewById(R.id.New_Question);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    if(buttonCount==19)
                    {
                        button.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), "Limite de preguntas alcanzado", Toast.LENGTH_LONG).show();

                    }



                    questions.get(buttonCount).setVisibility(View.VISIBLE);
                    buttonCount++;

            }
        });

    }

    private void loadQuestions() {

        questions.add(findViewById(R.id.question1));
        questions.add(findViewById(R.id.question2));
        questions.add(findViewById(R.id.question3));
        questions.add(findViewById(R.id.question4));
        questions.add(findViewById(R.id.question5));
        questions.add(findViewById(R.id.question6));
        questions.add(findViewById(R.id.question7));
        questions.add(findViewById(R.id.question8));
        questions.add(findViewById(R.id.question9));
        questions.add(findViewById(R.id.question10));
        questions.add(findViewById(R.id.question11));
        questions.add(findViewById(R.id.question12));
        questions.add(findViewById(R.id.question13));
        questions.add(findViewById(R.id.question14));
        questions.add(findViewById(R.id.question15));
        questions.add(findViewById(R.id.question16));
        questions.add(findViewById(R.id.question17));
        questions.add(findViewById(R.id.question18));
        questions.add(findViewById(R.id.question19));
        questions.add(findViewById(R.id.question20));

    }




    }
