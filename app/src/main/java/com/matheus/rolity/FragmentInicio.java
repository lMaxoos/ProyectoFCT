package com.matheus.rolity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matheus.rolity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentInicio extends Fragment {
    private List<Producto> producto;
    private FirebaseFirestore db;
    public FragmentInicio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        producto = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();

        db.collection("patines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();
                for (int i = 0; i < lista.size(); i++) {
                    String nombre = lista.get(i).getString("nombre");
                    String precio = lista.get(i).getString("precio");

                    producto.add(new Producto(nombre, precio));
                }

                RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewLista);
                ListAdaptador listAdaptador = new ListAdaptador(getActivity(), producto, recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                recyclerView.setAdapter(listAdaptador);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }
}