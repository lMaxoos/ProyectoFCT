package com.example.rolity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.proyecto1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {
    private List<ListProducto> producto;
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

                    producto.add(new ListProducto(0, nombre, precio));
                }

                ListAdaptador listAdaptador = new ListAdaptador(ActivityMain.this, producto);
                RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ActivityMain.this, 2));
                recyclerView.setAdapter(listAdaptador);


            }
        });
    }

    public void abrirPatin(View view) {
        Intent ejemplo = new Intent(this, ActivityPatin.class);
        startActivity(ejemplo);
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