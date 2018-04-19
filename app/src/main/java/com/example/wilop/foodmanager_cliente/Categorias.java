package com.example.wilop.foodmanager_cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.wilop.foodmanager_cliente.MainActivity.sharedPreferences;

public class Categorias extends AppCompatActivity {


    JSONObject MarketId;
    JSONArray productos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        String marketId = sharedPreferences.getString("marketId", "");
        try {
            MarketId = new JSONObject(marketId);
            String marketid = MarketId.getString("_id");
            Log.d("IDMERCADDO", marketid);
            getProductsbymarket(marketid);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void ClickCategory(View view){
        ImageView tag = (ImageView)view;
        String Data = tag.getTag().toString();
        if(Data.equals("0")){



        }else if(Data.equals("1")){
            Log.d("TAG", "La chingada2");
        }else if(Data.equals("2")){
            Log.d("TAG", "La chingada2");
        }else if(Data.equals("3")){
            Log.d("TAG", "La chingada2");
        }else if(Data.equals("4")){
            Log.d("TAG", "La chingada2");
        }else if(Data.equals("5")){
            Log.d("TAG", "La chingada2");
        }


    }
    private void getProductsbymarket(String idmarket){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "https://food-manager.herokuapp.com/productsbymarkets/market/"+idmarket, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        // display response
                        Log.d("Response", response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            productos = jsonObject.getJSONArray("product");
                            /*for (int i = 0; i< arrayJSON.length(); i++){
                                JSONObject tmpjsonObject = new JSONObject(arrayJSON.get(i).toString());
                                //String name =  tmpjsonObject.getString("name");
                                //String location =  tmpjsonObject.getString("location");
                                //mercados.add(name.toUpperCase()+"\n"+location);
                            }*/
                            //List_markets.setAdapter(arrayAdapter);
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
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Salir")
                .setMessage("¿Esta seguro que desea cancelar el pedido?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
        return true;
    }
}
