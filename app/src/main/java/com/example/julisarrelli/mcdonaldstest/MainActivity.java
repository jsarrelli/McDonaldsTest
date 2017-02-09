package com.example.julisarrelli.mcdonaldstest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Platform platform=Platform.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if(platform.isUservalidated()) {
            if (platform.loggedUser.getType().equals("classic")) {
                hideItems();

            }
        }

        if(!platform.isUservalidated()){

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivityForResult(intent, 0);
            finish();
        }



        //se instancia el navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Button button = (Button) findViewById(R.id.Start1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListedLocals.class);
                startActivityForResult(intent, 0);
            }
        });


    }

    private void hideItems() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        nav_Menu.findItem(R.id.administrator).setVisible(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir de la Aplicacion")
                    .setMessage("Desea salir de la aplicacion?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            platform.setUservalidated(false);
                            platform.setLoggedUser(null);
                            finish();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.complete) {

            Intent intent = new Intent(MainActivity.this, ListedLocals.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.account) {


                new AlertDialog.Builder(this)
                        .setIcon(platform.getLoggedUser().getPhoto())
                        .setTitle("Mi cuenta")
                        .setMessage("Usuario actual: "+platform.getLoggedUser().getUsername())
                        .setPositiveButton("Aceptar",null)
                        .setNegativeButton("Log out", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                platform.setUservalidated(false);
                                platform.setLoggedUser(null);
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivityForResult(intent, 0);
                                finish();
                            }
                        })
                        .show();



        } else if (id == R.id.AnsweredForms) {

            Intent intent=new Intent(MainActivity.this,CompletedForms.class);
            startActivityForResult(intent,0);

        } else if (id == R.id.EditForms) {

            Intent intent=new Intent(MainActivity.this,EditForms.class);
            startActivityForResult(intent,0);




        }
        else if (id == R.id.EditLocals) {

            Intent intent = new Intent(MainActivity.this, EditLocals.class);
            startActivityForResult(intent, 0);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}

