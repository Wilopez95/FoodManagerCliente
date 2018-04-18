package com.example.wilop.foodmanager_cliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Orden extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    ArrayList<String> pedido = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    ListView Lists_pedidos;
    TextView EmpyCard;
    EditText observaciones;
    EditText direccion;
    String correo;
    String contraseña;
    Session  session;
    String mailpedido;
    int pos;
    String UserID;
    String UserName;
    String UserEmail;
    String MarketID;
    String Productos;
    String Direccion;
    String Precio;
    String Observacion;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Lists_pedidos = findViewById(R.id.lista_pedido);
        observaciones = findViewById(R.id.observaciones);
        direccion = findViewById(R.id.direccion);
        EmpyCard = findViewById(R.id.Empycard);
 //GET SHARED INFO
        sharedPreferences = getSharedPreferences("com.example.wilop.foodmanager_cliente", Context.MODE_PRIVATE);
        UserID = sharedPreferences.getString("userid", "");
        UserName = sharedPreferences.getString("name", "");
        UserEmail = sharedPreferences.getString("email", "");
        MarketID = sharedPreferences.getString("market_Id", "");
        token =  sharedPreferences.getString("token", "");
        Productos = Categorias.Lista_orden.toString();
        Precio = String.valueOf(calcularprecio());
        llenarOrden();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,pedido);
        Lists_pedidos.setAdapter(arrayAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Direccion = direccion.getText().toString();
                Observacion = observaciones.getText().toString();
                if (Categorias.Lista_precios.size() <=0){
                    Snackbar.make(view, "Por favor agregue algun producto al carrito", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(direccion.getText().toString().equals("")){
                    Snackbar.make(view, "Por favor ingrese una direccion", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Ordenar();
                    Finish();

                }

            }
        });

        Lists_pedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos =  position;
                resetList();

                return false;
            }
        });
    }
    public void resetList(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Eliminar")
                .setMessage("¿Desea eliminar este producto?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Categorias.Lista_orden.remove(pos-1);
                        Categorias.Lista_precios.remove(pos-1);
                        pedido.clear();
                        Precio = String.valueOf(calcularprecio());
                        llenarOrden();
                        Lists_pedidos.setAdapter(arrayAdapter);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
    public void llenarOrden() {

        if (Categorias.Lista_precios.size() <= 0) {

            EmpyCard.setVisibility(View.VISIBLE);
            observaciones.setVisibility(View.INVISIBLE);
            direccion.setVisibility(View.INVISIBLE);
        } else {

            EmpyCard.setVisibility(View.INVISIBLE);
            observaciones.setVisibility(View.VISIBLE);
            direccion.setVisibility(View.VISIBLE);
            pedido.add("Cliente:" + "\n" + UserName);
            for (int i = 0; i < Categorias.Lista_orden.size(); i++) {
                pedido.add(Categorias.Lista_orden.get(i).toUpperCase() + ":           " + Categorias.Lista_precios.get(i) + "₡");
            }
            pedido.add("TOTAL" + "           " + Precio + "₡");
        }
    }
    public void Finish(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.checkbox_on_background)
                .setTitle("Listo")
                .setMessage("Se ha creado una nueva orden")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                        Categorias.Lista_precios.clear();
                        Categorias.Lista_orden.clear();
                    }
                })

                .show();
    }
    public int calcularprecio(){
        int preciototal = 1500;
        for (int i= 0; i < Categorias.Lista_precios.size();i++){
            int tmpPrecio = Integer.parseInt(Categorias.Lista_precios.get(i));
            preciototal = preciototal + tmpPrecio;
        }
        return preciototal;
    }
    public void Ordenar(){
        StringRequest postRequest = null;
        String url = "https://food-manager.herokuapp.com/orders";
        postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPUESTA+", response);
                        SendMail(UserEmail,UserName);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "fallo");
                        Log.i("RESPUESTA-", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserID);
                params.put("marketId", MarketID);
                params.put("products", Productos);
                params.put("address", Direccion);
                params.put("price", Precio);
                params.put("remark", Observacion);
                params.put("token", token);
                return params;

            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(postRequest);

    }
    public void SendMail(String email, String name){
        correo = "Foodmanagercr@gmail.com";
        contraseña = "FoodManager#admin";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");
        try{
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return  new PasswordAuthentication(correo,contraseña);
                }
            });

            if(session!= null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("Se creo una nueva orden");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setContent("Se ha creado una nueva orden asociada a esta cuenta con los siguientes articulos:"+"\n"+pedido+"\n"+"Te haremos saber cuando este listo tu pedido!","text/html; charset=utf-8");
                Transport.send(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Salir")
                .setMessage("¿Desea salir al menu principal?")
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
