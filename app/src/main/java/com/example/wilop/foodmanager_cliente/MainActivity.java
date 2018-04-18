package com.example.wilop.foodmanager_cliente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    static SharedPreferences sharedPreferences;
    Button login;
    Button singup;
    TextView invalidata;
    EditText email;
    EditText password;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        login = findViewById(R.id.login);
        singup = findViewById(R.id.singup);
        invalidata = findViewById(R.id.invalidata);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("com.example.wilop.foodmanager_cliente", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        valildartoken(token);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(),password.getText().toString());
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singup();
            }
        });


    }

    private void valildartoken(final String token){
        StringRequest postRequest = null;
        if(token == ""){

        }else {
            String url = "https://food-manager.herokuapp.com/users/check";
            postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp =  jsonObject.getString("message");
                                Log.i("POST", resp);
                                if(resp.equals("Token valido")){
                                    //El token es valido pase al home
                                    Intent intent = new Intent(getApplicationContext(),Home.class);
                                    startActivity(intent);
                                }else{
                                    //El token es invalido debe iniciar session
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", "fallo");


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    return params;
                }
            };
            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            MyRequestQueue.add(postRequest);

        }
    }

    private void login(final String email, final String password){
        StringRequest postRequest = null;
        String url = "https://food-manager.herokuapp.com/users/login";
        postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.i("RESPUESTA", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String name =  jsonObject.getString("name");
                                String userID =  jsonObject.getString("userId");
                                String token =  jsonObject.getString("token");
                                sharedPreferences.edit().putString("userid", userID).apply();
                                sharedPreferences.edit().putString("token", token).apply();
                                sharedPreferences.edit().putString("name", name).apply();
                                sharedPreferences.edit().putString("email", email).apply();
                                Intent intent = new Intent(getApplicationContext(),Home.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            invalidata.setVisibility(View.INVISIBLE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", "fallo");
                            invalidata.setVisibility(View.VISIBLE);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            MyRequestQueue.add(postRequest);
    }

    private void singup(){
        Intent intent = new Intent(getApplicationContext(),Singup.class);
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
            return true;
        }
}
