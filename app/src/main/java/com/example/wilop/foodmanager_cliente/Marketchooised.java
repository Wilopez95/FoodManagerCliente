package com.example.wilop.foodmanager_cliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.wilop.foodmanager_cliente.MainActivity.sharedPreferences;

public class Marketchooised extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        TextView market_name;
        TextView location;
        TextView phone;
        Button aceptar;
        String marketid;
        String marketname;
        String marketlocation;
        String marketphone;
        JSONObject myMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketchooised);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        market_name =findViewById(R.id.market_name);
        location =  findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        aceptar = findViewById(R.id.okbutton);
        String marketId = sharedPreferences.getString("marketId", "");
        try {
            myMarket = new JSONObject(marketId);
            //Log.i("TEST",myMarket.toString());
            marketid = myMarket.getString("_id");
            marketname = myMarket.getString("name");
            marketlocation = myMarket.getString("location");
            marketphone = myMarket.getString("phone");
            market_name.setText(marketname);
            location.setText(marketlocation);
            phone.setText(marketphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.marketchooised, menu);
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

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_card) {

        } else if (id == R.id.nav_historial) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_setings) {

        } else if (id == R.id.nav_exit) {
            sharedPreferences.edit().putString("userid", "").apply();
            sharedPreferences.edit().putString("token", "").apply();
            sharedPreferences.edit().putString("name", "").apply();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getMarket(String _id){

    }
}
