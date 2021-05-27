package com.example.rolity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.local.QueryResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityProducto extends AppCompatActivity {
    private static boolean favorito = false;
    private FirebaseFirestore db;
    private FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    private String nombreProducto;
    private final String[] NOMBRES_CAMPOS = new String[]{"precio", "nombre", "descripcion", "caracteristicas"};
    private ArrayList<TextView> textViews;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        nombreProducto = intent.getStringExtra("nombre");

        textViews = cargarTextViews();

        cargarDatos();

        fab = findViewById(R.id.fab);
        checkFavorito(fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorito) {
                    db.collection("usuarios").document(usr.getEmail()).collection("favoritos").document(nombreProducto).delete();
                    fab.setImageResource(R.drawable.corazonnegro);
                    favorito = false;
                } else {
                    HashMap<String, String> aux = new HashMap<>();
                    aux.put("exists", nombreProducto);
                    db.collection("usuarios").document(usr.getEmail()).collection("favoritos").document(nombreProducto).set(aux);
                    fab.setImageResource(R.drawable.corazonrojo);
                    favorito = true;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFavorito(fab);
    }

    private void checkFavorito(FloatingActionButton fab) {
        db.collection("usuarios").document(usr.getEmail()).collection("favoritos").whereEqualTo("exists", nombreProducto).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> lista = task.getResult().getDocuments();
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getString("exists").equals(nombreProducto)) {
                            System.out.println(lista.get(i).getString("exists"));
                            favorito = true;
                            fab.setImageResource(R.drawable.corazonrojo);
                            break;
                        }
                    }
                }
                else
                    favorito = false;
            }
        });
    }

    private void cargarDatos() {
        StorageReference st = FirebaseStorage.getInstance().getReference();
        ImageView imagenProducto = findViewById(R.id.imagenProducto);

        StorageReference pathReference = st.child("patines/" + nombreProducto + ".jpg");
        Glide.with(this)
                .load(pathReference)
                .into(imagenProducto);

        db.collection("patines").document(nombreProducto).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                for (int i = 0; i < textViews.size(); i++) {
                    textViews.get(i).setText(documentSnapshot.getString(NOMBRES_CAMPOS[i]));
                }
            }
        });
    }

    private ArrayList<TextView> cargarTextViews() {
        ArrayList<TextView> textViews = new ArrayList<>();

        textViews.add(findViewById(R.id.tvPrecioProducto));
        textViews.add(findViewById(R.id.tvNombreProducto));
        textViews.add(findViewById(R.id.tvDescripcion));
        textViews.add(findViewById(R.id.tvCaracteristicas));

        return textViews;
    }
}