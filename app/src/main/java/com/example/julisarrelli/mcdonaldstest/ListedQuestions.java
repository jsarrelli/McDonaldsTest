package com.example.julisarrelli.mcdonaldstest;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.julisarrelli.mcdonaldstest.JavaClases.Answer;
import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListedQuestions extends AppCompatActivity {

    HashMap<Integer,String>questions;

    HashMap<Integer,Answer>answers;

    Platform platform;

    JSONParser jParser = new JSONParser();
    // products JSONArray
    JSONArray products = null;


    private TextSwitcher mSwitcher;
    ImageButton btnNext;
    ImageButton btnBack;
    ImageButton btnGood;
    ImageButton btnBad;
    TextView selected;
    Button submit;
    int position;
    int lastCompletedFormId;


    @InjectView(R.id.Observation) EditText ObservationText;



    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_questions);
        platform=Platform.getInstance();
        position=0;
        questions=new HashMap<Integer, String>();
        answers=new HashMap<Integer, Answer>();


        ButterKnife.inject(this);

        new getQuestionsFromsDatabase().execute();

        btnNext=(ImageButton) findViewById(R.id.next);
        btnBack=(ImageButton) findViewById(R.id.back);
        btnGood=(ImageButton) findViewById(R.id.good);
        btnBad=(ImageButton) findViewById(R.id.bad);
        selected=(TextView) findViewById(R.id.textView3) ;
        submit=(Button)findViewById(R.id.Submit);

        mSwitcher = (TextSwitcher) findViewById(R.id.TextSwitcher_question);

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(ListedQuestions.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(15);
                myText.setTextColor(Color.BLACK);


                return myText;
            }

        });
        mSwitcher.setText(questions.get(position));



        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);


        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.v("position: ", String.valueOf(position));

                selected.setText("");
                ObservationText.setText("");

                position++;

                try{
                    selected.setText(answers.get(position).getAnswer());
                    ObservationText.setText(answers.get(position).getObservation());
                }
                catch (Exception e){}


                mSwitcher.setText(questions.get(position));

                if(position>=questions.size()-1){
                    btnNext.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                }

                else {
                    btnNext.setVisibility(View.VISIBLE);

                }



            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.v("position: ", String.valueOf(position));

                selected.setText("");
                ObservationText.setText("");


                if(position!=0) {
                    position--;
                    mSwitcher.setText(questions.get(position));
                }
                if(position<=0){
                    btnBack.setVisibility(View.INVISIBLE);


                }
                else{
                    btnBack.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);



                }

                try{
                    selected.setText(answers.get(position).getAnswer());
                    ObservationText.setText(answers.get(position).getObservation());
                }
                catch (Exception e){}








            }
        });



        btnGood.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

           Answer("Good");

                Answer("Good");

                selected.setText("Good");



            }


        });

        btnBad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Answer("Bad");
                selected.setText("Bad");


            }


        });

        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub


                submit();



            }


        });


    }

    private void submit() {



        if(questions.size()==answers.size()){

            insertIntoDatabase(answers);



        }

        else
        {
            Toast.makeText(ListedQuestions.this,"Complete todas las preguntas",Toast.LENGTH_SHORT).show();
        }
    }

    private void insertIntoDatabase(HashMap<Integer, Answer> answers) {

        insertForm();


        Intent intent=new Intent(ListedQuestions.this,MainActivity.class);
        startActivityForResult(intent,0);
        Toast.makeText(this, "Formulario enviado con exito", Toast.LENGTH_SHORT).show();

        finish();


    }

    private void insertForm() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", platform.getFormName(platform.getIdFormToComplete())));
                nameValuePairs.add(new BasicNameValuePair("idForm", String.valueOf(platform.getIdFormToComplete())));

                Log.v("questions",String.valueOf(platform.getIdLocalToEvaluate()));
                nameValuePairs.add(new BasicNameValuePair("adress", platform.getLocalToEvaluateAdress()));

                Log.v("questions",platform.getLoggedUser().getUsername());
                nameValuePairs.add(new BasicNameValuePair("username", platform.getLoggedUser().getUsername()));

                Date date=new Date();
                DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

                nameValuePairs.add(new BasicNameValuePair("date", hourdateFormat.format(date)));







                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/insertcompletedforms.php");
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

                new getCompletedForms_LastId().execute();




            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private void insertAnswer(final Integer idAnswer, final Answer answer,final String question) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idAnswer",String.valueOf(idAnswer)));
                nameValuePairs.add(new BasicNameValuePair("answer", answer.getAnswer()));

                nameValuePairs.add(new BasicNameValuePair("observation", answer.getObservation()));

                nameValuePairs.add(new BasicNameValuePair("idForm", String.valueOf(lastCompletedFormId)));
                nameValuePairs.add(new BasicNameValuePair("question", question));




                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/insertanswers.php");
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
        sendPostReqAsyncTask.execute();
    }







    class getQuestionsFromsDatabase extends AsyncTask<String, String, String> {
            String url = "http://julisarrellidb.hol.es/mcconnect/getquestions.php";
            // JSON Node names
            String TAG_SUCCESS = "success";
            String TAG_PRODUCTS = "questions";
            String TAG_ID = "idQuestion";
            String TAG_QUESTION = "question";
            String TAG_IDFORM = "forms_idForm";



            /**
             * Antes de empezar el background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            /**
             * obteniendo todos los productos
             * */
            protected String doInBackground(String... args) {
                // Building Parameters
                List params = new ArrayList();
                // getting JSON string from URL
                JSONObject json = jParser.makeHttpRequest(url, "GET", params);

                // Check your log cat for JSON reponse

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        products = json.getJSONArray(TAG_PRODUCTS);

                        // looping through All Products
                        //Log.i("ramiro", "produtos.length" + products.length());
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            int idQuestion = Integer.parseInt(c.getString(TAG_ID));
                            String question = c.getString(TAG_QUESTION);
                            int idForm = Integer.parseInt(c.getString(TAG_IDFORM));


                            if(idForm==platform.getIdFormToComplete())
                            {
                                Log.v("questions",question);
                                questions.put(idQuestion,question);
                            }
                            //cargamos el hashmap de la plataforma


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {

            }


        }


    class getCompletedForms_LastId extends AsyncTask<String, String, String> {
        String url = "http://julisarrellidb.hol.es/mcconnect/getallcompletedforms.php";
        // JSON Node names
        String TAG_SUCCESS = "success";
        String TAG_PRODUCTS = "completedForms";
        String TAG_ID = "idCompletedForm";
        String TAG_FORMS_IDFORM = "forms_idForm";



        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url, "GET", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());

                    lastCompletedFormId=0;
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable

                        int id=Integer.parseInt(c.getString(TAG_ID));

                            lastCompletedFormId = id;





                        //cargamos el hashmap de la plataforma


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.v("id", String.valueOf(lastCompletedFormId));

            Set<Integer> keys=answers.keySet();


            for(Integer key:keys)
            {Log.v("id", String.valueOf(key));
                Log.v("id",answers.get(key).getAnswer());


                insertAnswer(key,answers.get(key),questions.get(key));
            }
        }


    }

private void Answer(String answerSelected) {

    String observation=ObservationText.getText().toString();

    Answer answer=new Answer(answerSelected,observation);
    answers.put(position,answer);





    }






}
