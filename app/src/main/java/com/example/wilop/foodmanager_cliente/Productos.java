package com.example.wilop.foodmanager_cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.wilop.foodmanager_cliente.MainActivity.sharedPreferences;

public class Productos extends AppCompatActivity {

    JSONArray Arrayproductos;
    //ArrayList<String> LProductos = new ArrayList<String>();
    ArrayList<String> pdt = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    ListView Lists_Productos;
    TextView category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Lists_Productos = findViewById(R.id.listProducts );

        category = findViewById(R.id.category);

        String Products = sharedPreferences.getString("Productosxcategoria", "");
        String Categoria = sharedPreferences.getString("categoria", "");
        category.setText(Categoria);
        try {
            Arrayproductos = new JSONArray(Products);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listaProductos();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,pdt);
        Lists_Productos.setAdapter(arrayAdapter);
    }

    
    private void listaProductos(){
        for (int i =0; i<Arrayproductos.length();i++){
            try {
                JSONObject Producto = new JSONObject(Arrayproductos.get(i).toString());
                JSONObject Productodata  = new JSONObject(Producto.getString("product"));
                String name = Productodata.getString("name");
                String brand = Productodata.getString("brand");
                String Description = Productodata.getString("description");
                String price = Producto.getString("price");
                String orden = name.toUpperCase()+"\n"+"Marca: "+brand+"\n"+"Descripcion"+Description+"\n"+"Precio: "+price+"₡";
                pdt.add(orden);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d("DEBUG_TAG","Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d("DEBUG_TAG","Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d("DEBUG_TAG","Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d("DEBUG_TAG","Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d("DEBUG_TAG","Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Volver")
                .setMessage("¿Desea volver a Categorias?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),Categorias.class);
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
