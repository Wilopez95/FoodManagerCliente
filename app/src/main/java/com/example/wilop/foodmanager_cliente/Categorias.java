package com.example.wilop.foodmanager_cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

import java.util.ArrayList;

import static com.example.wilop.foodmanager_cliente.Login.sharedPreferences;

public class Categorias extends AppCompatActivity {

    public static ArrayList<String> Lista_orden = new ArrayList<String>();
    public static ArrayList<String> Lista_precios = new ArrayList<String>();
    public static ArrayList<String> Lista_producto_precio = new ArrayList<String>();

    JSONObject MarketId;
    JSONArray productos;

    ArrayList<JSONObject> productosbyCategory= new ArrayList<JSONObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        productosbyCategory.clear();
        String marketId = sharedPreferences.getString("marketId", "");
        try {
            MarketId = new JSONObject(marketId);
            String marketid = MarketId.getString("_id");
            //Log.d("IDMERCADDO", marketid);
            getProductsbymarket(marketid);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void ClickCategory(View view){
        ImageView tag = (ImageView)view;
        String Data = tag.getTag().toString();
        if(Data.equals("0")){
            selectCategory("Canasta básica");
        }else if(Data.equals("1")){
            selectCategory("Carnes");
        }else if(Data.equals("2")){
            selectCategory("Pastas");
        }else if(Data.equals("3")){
            selectCategory("Salsas");
        }else if(Data.equals("4")){
            selectCategory("Condimentos");
        }else if(Data.equals("5")){
            selectCategory("Granos");
        }else if(Data.equals("6")){
            selectCategory("Lácteos");
        }else if(Data.equals("7")){
            selectCategory("Frutas y verduras");
        }else if(Data.equals("8")){
            selectCategory("Pan");
        }else if(Data.equals("9")){
            selectCategory("Enlatados");
        }else if(Data.equals("10")){
            selectCategory("Snacks");
        }else if(Data.equals("11")){
            selectCategory("Golosinas");
        }else if(Data.equals("12")){
            selectCategory("Refrescos");
        }else if(Data.equals("13")){
            selectCategory("Higiene personal");
        }else if(Data.equals("14")){
            selectCategory("Productos para hogar");
        }else if(Data.equals("15")){
            selectCategory("Licores");
        }
    }
    private void selectCategory(String categoria){
        for (int i = 0 ; i<productos.length() ;i++){
            try {
                JSONObject producto = new JSONObject(productos.get(i).toString());
                JSONObject productobymarket = producto.getJSONObject("product");
                String Category = productobymarket.getString("category");
                if(Category.equals(categoria)){
                    productosbyCategory.add(producto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sharedPreferences.edit().putString("Productosxcategoria", productosbyCategory.toString()).apply();
        sharedPreferences.edit().putString("categoria", categoria).apply();
        Intent intent = new Intent(getApplicationContext(),Productos.class);
        startActivity(intent);


    }
    private void getProductsbymarket(String idmarket){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "https://food-manager.herokuapp.com/productsbymarkets/market/"+idmarket, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        // display response
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            productos = jsonObject.getJSONArray("product");
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
