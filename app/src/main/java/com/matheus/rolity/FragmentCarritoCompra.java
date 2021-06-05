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
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    public void onStart() {
        super.onStart();

        if (logeado) {
            productos = new ArrayList<>();

            db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    RelativeLayout layout = getActivity().findViewById(R.id.layoutNingunProducto);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        ArrayList<String> carrito = (ArrayList<String>) document.get("carrito");
                        if (carrito.size() != 0) {
                            layout.setVisibility(View.INVISIBLE);
                            crearLayouts(carrito);
                        } else {
                            crearLayouts(carrito);
                            layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            RelativeLayout layout = getActivity().findViewById(R.id.layoutNoLogeado);
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

    private void crearLayouts(ArrayList<String> carrito) {
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewCarrito);


        for (int i = 0; i < carrito.size(); i++) {
            db.document(carrito.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot producto = task.getResult();
                        String nombre = producto.getString("nombre");
                        String precio = producto.getString("precio");
                        String imagen = producto.getString("imagen");
                        String categoria = producto.getString("categoria");

                        productos.add(new Producto(nombre, precio, imagen, categoria));

                        ListAdaptador listAdaptador = new ListAdaptador(getActivity(), productos, recyclerView, true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                        recyclerView.setAdapter(listAdaptador);

                    } else
                        System.out.println(task.getException().getMessage());
                }
            });
        }

        /*db.collection("patines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> productos = queryDocumentSnapshots.getDocuments();

                for (int i = 0; i < carrito.size(); i++) {
                    for (int j = 0; j < productos.size(); j++) {
                        if (productos.get(j).getString("nombre").equals(carrito.get(i))) {
                            String nombre = productos.get(j).getString("nombre");
                            String precio = productos.get(j).getString("precio");
                            String imagen = productos.get(i).getString("imagen");

                            FragmentCarritoCompra.this.productos.add(new Producto(nombre, precio, imagen));
                        }
                    }
                }

                RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewLista);
                ListAdaptador listAdaptador = new ListAdaptador(getActivity(), FragmentCarritoCompra.this.productos, recyclerView, true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                recyclerView.setAdapter(listAdaptador);
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrito_compra, container, false);
    }
}