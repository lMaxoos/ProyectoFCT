package com.example.rolity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.proyecto1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentDatos extends Fragment {
    private ArrayList<EditText> eCampos;
    private ArrayList<String> campos;
    private FirebaseFirestore db;
    private boolean preparado;

    public FragmentDatos() {
        preparado = false;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**/
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!preparado) {
            cargarDatos();
            preparado = true;
        }
    }

    private void cargarDatos() {
        System.out.println("PREPARANDO LOS DATOS...");
        ArrayList<EditText> editTexts = cargarEditTexts();
        db = FirebaseFirestore.getInstance();
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("usuarios").document(usr.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                editTexts.get(0).setText(documentSnapshot.getString("email"));
                editTexts.get(1).setText(documentSnapshot.getString("contrasenia"));
                editTexts.get(2).setText(documentSnapshot.getString("nombre"));
                editTexts.get(3).setText(documentSnapshot.getString("apellidos"));
                editTexts.get(4).setText(documentSnapshot.getString("tlf"));
                editTexts.get(5).setText(documentSnapshot.getString("pais"));
                editTexts.get(6).setText(documentSnapshot.getString("cp"));
                editTexts.get(7).setText(documentSnapshot.getString("provincia"));
                editTexts.get(8).setText(documentSnapshot.getString("localidad"));
                editTexts.get(9).setText(documentSnapshot.getString("direccion"));
            }
        });

        Button btnGuardarDatos = getActivity().findViewById(R.id.botonGuardarCambios);
        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatosUsuario();
            }
        });

        Button btnCerrarSesion = getActivity().findViewById(R.id.botonCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(getActivity(), ActivityLogin.class);
                startActivity(login);
                getActivity().finish();
            }
        });
    }

    private void guardarDatosUsuario() {
        eCampos = cargarEditTexts();
        campos = new ArrayList<>();
        String email = campos.get(0).trim();
        String pass = eCampos.get(1).getText().toString();

        HashMap<String, String> datosUser = new HashMap<>();

        boolean error;
        error = comprobarCampos();
        if (!error) {
            error = comprobarEmail(email, pass);
            if (!error) {
                datosUser.put("email", campos.get(0).trim());
                datosUser.put("contrasenia", campos.get(1).trim());
                datosUser.put("nombre", campos.get(2).trim());
                datosUser.put("apellidos", campos.get(3).trim());
                datosUser.put("tlf", campos.get(4).trim());
                datosUser.put("pais", campos.get(5).trim());
                datosUser.put("cp", campos.get(6).trim());
                datosUser.put("provincia", campos.get(7).trim());
                datosUser.put("localidad", campos.get(8).trim());
                datosUser.put("direccion", campos.get(9).trim());

                db.collection("usuarios").document(email).set(datosUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Usuario actualizado!",  Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean comprobarCampos() {
        for (int i = 0; i < eCampos.size(); i++) {
            if (eCampos.get(i).getText().toString().isEmpty()) {
                Toast.makeText(getActivity(),"Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return true;
            }
            campos.add(eCampos.get(i).getText().toString());
        }
        return false;
    }

    private boolean comprobarEmail(String email, String pass) {
        final boolean[] error = {true};

        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        usr.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                error[0] = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage().contains("already in use"))
                    Toast.makeText(getActivity(),"Este email ya está en uso!", Toast.LENGTH_SHORT).show();
                error[0] = true;
            }
        });

        usr.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                error[0] = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage().contains("password is invalid"))
                    Toast.makeText(getActivity(), "La contraseña es muy corta (6 caractéres mínimo)", Toast.LENGTH_SHORT).show();
                error[0] = true;
            }
        });

        return error[0];
    }


    private ArrayList<EditText> cargarEditTexts() {
        ArrayList<EditText> editTexts = new ArrayList<>();

        editTexts.add(getActivity().findViewById(R.id.logedEmail));
        editTexts.add(getActivity().findViewById(R.id.logedContrasenia));
        editTexts.add(getActivity().findViewById(R.id.logedNombre));
        editTexts.add(getActivity().findViewById(R.id.logedApellidos));
        editTexts.add(getActivity().findViewById(R.id.logedTlf));
        editTexts.add(getActivity().findViewById(R.id.logedPais));
        editTexts.add(getActivity().findViewById(R.id.logedCp));
        editTexts.add(getActivity().findViewById(R.id.logedProvincia));
        editTexts.add(getActivity().findViewById(R.id.logedLocalidad));
        editTexts.add(getActivity().findViewById(R.id.logedDireccion));

        return editTexts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_datos, container, false);
    }
}