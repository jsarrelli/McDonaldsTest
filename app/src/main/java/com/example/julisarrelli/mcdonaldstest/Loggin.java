
package com.example.julisarrelli.mcdonaldstest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Local;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;
import com.example.julisarrelli.mcdonaldstest.JavaClases.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.julisarrelli.mcdonaldstest.R.id.listView;
import static com.example.julisarrelli.mcdonaldstest.R.id.text;

public class Loggin extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    Platform platform=Platform.getInstance();

    ArrayList<HashMap<String, String>> usersList;


    // url to get all products list
    private static String url_getallusers = "http://julisarrellidb.hol.es/mcconnect/getallusers.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "users";
    private static final String TAG_ID = "iduser";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASS = "pass";


    // products JSONArray
    JSONArray products = null;

    ListView list;



    @InjectView(R.id.input_username) EditText usernameText;
    @InjectView(R.id.input_password) EditText passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);


        ButterKnife.inject(this);
        // Hashmap para el ListView
        usersList= new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllUsers().execute();


        final Button button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Validate();
            }
        });





    }


    class LoadAllUsers extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         */
        @Override
//       Override protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Loggin.this);
//            pDialog.setMessage("Inicializando...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }

        /**
         * obteniendo todos los productos
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_getallusers, "GET", params);

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
                        String username = c.getString(TAG_USERNAME);
                        String pass = c.getString(TAG_PASS);

                        //obtenemos todos los usuarios en la base y los cargamos en el hashmap,
                        //si la base llegase a tener muchos usuarios esto no es conveniente
                        //ya que le demanda mucho al procesador del telefono

                        User user=new User(username,pass);
                        platform.addUser(Integer.parseInt(id),user);



                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    public void Validate()
    {


        String username=usernameText.getText().toString();
        String pass=passText.getText().toString();
        User user=new User(username,pass);



        if (username.isEmpty()) {
            usernameText.setError("Ingrese un usuario");
        }
        if (pass.isEmpty()) {
            passText.setError("Ingrese su clave");
        }
        else if(platform.ValidateUser(user))
        {
            Intent intent = new Intent(Loggin.this, MainActivity.class);

            startActivityForResult(intent, 0);
            finish();
        }

        else {
            Toast.makeText(getBaseContext(), "Usuario no encontrado", Toast.LENGTH_LONG).show();
        }
    }


}
