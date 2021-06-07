package com.matheus.rolity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityCategoriaProducto extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_producto);

        String categoria = getIntent().getStringExtra("categoria");

        TextView tvCategoria = findViewById(R.id.tvCategoria);
        tvCategoria.setText(categoria);

        categoria = categoria.toLowerCase();

        db = FirebaseFirestore.getInstance();

        ImageView flechaAtras = findViewById(R.id.flechaAtras);
        flechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cargarProductos(categoria);
    }

    private void cargarProductos(String categoria) {
        List<Producto> producto = new ArrayList<>();

        db.collection(categoria).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();
                for (int i = 0; i < lista.size(); i++) {
                    String nombre = lista.get(i).getString("nombre");
                    String precio = lista.get(i).getString("precio");
                    String imagen = lista.get(i).getString("imagen");

                    producto.add(new Producto(nombre, precio, imagen, categoria));
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerViewCategoriaProducto);
                ListAdaptador listAdaptador = new ListAdaptador(ActivityCategoriaProducto.this, producto, null, recyclerView, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ActivityCategoriaProducto.this, 2));
                recyclerView.setAdapter(listAdaptador);
            }
        });
    }
}