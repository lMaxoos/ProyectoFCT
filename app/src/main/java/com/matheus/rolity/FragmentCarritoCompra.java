package com.matheus.rolity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentCarritoCompra extends Fragment {
    private List<Producto> productos;
    private FirebaseFirestore db;
    private FirebaseUser usr;
    private boolean logeado;

    public FragmentCarritoCompra() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        usr = FirebaseAuth.getInstance().getCurrentUser();
        logeado = usr != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // cargarDatos();
    }

    @Override
    public void onStart() {
        super.onStart();
        cargarDatos();
    }

    private void cargarDatos() {
        if (logeado) {
            productos = new ArrayList<>();

            db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    RelativeLayout layout = getActivity().findViewById(R.id.layoutNingunProducto);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        ArrayList<String> carrito = (ArrayList<String>) document.get("carrito");
                        ArrayList<String> carritoNum = (ArrayList<String>) document.get("carrito_num");

                        if (carrito.size() != 0) {
                            crearLayouts(carrito, carritoNum);
                            layout.setVisibility(View.INVISIBLE);
                        } else {
                            crearLayouts(carrito, carritoNum);
                            layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            RelativeLayout layout = getActivity().findViewById(R.id.layoutNoLogeadoCarrito);
            layout.setVisibility(View.VISIBLE);

            Button login = getActivity().findViewById(R.id.botonIniciarSesionCarrito);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginRegister = new Intent(getActivity(), ActivityLoginRegister.class);
                    startActivity(loginRegister);
                }
            });
        }
    }

    private void crearLayouts(ArrayList<String> carrito, ArrayList<String> carritoNum) {
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewCarrito);

        for (int i = 0; i < carrito.size(); i++) {
            String path = db.document(carrito.get(i)).getPath();
            db.document(carrito.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot producto = task.getResult();
                        String nombre = producto.getString("nombre");
                        String precio = producto.getString("precio");
                        String imagen = producto.getString("imagen");
                        String categoria = producto.getString("categoria");

                        productos.add(new Producto(nombre, precio, imagen, categoria, path));

                        ListAdaptador listAdaptador = new ListAdaptador(getActivity(), productos, carritoNum, recyclerView, true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                        recyclerView.setAdapter(listAdaptador);

                    } else
                        System.out.println(task.getException().getMessage());
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrito_compra, container, false);
    }
}