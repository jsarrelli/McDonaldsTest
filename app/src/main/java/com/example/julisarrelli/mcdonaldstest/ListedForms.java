package com.example.julisarrelli.mcdonaldstest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters.FormsListViewAdapter;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Form;
import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.julisarrelli.mcdonaldstest.R.id.listView;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView2;

public class ListedForms extends AppCompatActivity {

    Platform platform=Platform.getInstance();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> usersList;


    // url to get all products list
    private static String url_getallforms = "http://julisarrellidb.hol.es/mcconnect/getallforms.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "forms";
    private static final String TAG_ID = "idform";
    private static final String TAG_NAME = "name";
    private ArrayList<String>names;
    private FormsListViewAdapter adapter;




    // products JSONArray
    JSONArray products = null;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_forms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hashmap para el ListView
        usersList= new ArrayList<HashMap<String, String>>();

        TextView text=(TextView)findViewById(R.id.LocalToEvaluate);
        text.setText("El local a evaluar es: "+platform.getLocalToEvaluateAdress());

        // Cargar los productos en el Background Thread



        names=new ArrayList<String>();

        new LoadAllProducts().execute();






        list = (ListView) findViewById(listView2);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Form form=(Form)parent.getItemAtPosition(position);

                    platform.setIdFormToComplete(form.getIdForm());

                    Intent intent=new Intent(ListedForms.this,ListedQuestions.class);
                    startActivityForResult(intent,0);
                }
                catch (Exception e)
                {
                    Log.v("julierror","sigue siendo una garcha");
                }



            }
        });

    }


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListedForms.this);
            pDialog.setMessage("Cargando formularios. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
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

                        Form form=new Form(Integer.parseInt(id),name,null);
                        platform.addForm(form);




                       names.add(name);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
          cargarLista();

        }
    }
    private void cargarLista() {

        adapter=new FormsListViewAdapter(this,names);
        list.setAdapter(adapter);


    }

    }
