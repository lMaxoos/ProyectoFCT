package com.example.rolity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {
    private List<Producto> producto;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        producto = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("patines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();
                for (int i = 0; i < lista.size(); i++) {
                    String nombre = lista.get(i).getString("nombre");
                    String precio = lista.get(i).getString("precio");

                    producto.add(new Producto(nombre, precio));
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerViewLista);
                ListAdaptador listAdaptador = new ListAdaptador(ActivityMain.this, producto, recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ActivityMain.this, 2));
                recyclerView.setAdapter(listAdaptador);
            }
        });
    }

    public static void abrirProducto(Context context, String nombre) {
        Intent producto = new Intent(context, ActivityProducto.class);
        producto.putExtra("nombre", nombre);
        context.startActivity(producto);
    }

    public void abrirUsuario(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent usuario = new Intent(this, ActivityUsuario.class);
            startActivity(usuario);
        } else {
            Intent login = new Intent(this, ActivityLoginRegister.class);
            startActivity(login);
        }
    }
}