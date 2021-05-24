package com.example.rolity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Button btnRegistro = findViewById(R.id.botonRegistro);
        Button btnInicioSesion = findViewById(R.id.botonEntrar);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(ActivityLoginRegister.this, ActivityRegistro.class);
                startActivity(registro);
                finish();
            }
        });

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        EditText eEmail = findViewById(R.id.loginEmail);
        EditText eContrasenia = findViewById(R.id.loginPass);
        String email = eEmail.getText().toString();
        String contrasenia = eContrasenia.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,contrasenia).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent usuario = new Intent(ActivityLoginRegister.this, ActivityUsuario.class);
                startActivity(usuario);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage().contains("password is invalid"))
                    Toast.makeText(ActivityLoginRegister.this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                else if (e.getMessage().contains("no user record"))
                    Toast.makeText(ActivityLoginRegister.this, "Esta dirección de correo no corresponde a ningún usuario.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ActivityLoginRegister.this, "Dirección de correo mal formulada.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }
}