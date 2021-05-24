package com.example.rolity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentDatos extends Fragment {
    private ArrayList<EditText> editTexts;
    private ArrayList<String> datos;
    private FirebaseFirestore db;
    private boolean preparado;
    private final String[] NOMBRES_CAMPOS = new String[]{"email", "nombre", "apellidos", "tlf", "pais", "cp", "provincia", "localidad", "direccion"};
    private String userEmail;
    private FirebaseUser usr;

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
                Intent login = new Intent(getActivity(), ActivityLoginRegister.class);
                startActivity(login);
                getActivity().finish();
            }
        });

        Button btnCambiarPass = getActivity().findViewById(R.id.btnChangePass);
        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog();
            }
        });
    }

    private void mostrarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_cambiar_pass, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextOldPass = dialog.findViewById(R.id.changePassOld);
        EditText editTextNewPass = dialog.findViewById(R.id.changePassNew);
        EditText editTextNewPass2 = dialog.findViewById(R.id.changePassNew2);


        Button btnEnviarNuevaPass = dialog.findViewById(R.id.btnChangePassEnviar);
        btnEnviarNuevaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNewPass.getText().toString().equals(editTextNewPass2.getText().toString())) {
                    AuthCredential credential = EmailAuthProvider.getCredential(userEmail, editTextOldPass.getText().toString());
                    usr.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                usr.updatePassword(editTextNewPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Contraseña actualizada", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                        else
                                            System.out.println(task.getException().getMessage());
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getActivity(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                editTextOldPass.setText("");
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Ambas contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    editTextNewPass.setText("");
                    editTextNewPass2.setText("");
                }
            }
        });
    }

    private void cargarDatos() {
        editTexts = cargarEditTexts();

        db = FirebaseFirestore.getInstance();

        usr = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = usr.getEmail();

        db.collection("usuarios").document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                for (int i = 0; i < editTexts.size(); i++) {
                    editTexts.get(i).setText(documentSnapshot.getString(NOMBRES_CAMPOS[i]));
                }
            }
        });

    }

    private void guardarDatosUsuario() {
        datos = new ArrayList<>();

        HashMap<String, String> datosUser = new HashMap<>();

        boolean error;
        error = comprobarCampos();
        if (!error) {
            datosUser.put("email", userEmail);
            for (int i = 0; i < datos.size(); i++) {
                datosUser.put(NOMBRES_CAMPOS[i], datos.get(i).trim());
            }

            db.collection("usuarios").document(userEmail).set(datosUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("Datos actualizados");
                    Toast.makeText(getActivity(), "Usuario actualizado!",  Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    private boolean comprobarCampos() {
        for (int i = 0; i < editTexts.size(); i++) {
            if (editTexts.get(i).getText().toString().isEmpty()) {
                Toast.makeText(getActivity(),"Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return true;
            }
            datos.add(editTexts.get(i).getText().toString());
        }
        return false;
    }

    /*private boolean comprobarPass(String email, String newPass) {
        final boolean[] error = {true};

        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(usr.getEmail(), currentPass);
        usr.reauthenticate(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    usr.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                System.out.println("Contraseña actualizada");
                            error[0] = false;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e.getMessage());
                            if (e.getMessage().contains("password is invalid"))
                                Toast.makeText(getActivity(), "La contraseña es muy corta (6 caractéres mínimo)", Toast.LENGTH_SHORT).show();
                            error[0] = true;
                        }
                    });
                }
            }
        });

        return error[0];
    }*/


    private ArrayList<EditText> cargarEditTexts() {
        ArrayList<EditText> editTexts = new ArrayList<>();

        editTexts.add(getActivity().findViewById(R.id.logedEmail));
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