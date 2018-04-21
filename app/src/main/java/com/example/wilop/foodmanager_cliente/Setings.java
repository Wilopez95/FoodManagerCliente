package com.example.wilop.foodmanager_cliente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.Map;

import static com.example.wilop.foodmanager_cliente.Login.sharedPreferences;

public class Setings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
        Button savename;
        EditText newname;
        EditText newphone;
        TextView errorname;
        TextView errorphone;
        String UserID;
        String data;
        GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("com.example.wilop.foodmanager_cliente", Context.MODE_PRIVATE);
        UserID = sharedPreferences.getString("userid", "");
        savename = findViewById(R.id.savename);
        newname = findViewById(R.id.newname);
        newphone =  findViewById(R.id.newphone);
        errorname = findViewById(R.id.errorname);
        errorphone = findViewById(R.id.errorphone);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient =  new  GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        savename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newname.getText().toString().equals("") && newphone.getText().toString().equals("")){
                    errorname.setVisibility(View.VISIBLE);
                    errorphone.setVisibility(View.VISIBLE);
                }else if (newname.getText().toString() != "" && newphone.getText().toString().equals("")){
                    errorname.setVisibility(View.INVISIBLE);
                    errorphone.setVisibility(View.INVISIBLE);
                    Log.i("UPDATE", "UPDATE NAME");
                    data=newname.getText().toString();
                    update("name");
                }else if(newphone.getText().toString() != "" && newname.getText().toString().equals("") ){
                    errorname.setVisibility(View.INVISIBLE);
                    errorphone.setVisibility(View.INVISIBLE);
                    Log.i("UPDATE", "UPDATE PHONE");
                    data = newphone.getText().toString();
                    update("phone");
                }else {
                    errorname.setVisibility(View.INVISIBLE);
                    errorphone.setVisibility(View.INVISIBLE);
                    Log.i("UPDATE", "UPDATE ALL");
                    data=newname.getText().toString();
                    update("name");
                    data = newphone.getText().toString();
                    update("phone");
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
        getMenuInflater().inflate(R.menu.setings, menu);
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
        } else if (id == R.id.nav_carrito) {
            Intent intent = new Intent(getApplicationContext(),Orden.class);
            startActivity(intent);
        } else if (id == R.id.nav_historial) {
            Intent intent = new Intent(getApplicationContext(),Historial.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(getApplicationContext(),Help.class);
            startActivity(intent);
        } else if (id == R.id.nav_setings) {

        } else if (id == R.id.nav_exit) {
            sharedPreferences.edit().putString("userid", "").apply();
            sharedPreferences.edit().putString("token", "").apply();
            sharedPreferences.edit().putString("name", "").apply();
            revoke();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void revoke(){
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    Log.i("Info", "OK");
                }else {
                    Log.i("Info", "NOT OK");
                }

            }
        });
    }

    public void update(String type){
        String url ="https://food-manager.herokuapp.com/users/"+type+"/"+UserID;
        StringRequest putRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("value",data);


                return params;
            }

        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(putRequest);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
