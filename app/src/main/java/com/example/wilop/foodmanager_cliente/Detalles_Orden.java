package com.example.wilop.foodmanager_cliente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Detalles_Orden extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    ListView Lista_detalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles__orden);
        Lista_detalles =  findViewById(R.id.details);

        
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Historial.datos_orden);
        Lista_detalles.setAdapter(arrayAdapter);


    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        Historial.datos_orden.clear();
        Intent intent = new Intent(getApplicationContext(),Historial.class);
        startActivity(intent);
        return true;
    }
}
