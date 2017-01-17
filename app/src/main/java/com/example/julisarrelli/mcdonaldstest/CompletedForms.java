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

import com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters.CompletedFormsListViewAdapter;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters.FormsListViewAdapter;
import com.example.julisarrelli.mcdonaldstest.JavaClases.CompletedForm;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Form;
import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Local;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.julisarrelli.mcdonaldstest.R.id.idLocal;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView2;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView4;

public class CompletedForms extends AppCompatActivity {

    Platform platform=Platform.getInstance();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> formsList;


    // url to get all products list
    private static String url_getallforms = "http://julisarrellidb.hol.es/mcconnect/getallcompletedforms.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "completedForms";
    private static final String TAG_ID = "idCompletedForm";
    private static final String TAG_NAME = "name";
    private static final String TAG_USER = "users_username";
    private static final String TAG_DATE = "date";
    private static final String TAG_LOCAL = "locals_adress";

    private static final String TAG_FORMS_IDFORM = "forms_idForm";

    private ArrayList<Integer>ids;
    private ArrayList<String>names;
    private ArrayList<String>dates;
    private ArrayList<String>users;
    private ArrayList<String>locals;


    private CompletedFormsListViewAdapter adapter;




    // products JSONArray
    JSONArray products = null;

    ListView list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_forms);


        // Hashmap para el ListView
        formsList= new ArrayList<HashMap<String, String>>();



        // Cargar los productos en el Background Thread



        names=new ArrayList<String>();
        ids=new ArrayList<Integer>();
        dates=new ArrayList<String>();
        users=new ArrayList<String>();
        locals=new ArrayList<String>();


        new LoadAllLocals().execute();
        new LoadAllCompleteForms().execute();







        list = (ListView) findViewById(listView4);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {

                    HashMap<String,String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);
                    int idCompletedForm= Integer.parseInt(selectedItem.get("id"));
                    String name= selectedItem.get("name");
                    String user= selectedItem.get("user");
                    String date= selectedItem.get("date");
                    String localAdress= selectedItem.get("local");

                    Log.v("algo",selectedItem.get("user"));
                    Log.v("algo",selectedItem.get("date"));

                    CompletedForm cp=new CompletedForm(idCompletedForm,name,0,localAdress,user,date);

                    platform.setFormToShow(cp);


                    Intent intent=new Intent(CompletedForms.this,ListedAnswers.class);
                    startActivityForResult(intent,0);
                }
                catch (Exception e) {}



            }
        });

    }


    class LoadAllCompleteForms extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompletedForms.this);
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
                        String user = c.getString(TAG_USER);
                        String date = c.getString(TAG_DATE);
                        String local = c.getString(TAG_LOCAL);
                        String forms_idForm = c.getString(TAG_FORMS_IDFORM);




//                        Form form=new Form(Integer.parseInt(id),name,null);
//                        platform.addForm(form);


                        CompletedForm cf=new CompletedForm(Integer.parseInt(id),name,Integer.parseInt(forms_idForm),local,user,date);

                        ids.add(Integer.valueOf(id));
                        names.add(name);
                        users.add(user);
                        dates.add(date);
                        locals.add(local);

                        Log.v("id", String.valueOf(ids.size()));

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


        adapter=new CompletedFormsListViewAdapter(CompletedForms.this,ids,names,users,dates,locals);
        list.setAdapter(adapter);


    }

    class LoadAllLocals extends AsyncTask<String, String, String> {

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
            JSONObject json = jParser.makeHttpRequest("http://julisarrellidb.hol.es/mcconnect/getalllocals.php", "GET", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray("locals");

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString("idlocal");
                        String adress = c.getString("adress");
                        String city = c.getString("city");



                        //cargamos el hashmap de la plataforma
                        Local local=new Local(Integer.parseInt(id),adress,city);
                        platform.addLocal(local);




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
