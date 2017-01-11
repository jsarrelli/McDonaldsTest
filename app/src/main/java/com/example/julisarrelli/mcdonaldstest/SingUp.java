
package com.example.julisarrelli.mcdonaldstest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

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


import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SingUp extends AppCompatActivity {


    Platform platform=Platform.getInstance();



    @InjectView(R.id.input_username) EditText usernameText;
    @InjectView(R.id.input_password) EditText passText;
    @InjectView(R.id.input_repeatpassword) EditText repeatPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);


        ButterKnife.inject(this);



        final Button button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SingUpuser();
            }
        });





    }




    public void SingUpuser() {


        String username = usernameText.getText().toString();
        String pass = passText.getText().toString();
        String repeatPassword = repeatPasswordText.getText().toString();
        Log.v("pass",username);
        Log.v("pass",pass);
        Log.v("pass",repeatPassword);
        if (username.isEmpty()) {
            usernameText.requestFocus();
            usernameText.setError("Ingrese un usuario");
        }
        if (pass.isEmpty()) {
            passText.requestFocus();
            passText.setError("Ingrese una pass");
        }

        else if (repeatPassword.isEmpty()) {
            repeatPasswordText.requestFocus();
            repeatPasswordText.setError("Confirme su clave");
        }
        else if (!pass.equals(repeatPassword)) {

            Toast.makeText(getBaseContext(), "Las claves no coinciden", Toast.LENGTH_LONG).show();

        }


        else {
            if (!platform.cheackUsername(username)) {
              

               insertToDatabase(username,pass);
            } else {
                Toast.makeText(getBaseContext(), "Usuario existente, ingrese otro", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void insertToDatabase(final String username, final String pass) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramPass = params[1];



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idUser", String.valueOf(platform.lastUserId())));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("pass", pass));
                nameValuePairs.add(new BasicNameValuePair("type", "classic"));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/insertusers.php");
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

                Toast.makeText(getBaseContext(), "Usuario insertado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SingUp.this, Login.class);

                startActivityForResult(intent, 0);
                finish();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(username, pass);
    }




}
