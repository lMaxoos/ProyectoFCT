package com.matheus.rolity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.matheus.rolity.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentInicio frInicio = new FragmentInicio();
        FragmentFavoritos frFavoritos = new FragmentFavoritos();
        FragmentUsuario frUsuario = new FragmentUsuario();
        FragmentCarritoCompra frCarrito = new FragmentCarritoCompra();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorMain,frInicio).commit();

        String frag = getIntent().getStringExtra("fragment");

        if (frag != null) {
            FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
            transition.replace(R.id.contenedorMain, frUsuario);
        }

        ImageView logoMain = findViewById(R.id.logoInicio);
        logoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transition.replace(R.id.contenedorMain, frInicio);
                transition.commit();
            }
        });

        ImageView logoFavoritos = findViewById(R.id.logoFavoritos);
        logoFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transition.replace(R.id.contenedorMain, frFavoritos);
                transition.commit();
            }
        });

        ImageView logoCarritoCompra = findViewById(R.id.logoCarrito);
        logoCarritoCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transition.replace(R.id.contenedorMain, frCarrito);
                transition.commit();
            }
        });

        ImageView logoUsuario = findViewById(R.id.logoUsuario);
        logoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent usuario = new Intent(ActivityMain.this, ActivityUsuario.class);
                    startActivity(usuario);
                } else {
                    Intent loginRegister = new Intent(ActivityMain.this, ActivityLoginRegister.class);
                    startActivity(loginRegister);
                }
            }
        });
    }

    public static void abrirProducto(Context context, String nombre) {
        Intent producto = new Intent(context, ActivityProducto.class);
        producto.putExtra("nombre", nombre);
        context.startActivity(producto);
    }

    public void abrirUsuario(View view) {

    }
}