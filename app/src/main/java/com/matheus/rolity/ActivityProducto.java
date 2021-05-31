  package com.matheus.rolity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.matheus.rolity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

  public class ActivityProducto extends AppCompatActivity {
    private static boolean favorito = false;
    private FirebaseFirestore db;
    private FirebaseUser usr;
    private String nombreProducto;
    private final String[] NOMBRES_CAMPOS = new String[]{"precio", "nombre", "descripcion", "caracteristicas"};
    private ArrayList<TextView> textViews;
    private FloatingActionButton fab;
    private boolean logeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        usr = FirebaseAuth.getInstance().getCurrentUser();

        logeado = usr != null;

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
                if (logeado) {
                    if (favorito) {
                        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");
                                    for (int i = 0; i < favoritos.size(); i++) {
                                        if (favoritos.get(i).equals(nombreProducto)) {
                                            favoritos.remove(i);
                                            break;
                                        }
                                    }

                                    HashMap<String, ArrayList<String>> datos = new HashMap<>();
                                    datos.put("favoritos", favoritos);
                                    db.collection("usuarios").document(usr.getEmail()).set(datos, SetOptions.merge());
                                }
                            }
                        });
                        fab.setImageResource(R.drawable.corazonnegro);
                        favorito = false;
                    } else {
                        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");
                                    favoritos.add(nombreProducto);
                                    HashMap<String, ArrayList<String>> datos = new HashMap<>();
                                    datos.put("favoritos", favoritos);
                                    db.collection("usuarios").document(usr.getEmail()).set(datos, SetOptions.merge());
                                }
                            }
                        });

                        fab.setImageResource(R.drawable.corazonrojo);
                        favorito = true;
                    }
                } else {
                    // TODO: 31/05/2021 Mostrar dialog preguntando si quiere iniciar sesión
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
        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");

                    if (favoritos.contains(nombreProducto)) {
                        favorito = true;
                        fab.setImageResource(R.drawable.corazonrojo);
                    }
                    else {
                        favorito = false;
                        fab.setImageResource(R.drawable.corazonnegro);
                    }
                }
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