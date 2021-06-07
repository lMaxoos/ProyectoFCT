package com.matheus.rolity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.List;

public class ActivityMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentInicio frInicio = new FragmentInicio();
        FragmentFavoritos frFavoritos = new FragmentFavoritos();
        FragmentCarritoCompra frCarrito = new FragmentCarritoCompra();

        replaceFragment(frInicio);

        ImageView logoMenu = findViewById(R.id.logoTresBarras);
        logoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(ActivityMain.this, v);

                menu.inflate(R.menu.layout_menu);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent abrirCategoria = new Intent(ActivityMain.this, ActivityCategoriaProducto.class);
                        abrirCategoria.putExtra("categoria", item.toString());
                        startActivity(abrirCategoria);
                        return true;
                    }
                });

                menu.show();
            }
        });

        ImageView logoLupa = findViewById(R.id.logoLupa);
        logoLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView buscador = findViewById(R.id.buscador);
                String busqueda = buscador.getText().toString();

                Intent abrirBusqueda = new Intent(ActivityMain.this, ActivityBusqueda.class);
                abrirBusqueda.putExtra("busqueda", busqueda);
                startActivity(abrirBusqueda);
            }
        });

        ImageView logoMain = findViewById(R.id.logoInicio);
        logoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(frInicio);
            }
        });

        ImageView logoFavoritos = findViewById(R.id.logoFavoritos);
        logoFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(frFavoritos);
            }
        });

        ImageView logoCarritoCompra = findViewById(R.id.logoCarrito);
        logoCarritoCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(frCarrito);
            }
        });

        ImageView logoUsuario = findViewById(R.id.logoUsuario);
        logoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirUsuario();
            }
        });
    }

    private void replaceFragment(Fragment fr) {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transition.replace(R.id.contenedorMain, fr);
        transition.commit();
    }

    private void abrirUsuario() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent usuario = new Intent(ActivityMain.this, ActivityUsuario.class);
            startActivity(usuario);
        } else {
            Intent loginRegister = new Intent(ActivityMain.this, ActivityLoginRegister.class);
            startActivity(loginRegister);
        }
    }
}