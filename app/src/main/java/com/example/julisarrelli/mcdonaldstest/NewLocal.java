
package com.example.julisarrelli.mcdonaldstest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

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


import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewLocal extends AppCompatActivity {


    Platform platform=Platform.getInstance();



    @InjectView(R.id.input_adress) EditText adressText;
    @InjectView(R.id.input_city) EditText cityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_local);


        ButterKnife.inject(this);



        final Button button = (Button) findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertLocal();
            }
        });





    }




    public void insertLocal() {


        String adress= adressText.getText().toString();
        String city = cityText.getText().toString();


        if (adress.isEmpty()) {
            adressText.requestFocus();
            adressText.setError("Ingrese una direccion");
        }
        else if (city.isEmpty()) {
            cityText.requestFocus();
            cityText.setError("Ingrese una ciudad");
        }
        else {
            if (!platform.checkLocalExist(adress,city)){
                insertToDatabase(adress,city);
            } else {
                Toast.makeText(getBaseContext(), "Direccion de local existente, ingrese otra", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void insertToDatabase(final String adress, final String city) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramPass = params[1];



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idLocal", String.valueOf(platform.lastLocalId())));
                nameValuePairs.add(new BasicNameValuePair("adress", adress));
                nameValuePairs.add(new BasicNameValuePair("city", city));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/insertlocals.php");
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

                Toast.makeText(getBaseContext(), "Local insertado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewLocal.this, EditLocals.class);

                startActivityForResult(intent, 0);
                finish();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(adress, city);
    }




}
