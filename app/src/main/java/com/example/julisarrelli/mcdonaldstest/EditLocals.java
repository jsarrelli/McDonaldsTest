
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

import static com.example.julisarrelli.mcdonaldstest.R.id.EditLocals;
import static com.example.julisarrelli.mcdonaldstest.R.id.listView;

public class EditLocals extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    Platform platform=Platform.getInstance();

    ArrayList<HashMap<String, String>> usersList;


    // url to get all products list
    private static String url_getalllocals = "http://julisarrellidb.hol.es/mcconnect/getalllocals.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "locals";
    private static final String TAG_ID = "idlocal";
    private static final String TAG_ADRESS = "adress";
    private static final String TAG_CITY = "city";

    private ArrayList<String > cities;
    private ArrayList<String> adresses;
    LocalsListViewAdapter adapter;

    // products JSONArray
    JSONArray products = null;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locals);


        list = (ListView) findViewById(listView);

        adresses=new ArrayList<String>();
        cities=new ArrayList<String>();


    // Cargar los productos en el Background Thread
        new LoadAllLocals().execute();






//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                HashMap<String,String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);
//                String selectedLocal_Adress=selectedItem.get("adress");
//                String selectedLocal_City=selectedItem.get("city");
//                deleteLocal(platform.getLocalId(selectedLocal_Adress,selectedLocal_City));
//
//            }
//        });

        list.setLongClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {


                HashMap<String,String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);
                String selectedLocal_Adress=selectedItem.get("adress");
                String selectedLocal_City=selectedItem.get("city");
                deleteLocal(platform.getLocalId(selectedLocal_Adress,selectedLocal_City));


                return false;
            }});



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addLocal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent= new Intent(EditLocals.this,NewLocal.class);
                startActivityForResult(intent, 0);
                finish();

            }
        });

    }

    private void deleteLocal(final int idlocal) {


            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.mcshops)
                    .setTitle("Eliminar Local")
                    .setMessage("Desea eliminar este local? "+platform.getLocalAdress(idlocal))
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           deleteLocalFromDatabase(idlocal);
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();


    }

    private void deleteLocalFromDatabase(final int idLocal) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramposition = params[0];




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idLocal", String.valueOf(idLocal)));

                Log.v("locall", String.valueOf(idLocal));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://julisarrellidb.hol.es/mcconnect/deletelocal.php");
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


                platform.deleteLocal(idLocal);
                Toast.makeText(EditLocals.this,"Local eliminado",Toast.LENGTH_LONG);
                Intent intent=new Intent(EditLocals.this,EditLocals.class);
                startActivityForResult(intent, 0);
                finish();


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(String.valueOf(idLocal));
    }


    class LoadAllLocals extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditLocals.this);
            pDialog.setMessage("Cargando locales. Por favor espere...");
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
            JSONObject json = jParser.makeHttpRequest(url_getalllocals, "GET", params);

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
                        String adress = c.getString(TAG_ADRESS);
                        String city = c.getString(TAG_CITY);

                        adresses.add(adress);
                        cities.add(city);


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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            //cargamos la lista una vez cargado el vector con la info
            //que vamos a mandar al listAdapter
          cargarLista();
        }

    }

    private void cargarLista() {

        adapter=new LocalsListViewAdapter(this,adresses,cities);
        list.setAdapter(adapter);


    }

}
