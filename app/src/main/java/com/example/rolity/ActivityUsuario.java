package com.example.rolity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.proyecto1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityUsuario extends AppCompatActivity {
    private Button btnDatos;
    private Button btnLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        FragmentDatos frDatos = new FragmentDatos();
        FragmentLista frLista = new FragmentLista();

        getSupportFragmentManager().beginTransaction().add(R.id.layoutContenedor,frLista).add(R.id.layoutContenedor, frDatos).commit();

        Drawable seleccionado = ResourcesCompat.getDrawable(getResources(),R.drawable.bordecustombotonseleccionado, null);
        Drawable deseleccionado = ResourcesCompat.getDrawable(getResources(),R.drawable.bordecustombotondeseleccionado, null);

        btnDatos = findViewById(R.id.botonDatos);
        btnLista = findViewById(R.id.botonLista);

        btnDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.replace(R.id.layoutContenedor, frDatos);
                btnDatos.setBackground(seleccionado);
                btnLista.setBackground(deseleccionado);
                transition.commit();
            }
        });

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                transition.replace(R.id.layoutContenedor, frLista);
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