


package com.example.julisarrelli.mcdonaldstest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters.FormsListViewAdapter;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Form;
import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters.LocalsListViewAdapter;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Local;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.julisarrelli.mcdonaldstest.R.id.EditForms;
import static com.example.julisarrelli.mcdonaldstest.R.id.EditLocals;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView1;

public class EditForms extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    Platform platform=Platform.getInstance();




    // url to get all products list
    private static String url_getallforms = "http://julisarrellidb.hol.es/mcconnect/getallforms.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "forms";
    private static final String TAG_ID = "idform";
    private static final String TAG_NAME = "name";

    private ArrayList<String > names;

    FormsListViewAdapter adapter;

    // products JSONArray
    JSONArray products = null;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_forms);



         list = (ListView) findViewById(listView1);

        names=new ArrayList<String>();

        // Cargar los productos en el Background Thread
        new LoadAllForms().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Form form=(Form)parent.getItemAtPosition(position);
                deleteForm(form.getIdForm());

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addForm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent= new Intent(EditForms.this,NewLocal.class);
                startActivityForResult(intent, 0);
                finish();

            }
        });

    }

    private void deleteForm(final int position) {


            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.forms)
                    .setTitle("Eliminar Formulario")
                    .setMessage("Desea eliminar este formulario? "+platform.getFormName(position))
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteFormFromDatabase(position);
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();


    }

    private void deleteFormFromDatabase(final int idForm) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramposition = params[0];




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idLocal", String.valueOf(idForm)));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/deleteform.php");
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


                platform.deleteForm(idForm);
                Toast.makeText(EditForms.this,"Formuluario eliminado",Toast.LENGTH_LONG);
                Intent intent=new Intent(EditForms.this,EditForms.class);
                startActivityForResult(intent, 0);
                finish();


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(String.valueOf(idForm));
    }


    class LoadAllForms extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditForms.this);
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
                Log.v("formularios","jeje");

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
                        Log.v("formularios",form.getName());
                        platform.addForm(form);
                            //falta agregar el addForms en la plataforma

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
