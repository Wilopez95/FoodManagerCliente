package com.example.wilop.foodmanager_cliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

import static com.example.wilop.foodmanager_cliente.Login.sharedPreferences;
import static java.util.Arrays.asList;

public class Help extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
        ArrayList<String> opciones ;
        ArrayAdapter<String> arrayAdapter;
        GoogleApiClient googleApiClient;
        ListView ListaOpciones;
        TextView texto;
        String Abastecedores = "Al entrar el usuario tendra una lista de todos los supermercados disponibles en los cuales puede realizar sus pedidos , al seleccionar un super mercado se puede ver los detalles del mismo";
        String Categorias = "Para mayor facilidad de los clientes hemos agrupado todos los productos en categorias las cuales facilitan la busqueda de los productos";
        String Agregar_productos = "Despues de seleccionar la categoria , los usuarios tienen una lista con todos sus productos , con solo tocarlos nos preguntara su queremos agregarlos al carrito y de esta forma llenamos nuestro carrito de productos , al finalizar podemos precionar el boton de carrito para ver nuestro pedido y sus detalles";
        String Carrito = "Desde aqui podemos revisar nuestro pedido y confirmarlo , tenemos la informacion de los productos que pedimos asi como el monto final, antes de realizar un pedido debemos confirmar la direccion y tenemos la opcion de agregar una observacion";
        String Quitar = "En caso que ya no queramos un producto antes de realizar el pedido podemos retirarlo de la lista , esto con solo tocarlo por un momento y tendremos la opcion de eliminarlo";
        String Ordenar = "Una vez estemos seguros de nuestro pedido basta tocar el boton rojo que tenemos en nuetra pantalla para confirmar el pedido , reciviremos un correo donde podemos ver nuestro pedido y tambien uno cuando este listo";
        String Historial = "Desde aqui podemos ver todos nuestros pedidos antiguos , entrar podemos ver una lista por fecha de cada pedido que se ha realizado, al tocarlo podemos ver los detalles de cada pedido";
        String Configuraciones = "En las configuraciones el usuario puede realizar una actualizacion del telefono y del nombre, simplemente hay que poner los nuevos valores y guardar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        texto = findViewById(R.id.help);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient =  new  GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        ListaOpciones = findViewById(R.id.ListaAyuda);
        opciones = new ArrayList<String>(asList("Abastecedores","Categorias","Agregar productos","Carrito","Quitar productos","Ordenar","Historial","Configuraciones"));
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,opciones);
        ListaOpciones.setAdapter(arrayAdapter);


        ListaOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(position ==0){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Abastecedores);
                    texto.setVisibility(View.VISIBLE);
                }else if(position ==1){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Categorias);
                    texto.setVisibility(View.VISIBLE);

                }else if(position ==2){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Agregar_productos);
                    texto.setVisibility(View.VISIBLE);

                }else if(position ==3){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Carrito);
                    texto.setVisibility(View.VISIBLE);

                }else if(position ==4){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Quitar);
                    texto.setVisibility(View.VISIBLE);

                }else if(position ==5){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Ordenar);
                    texto.setVisibility(View.VISIBLE);

                }else if(position ==6){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Historial);
                    texto.setVisibility(View.VISIBLE);

                }else if(position ==7){
                    ListaOpciones.setVisibility(View.INVISIBLE);
                    texto.setText(Configuraciones);
                    texto.setVisibility(View.VISIBLE);

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
        getMenuInflater().inflate(R.menu.help, menu);
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
        } else if (id == R.id.nav_Historial) {
            Intent intent = new Intent(getApplicationContext(),Historial.class);
            startActivity(intent);
        } else if (id == R.id.nav_ayuda) {

        } else if (id == R.id.nav_Configuraciones) {
            Intent intent = new Intent(getApplicationContext(),Setings.class);
            startActivity(intent);
        } else if (id == R.id.nav_salir) {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        texto.setVisibility(View.INVISIBLE);
        ListaOpciones.setVisibility(View.VISIBLE);
        return true;
    }
}
