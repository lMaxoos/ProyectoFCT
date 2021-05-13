package com.example.rolity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto1.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirPatin(View view) {
        Intent ejemplo = new Intent(this, ActivityPatin.class);
        startActivity(ejemplo);
    }

    public void abrirUsuario(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent usuario = new Intent(this, ActivityUsuario.class);
            startActivity(usuario);
        } else {
            Intent login = new Intent(this, ActivityLogin.class);
            startActivity(login);
        }
    }
}