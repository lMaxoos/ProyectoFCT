package com.example.rolity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto1.R;
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

public class FragmentLista extends Fragment {
    private List<Producto> producto;
    private FirebaseFirestore db;
    private FirebaseUser usr;

    public FragmentLista() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        usr = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();

        producto = new ArrayList<>();

        db.collection("usuarios").document(usr.getEmail()).collection("favoritos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> favoritos = task.getResult().getDocuments();

                    db.collection("patines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> productos = queryDocumentSnapshots.getDocuments();

                            for (int i = 0; i < favoritos.size(); i++) {
                                for (int j = 0; j < productos.size(); j++) {
                                    if (productos.get(j).getString("nombre").equals(favoritos.get(i).getString("exists"))) {
                                        String nombre = productos.get(j).getString("nombre");
                                        String precio = productos.get(j).getString("precio");

                                        producto.add(new Producto(nombre, precio));
                                    }
                                }
                            }

                            RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewLista);
                            ListAdaptador listAdaptador = new ListAdaptador(getActivity(), producto, recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                            recyclerView.setAdapter(listAdaptador);
                        }
                    });
                }
                else
                    System.out.println(task.getException().getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }
}