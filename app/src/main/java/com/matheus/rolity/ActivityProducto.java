  package com.matheus.rolity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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
    private Producto producto;
    private final String[] NOMBRES_CAMPOS = new String[]{"precio", "nombre", "descripcion", "caracteristicas"};
    private ArrayList<TextView> textViews;
    private FloatingActionButton fab;
    private boolean logeado;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Intent intent = getIntent();
        producto = (Producto) intent.getSerializableExtra("producto");

        db = FirebaseFirestore.getInstance();

        textViews = cargarTextViews();
        cargarDatos();

        usr = FirebaseAuth.getInstance().getCurrentUser();
        logeado = usr != null;

        fab = findViewById(R.id.fab);

        if (logeado)
            checkFavorito(fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logeado) {
                    if (favorito) {
                        DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                        array.update("favoritos", FieldValue.arrayRemove(path));
                        /*db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");
                                    for (int i = 0; i < favoritos.size(); i++) {
                                        if (favoritos.get(i).equals(producto.getNombreProducto())) {
                                            favoritos.remove(i);
                                            break;
                                        }
                                    }

                                    HashMap<String, ArrayList<String>> datos = new HashMap<>();
                                    datos.put("favoritos", favoritos);
                                    db.collection("usuarios").document(usr.getEmail()).set(datos, SetOptions.merge());
                                }
                            }
                        });/*/
                        fab.setImageResource(R.drawable.corazonnegro);
                        favorito = false;
                    } else {
                        DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                        array.update("favoritos", FieldValue.arrayUnion(path));
                        /*db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");
                                    favoritos.add(producto.getNombreProducto());
                                    HashMap<String, ArrayList<String>> datos = new HashMap<>();
                                    datos.put("favoritos", favoritos);
                                    db.collection("usuarios").document(usr.getEmail()).set(datos, SetOptions.merge());
                                }
                            }
                        });*/

                        fab.setImageResource(R.drawable.corazonrojo);
                        favorito = true;
                    }
                } else {
                    mostrarDialog();
                }

            }
        });

        Button btnAniadirCarrito = findViewById(R.id.botonAniadirCarrito);
        btnAniadirCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logeado) {
                    DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                    array.update("carrito", FieldValue.arrayUnion(path));
                    Toast.makeText(ActivityProducto.this, "Añadido al carrito!", Toast.LENGTH_SHORT).show();
                    /*db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                ArrayList<String> carrito = (ArrayList<String>) document.get("carrito");
                                carrito.add(producto.getNombreProducto());
                                HashMap<String, ArrayList<String>> datos = new HashMap<>();
                                datos.put("carrito", carrito);
                                db.collection("usuarios").document(usr.getEmail()).set(datos, SetOptions.merge());
                                Toast.makeText(ActivityProducto.this, "Añadido al carrito!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                } else {
                    mostrarDialog();
                }
            }
        });

        ImageView flechaAtras = findViewById(R.id.flechaAtras);
        flechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

      private void mostrarDialog() {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("No ha iniciado sesión");
          builder.setMessage("¿Desea iniciar sesión?")
            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent loginRegister = new Intent(ActivityProducto.this, ActivityLoginRegister.class);
                    startActivity(loginRegister);

                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setCancelable(true)
            .show();
      }

      @Override
    protected void onResume() {
        super.onResume();
        if (logeado)
            checkFavorito(fab);
    }

    private void checkFavorito(FloatingActionButton fab) {
        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");

                    if (favoritos.contains(path)) {
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
        ImageView imagenProducto = findViewById(R.id.imagenProducto);
        Glide.with(this)
                .load(producto.getImagen())
                .into(imagenProducto);

        path = db.collection(producto.getCategoria()).document(producto.getNombreProducto()).getPath();
        db.collection(producto.getCategoria()).document(producto.getNombreProducto()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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