package com.example.julisarrelli.mcdonaldstest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.julisarrelli.mcdonaldstest.R.id.id_form;

public class ListedAnswers extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> answers;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "answers";
    private static final String TAG_ID = "idAnswer";
    private static final String TAG_ANSWER = "answer";
    private static final String TAG_OBSERVATION = "observation";
    private static final String TAG_IDFORM = "completedForms_idCompletedForm";
    private static final String TAG_QUESTION = "question";
    Platform platform;


    // products JSONArray
    JSONArray products = null;

    ListView lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_answers);

        platform=Platform.getInstance();


       TextView form=(TextView)findViewById(R.id.id_form) ;
        TextView user=(TextView)findViewById(R.id.id_user) ;
        TextView date=(TextView)findViewById(R.id.id_date) ;
        TextView local=(TextView)findViewById(R.id.id_local) ;

//
//        Log.v("String",platform.getFormToShow().getUsers_username());
//        Log.v("String",platform.getFormName(platform.getFormToShow().getForms_idForm()));
//        Log.v("String",platform.getFormToShow().getDate());


        try {
            user.setText(platform.getFormToShow().getUsers_username());
            form.setText(platform.getFormToShow().getName());
            date.setText(platform.getFormToShow().getDate());
            local.setText(platform.getFormToShow().getLocals_adress());
        }
        catch (Exception e){}

        // Hashmap para el ListView
        answers = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllAnswers().execute();
        lista = (ListView) findViewById(R.id.listview5);

    }


    class LoadAllAnswers extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListedAnswers.this);
            pDialog.setMessage("Cargando respuestas. Por favor espere...");
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
            JSONObject json = jParser.makeHttpRequest( "http://julisarrellidb.hol.es/mcconnect/getallanswers.php", "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

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
                        String answer = c.getString(TAG_ANSWER);
                        String observation = c.getString(TAG_OBSERVATION);
                        String question = c.getString(TAG_QUESTION);
                        int idForm = Integer.parseInt(c.getString(TAG_IDFORM));

                        // creating new HashMap
                        HashMap map = new HashMap();


                        if(idForm==platform.getFormToShow().getIdForm()) {
                            // adding each child node to HashMap key => value
                            map.put(TAG_ANSWER, answer);
                            map.put(TAG_OBSERVATION, observation);
                            map.put(TAG_QUESTION, question);


                            answers.add(map);
                        }
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
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ListedAnswers.this,
                            answers,
                            R.layout.singlepost,
                            new String[] {
                                    TAG_QUESTION,
                                    TAG_ANSWER,
                                    TAG_OBSERVATION
                            },
                            new int[] {
                                    R.id.id_question,
                                    R.id.id_answer,
                                    R.id.id_observation,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);
                }
            });
        }
    }

}
