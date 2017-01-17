package com.example.julisarrelli.mcdonaldstest.JavaClases;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.julisarrelli.mcdonaldstest.ListedForms;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser.is;
import static com.example.julisarrelli.mcdonaldstest.R.id.adress;
import static com.example.julisarrelli.mcdonaldstest.R.id.question1;

/**
 * Created by julisarrelli on 1/14/17.
 */

public class Database {


    Platform platform = Platform.getInstance();
    JSONParser jParser = new JSONParser();
    // products JSONArray
    JSONArray products = null;


    public static Database instance = null;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();


        }

        return instance;
    }

    public void UpdateForms() {
        class LoadAllForms extends AsyncTask<String, String, String> {
            String url_getallforms = "http://julisarrellidb.hol.es/mcconnect/getallforms.php";
            // JSON Node names
            String TAG_SUCCESS = "success";
            String TAG_PRODUCTS = "forms";
            String TAG_ID = "idform";
            String TAG_NAME = "name";


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
                JSONObject json = jParser.makeHttpRequest(url_getallforms, "GET", params);

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
                            String id = c.getString(TAG_ID);
                            String name = c.getString(TAG_NAME);


                            //cargamos el hashmap de la plataforma
                            Form form = new Form(Integer.parseInt(id), name, null);
                            platform.addForm(form);


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
    }




    }


