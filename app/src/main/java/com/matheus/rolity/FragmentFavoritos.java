package com.matheus.rolity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matheus.rolity.R;
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

public class FragmentFavoritos extends Fragment {
    private List<Producto> producto;
    private FirebaseFirestore db;
    private FirebaseUser usr;

    public FragmentFavoritos() {

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

        db.collection("usuarios").document(usr.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> favoritos = (ArrayList<String>) document.get("favoritos");

                    db.collection("patines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> productos = queryDocumentSnapshots.getDocuments();

                            for (int i = 0; i < favoritos.size(); i++) {
                                for (int j = 0; j < productos.size(); j++) {
                                    if (productos.get(j).getString("nombre").equals(favoritos.get(i))) {
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
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favoritos, container, false);
    }
}