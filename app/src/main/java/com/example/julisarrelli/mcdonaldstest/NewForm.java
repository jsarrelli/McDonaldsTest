package com.example.julisarrelli.mcdonaldstest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Question;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewForm extends AppCompatActivity {

    @InjectView(R.id.text1) EditText text1;
    @InjectView(R.id.text2) EditText text2;
    @InjectView(R.id.text3) EditText text3;
    @InjectView(R.id.text4) EditText text4;
    @InjectView(R.id.text5) EditText text5;
    @InjectView(R.id.text6) EditText text6;
    @InjectView(R.id.text7) EditText text7;
    @InjectView(R.id.text8) EditText text8;
    @InjectView(R.id.text9) EditText text9;
    @InjectView(R.id.text10) EditText text10;
    @InjectView(R.id.text11) EditText text11;
    @InjectView(R.id.text12) EditText text12;
    @InjectView(R.id.text13) EditText text13;
    @InjectView(R.id.text14) EditText text14;
    @InjectView(R.id.text15) EditText text15;
    @InjectView(R.id.text16) EditText text16;
    @InjectView(R.id.text17) EditText text17;
    @InjectView(R.id.text18) EditText text18;
    @InjectView(R.id.text19) EditText text19;
    @InjectView(R.id.text20) EditText text20;

    @InjectView(R.id.NameText) EditText FormNameText;
    Platform platform=Platform.getInstance();


    ArrayList<View> questions;
    ArrayList<String>questionsTexts;

    int buttonCount=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_form);

        questions=new ArrayList<View>();
        questionsTexts=new ArrayList<String>();
        ButterKnife.inject(this);

        loadQuestions();



        final Button NewQuestion = (Button) findViewById(R.id.New_Question);
        NewQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(buttonCount==19)
                {
                    NewQuestion.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "Limite de preguntas alcanzado", Toast.LENGTH_LONG).show();

                }
                questions.get(buttonCount).setVisibility(View.VISIBLE);
                buttonCount++;

            }
        });

        final Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String formName=FormNameText.getText().toString();
                ArrayList<String> questionsToDatabase=new ArrayList<String>();
                loadQuestionsTexts();

              if(formName.isEmpty()){
                  FormNameText.requestFocus();
                  FormNameText.setError("Ingrese un nombre para el formulario");

              }

              else if(platform.checkFormExist(formName))
              {
                  Toast.makeText(getBaseContext(), "Existe ese formulario ya existe", Toast.LENGTH_SHORT).show();



              }

            else if(checkAllEmptyQuestions())
             {
                 Toast.makeText(getBaseContext(), "Ingrese al menos una pregunta", Toast.LENGTH_SHORT).show();


             }

                else{

                  insertForm(formName);
                  for(int i=0;i<20;i++)
                  {
                      //si la pregunta no esta vacia se agrega al array
                      if(!questionsTexts.get(i).isEmpty())
                      {
                          //questionsToDatabase.add(questionsTexts.get(i));
                          insertQuestions(questionsTexts.get(i),i);
                      }

                  }

                  Toast.makeText(getBaseContext(), "Formulario ingresado con exito", Toast.LENGTH_SHORT).show();

                  Intent intent=new Intent(NewForm.this,EditForms.class);
                  startActivityForResult(intent,0);
                  finish();
              }

            }
        });


    }



    private void insertQuestions(final String question,final int id) {



            class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
                @Override
                protected String doInBackground(String... params) {
                    String paramidquestion = params[0];

                     List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("idQuestion", String.valueOf(id)));
                    nameValuePairs.add(new BasicNameValuePair("question", question));
                    nameValuePairs.add(new BasicNameValuePair("idForm", String.valueOf(platform.lastFormId())));


                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(
                                "http://julisarrellidb.hol.es/mcconnect/insertquestions.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = httpClient.execute(httpPost);

                        HttpEntity entity = response.getEntity();


                    } catch (ClientProtocolException e) {

                    } catch (IOException e) {

                    }
                    return "success";
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);



                }
            }
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(question);




    }

    private void insertForm(final String formName) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramidForm = params[0];
                String paramForName = params[1];



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idForm", String.valueOf(platform.lastFormId())));
                nameValuePairs.add(new BasicNameValuePair("name", formName));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/insertforms.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(String.valueOf(platform.lastFormId()), formName);
    }

    //esta funcion nos dice si el usuario no lleno ningun campo de preguntas
    private boolean checkAllEmptyQuestions() {
        boolean flag=true;



        for (String question:questionsTexts)
        {

            if(!question.isEmpty()){
                 flag=false;
            }
        }
        return flag;
    }

    private void loadQuestionsTexts() {

        questionsTexts.add(text1.getText().toString());
        questionsTexts.add(text2.getText().toString());
        questionsTexts.add(text3.getText().toString());
        questionsTexts.add(text4.getText().toString());
        questionsTexts.add(text5.getText().toString());
        questionsTexts.add(text6.getText().toString());
        questionsTexts.add(text7.getText().toString());
        questionsTexts.add(text8.getText().toString());
        questionsTexts.add(text9.getText().toString());
        questionsTexts.add(text10.getText().toString());
        questionsTexts.add(text11.getText().toString());
        questionsTexts.add(text12.getText().toString());
        questionsTexts.add(text13.getText().toString());
        questionsTexts.add(text14.getText().toString());
        questionsTexts.add(text15.getText().toString());
        questionsTexts.add(text16.getText().toString());
        questionsTexts.add(text17.getText().toString());
        questionsTexts.add(text18.getText().toString());
        questionsTexts.add(text19.getText().toString());
        questionsTexts.add(text20.getText().toString());
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
