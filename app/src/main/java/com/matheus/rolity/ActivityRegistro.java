package com.matheus.rolity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.matheus.rolity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityRegistro extends AppCompatActivity {
    private ArrayList<EditText> eCampos;
    private ArrayList<String> campos;
    private FirebaseFirestore db;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar");
        builder.setMessage("¿Desea cancelar el registro?")
            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityRegistro.this.onBackPressed();
                    finish();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setCancelable(false)
            .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = FirebaseFirestore.getInstance();
        Button botonCrearUsu = findViewById(R.id.botonRegistrarUsuario);
        botonCrearUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
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

    private void crearUsuario() {
        eCampos = cargarEditTexts();
        campos = new ArrayList<>();
        String email = eCampos.get(0).getText().toString();
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
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,campos.get(1).trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(ActivityRegistro.this, "Usuario creado!",  Toast.LENGTH_SHORT).show();
                                Intent usuario = new Intent(ActivityRegistro.this, ActivityMain.class);
                                usuario.putExtra("fragment", "usuario");
                                startActivity(usuario);
                                finish();
                            }
                        });
                    }
                });
            }
        }
    }

    private boolean comprobarCampos() {
        for (int i = 0; i < eCampos.size(); i++) {
            if (eCampos.get(i).getText().toString().isEmpty()) {
                Toast.makeText(ActivityRegistro.this,"Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return true;
            }
            campos.add(eCampos.get(i).getText().toString());
        }
        return false;
    }

    private boolean comprobarEmail(String email, String pass) {
        final boolean[] error = {true};
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                error[0] = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage().contains("already in use"))
                    Toast.makeText(ActivityRegistro.this,"Este email ya está en uso!", Toast.LENGTH_SHORT).show();
                else if (e.getMessage().contains("password is invalid"))
                    Toast.makeText(ActivityRegistro.this, "La contraseña es muy corta (6 caractéres mínimo)", Toast.LENGTH_SHORT).show();
                error[0] = true;
            }
        });
        return error[0];
    }

    private ArrayList<EditText> cargarEditTexts() {
        ArrayList<EditText> eCampos = new ArrayList<>();

        eCampos.add(findViewById(R.id.registerEmail));
        eCampos.add(findViewById(R.id.registerContrasenia));
        eCampos.add(findViewById(R.id.registerNombre));
        eCampos.add(findViewById(R.id.registerApellidos));
        eCampos.add(findViewById(R.id.registerTlf));
        eCampos.add(findViewById(R.id.registerPais));
        eCampos.add(findViewById(R.id.registerCp));
        eCampos.add(findViewById(R.id.registerProvincia));
        eCampos.add(findViewById(R.id.registerLocalidad));
        eCampos.add(findViewById(R.id.registerDireccion));

        return eCampos;
    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }
}