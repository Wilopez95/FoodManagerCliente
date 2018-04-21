package com.example.wilop.foodmanager_cliente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    static SharedPreferences sharedPreferences;
    Button login;
    Button singup;
    SignInButton signInButton;
    TextView invalidata;
    EditText email;
    EditText password;
    GoogleApiClient googleApiClient;
    String gmail;
    String gpass;
    String gname;
    public static final int SIGN_IN_CODE = 777;

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



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient =  new  GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                 startActivityForResult(intent,SIGN_IN_CODE);
            }
        });

        sharedPreferences = getSharedPreferences("com.example.wilop.foodmanager_cliente", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        valildartoken(token);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 0 para login con credenciales , 1 login con google
                login(email.getText().toString().toLowerCase(),password.getText().toString(),0);
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singup();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
               GoogleSignInResult result =  Auth.GoogleSignInApi.getSignInResultFromIntent(data);
               handleSignInresult(result);
        }
    }

    private void handleSignInresult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            gmail= account.getEmail();
            gpass= account.getId();
            gname= account.getDisplayName();
            login(account.getEmail().toLowerCase(),account.getId(),1);

        }else{
            Toast.makeText(this,"No se ha podido iniciar sesion con Google", Toast.LENGTH_LONG).show();
        }
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

    private void login(final String email, final String password , final int type){
        Log.i("A", email);
                Log.i("B", password);
        StringRequest postRequest = null;
        String url = "https://food-manager.herokuapp.com/users/login";
        postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("RESPUESTA", response);
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
                            if(type == 1){
                                Log.d("Error.Response", "fallo login con google");
                                registrogoogle(gname.toLowerCase(), gmail,gpass);
                                Toast.makeText(getApplicationContext(),"No se ha podido iniciar sesion con esta cuenta de Google", Toast.LENGTH_LONG).show();
                                revoke();
                            }else {
                                Log.d("Error.Response", "fallo");
                                invalidata.setVisibility(View.VISIBLE);
                            }

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

    private void registrogoogle (final String name, final String email, final String password){
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
                                login(gmail.toLowerCase(),gpass,1);
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
                            //Toast.makeText(getApplicationContext(),"No se ha podido iniciar sesion con Google", Toast.LENGTH_LONG).show();
                            Log.d("Error.Response", "fallo");

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("name", name);
                    params.put("phone", "0");
                    params.put("type", "0");
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
