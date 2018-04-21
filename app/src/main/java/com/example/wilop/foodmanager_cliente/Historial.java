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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.wilop.foodmanager_cliente.Login.sharedPreferences;

public class Historial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    JSONArray arrayJSON;
    ArrayList<String> ordenes = new ArrayList<String>();
    public  static ArrayList<String> datos_orden = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    String token;
    String UserId;
    ListView Lista_Ordenes;
    TextView Empyhistorial;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("com.example.wilop.foodmanager_cliente", Context.MODE_PRIVATE);
        token =  sharedPreferences.getString("token", "");
        UserId = sharedPreferences.getString("userid", "");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Empyhistorial = findViewById(R.id.Empyhistorial);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Lista_Ordenes = findViewById(R.id.List_ordersdetails);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient =  new  GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        getOrderns();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ordenes);

        Lista_Ordenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detalle_orden(position);
                Intent intent = new Intent(getApplicationContext(),Detalles_Orden.class);
                startActivity(intent);
            }
        });
    }
    public void detalle_orden(int i){
        try {
            JSONObject tmporderdetails = new JSONObject(arrayJSON.get(i).toString());
            JSONObject tmpmarketdetails = new JSONObject(tmporderdetails.getString("market"));
            String id = tmporderdetails.getString("_id");
            String status = tmporderdetails.getString("status");
            String price = tmporderdetails.getString("price");
            String Productos = tmporderdetails.getString("products");
            String Address = tmporderdetails.getString("address");
            String Productos1 = Productos.substring(1, Productos.length()-1);
            List<String> myList = new ArrayList<String>(Arrays.asList(Productos1.split(",")));
            String  date =  tmporderdetails.getString("date");
            String[] parts = date.split("T");
            String part1 = parts[0];
            Log.i("CACHA",myList.toString());
            datos_orden.add("Orden: "+id);
            datos_orden.add("Fecha: "+part1);
            datos_orden.add("Estado de orden: "+status);
            datos_orden.add("Direccion de entrega: "+Address);
            datos_orden.add("PRODUCTOS:");
            for (int j = 0 ; j<myList.size();j++){
                datos_orden.add(myList.get(j));
            }
            datos_orden.add("Monto total: "+price);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        getMenuInflater().inflate(R.menu.historial, menu);
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
        } else if (id == R.id.nav_ayuda) {
            Intent intent = new Intent(getApplicationContext(),Help.class);
            startActivity(intent);
        } else if (id == R.id.nav_configuracion) {
            Intent intent = new Intent(getApplicationContext(),Setings.class);
            startActivity(intent);
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
    private  void getOrderns(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "https://food-manager.herokuapp.com/orders/user/"+UserId, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                           // Log.d("Response", response.toString());
                        //Empyhistorial.setVisibility(View.INVISIBLE);
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            arrayJSON = jsonObject.getJSONArray("orders");
                            for (int i = 0; i< arrayJSON.length(); i++){

                                JSONObject tmporder = new JSONObject(arrayJSON.get(i).toString());
                                String  date =  tmporder.getString("date");
                                String[] parts = date.split("T");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                String  address =  tmporder.getString("address");
                                String  price =  tmporder.getString("price");
                                JSONObject tmpmarket = new JSONObject(tmporder.getString("market"));
                                String Mname = tmpmarket.getString("name");
                                String tmpdata = "Tienda: "+Mname.toLowerCase()+"\n"+"Monto: "+price+"\n"+"Direccion: "+address+"\n"+"Fecha: "+part1;
                                Empyhistorial.setVisibility(View.INVISIBLE);
                                ordenes.add(tmpdata);

                            }
                            Lista_Ordenes.setAdapter(arrayAdapter);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "No ordenes");

                    }
                }
        );
        // add it to the RequestQueue
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(getRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
