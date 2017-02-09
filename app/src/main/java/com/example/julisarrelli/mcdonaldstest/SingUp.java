
package com.example.julisarrelli.mcdonaldstest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
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


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;



public class SingUp extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private ImageButton addPhoto;
    String userPhoto= "";

    Platform platform=Platform.getInstance();



    @InjectView(R.id.input_username) EditText usernameText;
    @InjectView(R.id.input_password) EditText passText;
    @InjectView(R.id.input_repeatpassword) EditText repeatPasswordText;
    private boolean UserHasPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);



        ButterKnife.inject(this);

        addPhoto= (ImageButton) findViewById(R.id.addphoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                           CAMERA_REQUEST);
                }


                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);





            }
        });


        final Button button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SingUpuser();
            }
        });




        UserHasPhoto=false;

    }
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Drawable image= new BitmapDrawable(getResources(),photo);
            userPhoto=getStringImage(photo);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addPhoto.setBackground(image);

            }

            UserHasPhoto=true;
        }
    }




    public void SingUpuser() {


        String username = usernameText.getText().toString();
        String pass = passText.getText().toString();
        String repeatPassword = repeatPasswordText.getText().toString();

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
        else if(!UserHasPhoto)
        {
            Toast.makeText(getBaseContext(), "Necesita una foto", Toast.LENGTH_LONG).show();

        }


        else {
            if (!platform.cheackUsername(username)) {
              

               insertToDatabase(username,pass);
            } else {
                Toast.makeText(getBaseContext(), "Usuario existente, ingrese otro", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void insertToDatabase(final String username, final String pass) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramPass = params[1];



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("pass", pass));
                nameValuePairs.add(new BasicNameValuePair("type", "classic"));
                nameValuePairs.add(new BasicNameValuePair("photo", String.valueOf(userPhoto)));
                Log.v("id", String.valueOf(userPhoto.length()));


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
