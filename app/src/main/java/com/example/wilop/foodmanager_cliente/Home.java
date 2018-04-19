package com.example.wilop.foodmanager_cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.wilop.foodmanager_cliente.MainActivity.sharedPreferences;

public class Home extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
        JSONArray arrayJSON;
        ArrayList<String> mercados = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter;
        ListView List_markets;
        public static JSONObject Choosemarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        List_markets = findViewById(R.id.List_markets);
        getMarkets();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mercados);

        List_markets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Posision ", Integer.toString(position));
                try {
                    Choosemarket = new JSONObject(arrayJSON.get(position).toString());
                    //Log.i("Choosemarket ", Choosemarket.toString());
                    sharedPreferences.edit().putString("marketId",Choosemarket.toString() ).apply();
                    Intent intent = new Intent(getApplicationContext(),Marketchooised.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        getMenuInflater().inflate(R.menu.home, menu);
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
            Log.i("NAV","HOME");
        } else if (id == R.id.nav_card) {
            Log.i("NAV","CARD");
        } else if (id == R.id.nav_historial) {
            Log.i("NAV","HISTORIAL");
        }else if (id == R.id.nav_help) {
            Log.i("NAV","HELP");
        } else if (id == R.id.nav_setings) {
            Log.i("NAV","SETINGS");
        }else if (id == R.id.nav_exit) {
            Log.i("NAV","EXIT");
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
    private void getMarkets(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "https://food-manager.herokuapp.com/markets/", null,
            new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response) {
                    // display response
                    //Log.d("Response", response.toString());
                    try{
                        JSONObject jsonObject = new JSONObject(response.toString());
                        arrayJSON = jsonObject.getJSONArray("products");
                        for (int i = 0; i< arrayJSON.length(); i++){
                            JSONObject tmpjsonObject = new JSONObject(arrayJSON.get(i).toString());
                            String name =  tmpjsonObject.getString("name");
                            String location =  tmpjsonObject.getString("location");
                            mercados.add(name.toUpperCase()+"\n"+location);
                        }
                        List_markets.setAdapter(arrayAdapter);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", "No hay locales");
                }
            }
    );
    // add it to the RequestQueue
    RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(getRequest);
}


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        return true;
    }
}
