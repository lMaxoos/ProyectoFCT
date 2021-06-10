package com.matheus.rolity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ActivityUsuario extends AppCompatActivity {
    private ArrayList<EditText> editTexts;
    private ArrayList<String> datos;
    private FirebaseFirestore db;
    private boolean preparado;
    private final String[] NOMBRES_CAMPOS = new String[]{"email", "nombre", "apellidos", "tlf", "pais", "cp", "provincia", "localidad", "direccion"};
    private String userEmail;
    private FirebaseUser usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        if (!preparado) {
            cargarDatos();
            preparado = true;
        }

        ImageView flechaAtras = findViewById(R.id.flechaAtras);
        flechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnGuardarDatos = this.findViewById(R.id.botonGuardarCambios);
        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatosUsuario();
            }
        });

        Button btnCerrarSesion = this.findViewById(R.id.botonCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent main = new Intent(ActivityUsuario.this, ActivityMain.class);
                startActivity(main);
                finish();
            }
        });

        Button btnCambiarPass = this.findViewById(R.id.btnChangePass);
        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog();
            }
        });
    }

    private void mostrarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_cambiar_pass, null);
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
                                            Toast.makeText(ActivityUsuario.this, "Contraseña actualizada", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                        else
                                            System.out.println(task.getException().getMessage());
                                    }
                                });
                            }
                            else {
                                Toast.makeText(ActivityUsuario.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                editTextOldPass.setText("");
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(ActivityUsuario.this, "Ambas contraseñas no coinciden", Toast.LENGTH_LONG).show();
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
                String nombreApellidos = documentSnapshot.getString(NOMBRES_CAMPOS[1]) + " " + documentSnapshot.getString(NOMBRES_CAMPOS[2]);
                for (int i = 0; i < editTexts.size(); i++) {
                    editTexts.get(i).setText(documentSnapshot.getString(NOMBRES_CAMPOS[i]));
                }

                TextView tvLetraUsuario = findViewById(R.id.tvLetraUsuario);
                TextView tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
                tvNombreUsuario.setText(nombreApellidos);
                tvLetraUsuario.setText("" + nombreApellidos.charAt(0));
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
                    Toast.makeText(ActivityUsuario.this, "Usuario actualizado!",  Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this,"Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return true;
            }
            datos.add(editTexts.get(i).getText().toString());
        }
        return false;
    }

    private ArrayList<EditText> cargarEditTexts() {
        ArrayList<EditText> editTexts = new ArrayList<>();

        editTexts.add(this.findViewById(R.id.logedEmail));
        editTexts.add(this.findViewById(R.id.logedNombre));
        editTexts.add(this.findViewById(R.id.logedApellidos));
        editTexts.add(this.findViewById(R.id.logedTlf));
        editTexts.add(this.findViewById(R.id.logedPais));
        editTexts.add(this.findViewById(R.id.logedCp));
        editTexts.add(this.findViewById(R.id.logedProvincia));
        editTexts.add(this.findViewById(R.id.logedLocalidad));
        editTexts.add(this.findViewById(R.id.logedDireccion));

        return editTexts;
    }
}