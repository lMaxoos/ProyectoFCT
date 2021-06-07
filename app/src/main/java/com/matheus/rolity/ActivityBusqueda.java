package com.matheus.rolity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityBusqueda extends AppCompatActivity {
    private final String[] CATEGORIAS = {"patines", "longboard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String busqueda = getIntent().getStringExtra("busqueda");

        List<Producto> productos = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBusqueda);

        for (int i = 0; i < CATEGORIAS.length; i++) {
            CollectionReference ref = db.collection(CATEGORIAS[i]);
            Query query = ref.whereEqualTo("nombre", busqueda);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> producto = task.getResult().getDocuments();
                        for (int j = 0; j < producto.size(); j++) {
                            String nombre = producto.get(j).getString("nombre");
                            String precio = producto.get(j).getString("precio");
                            String imagen = producto.get(j).getString("imagen");
                            String categoria = producto.get(j).getString("categoria");

                            productos.add(new Producto(nombre, precio, imagen, categoria));
                            ListAdaptador listAdaptador = new ListAdaptador(ActivityBusqueda.this, productos, null, recyclerView, false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(ActivityBusqueda.this, 2));
                            recyclerView.setAdapter(listAdaptador);
                        }
                    } else
                        System.out.println(task.getException().getMessage());
                }
            });

        }

        ImageView flechaAtras = findViewById(R.id.flechaAtras);
        flechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}