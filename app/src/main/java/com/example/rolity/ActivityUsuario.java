package com.example.rolity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyecto1.R;

public class ActivityUsuario extends AppCompatActivity {
    private Button btnDatos;
    private Button btnLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        FragmentDatos frDatos = new FragmentDatos();
        FragmentLista frLista = new FragmentLista();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorUsuario,frLista).add(R.id.contenedorUsuario, frDatos).commit();

        Drawable seleccionado = ResourcesCompat.getDrawable(getResources(),R.drawable.bordecustombotonseleccionado, null);
        Drawable deseleccionado = ResourcesCompat.getDrawable(getResources(),R.drawable.bordecustombotondeseleccionado, null);

        btnDatos = findViewById(R.id.botonDatos);
        btnLista = findViewById(R.id.botonLista);

        btnDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                transition.replace(R.id.contenedorUsuario, frDatos);
                btnDatos.setBackground(seleccionado);
                btnLista.setBackground(deseleccionado);
                transition.commit();
            }
        });

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                transition.replace(R.id.contenedorUsuario, frLista);
                btnDatos.setBackground(deseleccionado);
                btnLista.setBackground(seleccionado);
                transition.commit();
            }
        });

    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }
}