package com.example.wilop.foodmanager_cliente;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

public class Singup extends AppCompatActivity {

    Button register;

    EditText email;
    EditText name;
    EditText phone;
    EditText password1;
    EditText password2;
    TextView invalidpass;

    String correo;
    String contraseña;
    Session  session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        invalidpass = findViewById(R.id.validpassword);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temail = email.getText().toString();
                String tname = name.getText().toString();
                String tphone = phone.getText().toString();
                String tpasw1 = password1.getText().toString();
                String tpasw2 = password2.getText().toString();

                if (temail.isEmpty()) {
                    invalidpass.setVisibility(View.VISIBLE);
                    invalidpass.setText("Debe ingresar un correo");
                    invalidpass.setVisibility(View.VISIBLE);
                } else if (tname.isEmpty()) {
                    invalidpass.setText("Debe ingresar su nombre");
                    invalidpass.setVisibility(View.VISIBLE);
                } else if (tphone.isEmpty()) {
                    invalidpass.setText("Debe ingresar su numero de telefono");
                    invalidpass.setVisibility(View.VISIBLE);
                } else if (tpasw1.isEmpty()) {
                    invalidpass.setText("Debe ingresar contraseña");
                    invalidpass.setVisibility(View.VISIBLE);
                }else if(!tpasw1.equals(tpasw2)){
                    invalidpass.setText("Las contraseñas no coinciden");
                    invalidpass.setVisibility(View.VISIBLE);
                }else if(tpasw1.length() < 8){
                    invalidpass.setText("Las contraseñas debe contener minimo 8 caracteres");
                    invalidpass.setVisibility(View.VISIBLE);
                }else {
                    invalidpass.setVisibility(View.INVISIBLE);
                    Singup(tname,tphone,temail,tpasw1);
                }
            }
        });
    }
    private void Singup(final String name , final String phone , final String email, final String password){
        StringRequest postRequest = null;
        String url = "https://food-manager.herokuapp.com/users/signup";
        postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPUESTA", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensaje =  jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(),  mensaje,
                                    Toast.LENGTH_LONG).show();
                            sendRegisterMail(email, name);
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "El usuario no se creo",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "fallo");
                        invalidpass.setText("Este correo ya esta registrado");
                        invalidpass.setVisibility(View.VISIBLE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);
                params.put("phone", phone);
                params.put("type", "0");
                return params;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(postRequest);


    }

    private void sendRegisterMail(String email, String name){
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
                message.setSubject("<b>Bienvenido</b>");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setContent(name+" Bienvenido a <b>FOOD MANAGER</b>, el lugar donde puedes realizar sus compras inteligentes.","text/html; charset=utf-8");
                Transport.send(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
