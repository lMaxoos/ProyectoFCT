package com.matheus.rolity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ActivityProducto extends AppCompatActivity {
    private boolean esFavorito, estaEnCarrito;
    private FirebaseFirestore db;
    private FirebaseUser usr;
    private Producto producto;
    private final String[] NOMBRES_CAMPOS = new String[]{"precio", "nombre", "descripcion", "caracteristicas"};
    private final String[] TITLE_DIALOG = new String[]{"No ha iniciado sesión", "Este producto ya está en el carrito"};
    private final String[] MENSAJE_DIALOG = new String[]{"¿Desea iniciar sesión?", "¿Desea agregar más?"};
    private ArrayList<TextView> textViews;
    private FloatingActionButton fab;
    private boolean logeado;
    private String path;
    private Snackbar snackbar;

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

        if (logeado) {
            checkFavorito(fab);
            checkCarrito(path);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logeado) {
                    if (esFavorito) {
                        DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                        array.update("favoritos", FieldValue.arrayRemove(path));
                        fab.setImageResource(R.drawable.logo_corazonnegro);
                        esFavorito = false;
                    } else {
                        DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                        array.update("favoritos", FieldValue.arrayUnion(path));
                        fab.setImageResource(R.drawable.logo_corazonrojo);
                        esFavorito = true;
                    }
                } else {
                    mostrarDialog(0, null);
                }
            }
        });

        Button btnAniadirCarrito = findViewById(R.id.botonAniadirCarrito);
        btnAniadirCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logeado) {
                    if (estaEnCarrito)
                        mostrarDialog(1, v);
                    else
                        abrirSnackBarToast(v);
                } else {
                    mostrarDialog(0, null);
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

    private void mostrarDialog(int tipo, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_DIALOG[tipo]);
        builder.setMessage(MENSAJE_DIALOG[tipo])
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tipo == 0) {
                            Intent loginRegister = new Intent(ActivityProducto.this, ActivityLoginRegister.class);
                            startActivity(loginRegister);
                        } else if (tipo == 1) {
                            abrirSnackBarToast(v);
                        }
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

    private void abrirSnackBarToast(View v) {
        snackbar = Snackbar.make(v, "", Snackbar.LENGTH_INDEFINITE);
        View customSnackView = getLayoutInflater().inflate(R.layout.layout_snackbar_agregar_carrito, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);

        EditText numeroProductos = customSnackView.findViewById(R.id.numeroProductosSnack);
        Button botonMas = customSnackView.findViewById(R.id.botonMasSnack);
        Button botonMenos = customSnackView.findViewById(R.id.botonMenosSnack);

        botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = Integer.parseInt(numeroProductos.getText().toString());
                if (valor != 10) {
                    valor++;
                    numeroProductos.setText(String.valueOf(valor));
                }
            }
        });

        botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = Integer.parseInt(numeroProductos.getText().toString());
                if (valor != 1) {
                    valor--;
                    numeroProductos.setText(String.valueOf(valor));
                }
            }
        });

        ImageView equis = customSnackView.findViewById(R.id.equis);
        equis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        Button agregarCarrito = customSnackView.findViewById(R.id.agregarCarrito);
        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                array.update("carrito", FieldValue.arrayUnion(path));
                array.update("carrito_num", FieldValue.arrayUnion(path + "/" + numeroProductos.getText().toString()));
                Toast.makeText(ActivityProducto.this, "Añadido al carrito!", Toast.LENGTH_SHORT).show();
                snackbar.dismiss();
            }
        });

        snackbarLayout.addView(customSnackView, 0);
        snackbar.show();
    }

    private void checkCarrito(String path) {
        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> carrito = (ArrayList<String>) document.get("carrito");

                    if (carrito.contains(path))
                        estaEnCarrito = true;
                    else
                        estaEnCarrito = false;
                }
            }
        });
    }

    private void checkFavorito(FloatingActionButton fab) {
        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");

                    if (favoritos.contains(path)) {
                        esFavorito = true;
                        fab.setImageResource(R.drawable.logo_corazonrojo);
                    } else {
                        esFavorito = false;
                        fab.setImageResource(R.drawable.logo_corazonnegro);
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
                String precio = documentSnapshot.getString(NOMBRES_CAMPOS[0]) + " €";
                textViews.get(0).setText(precio);
                for (int i = 1; i < textViews.size(); i++) {
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

    @Override
    protected void onResume() {
        super.onResume();
        if (logeado)
            checkFavorito(fab);
    }
}