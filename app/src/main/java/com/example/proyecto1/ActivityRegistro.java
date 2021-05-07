package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityRegistro extends AppCompatActivity {
    private ArrayList<EditText> eCampos;
    private ArrayList<String> campos;
    private UsuarioDTO usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button botonCrearUsu = findViewById(R.id.botonGuardarDatos);
        eCampos = cargarEditTexts();
        botonCrearUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usr = new UsuarioDTO();
                campos = new ArrayList<>();

                boolean error;
                error = comprobarCampos();
                if (!error) {
                    error = comprobarUsuario();
                    if (!error) {
                        error = comprobarEmail();
                        if (!error) {
                            usr.setNom_usuario(campos.get(0).trim());
                            usr.setContrasenia(campos.get(1).trim());
                            usr.setEmail(campos.get(2).trim());
                            usr.setNombre(campos.get(3).trim());
                            usr.setApellidos(campos.get(4).trim());
                            usr.setTlf(campos.get(5).trim());
                            usr.setPais(campos.get(6).trim());
                            usr.setCP(campos.get(7).trim());
                            usr.setProvincia(campos.get(8).trim());
                            usr.setLocalidad(campos.get(9).trim());
                            usr.setDireccion(campos.get(10).trim());

                            int resInsert = Modelo.insertarUsuario(ActivityRegistro.this, usr);

                            if (resInsert == 1) {
                                Toast.makeText(ActivityRegistro.this, "Usuario creado!",  Toast.LENGTH_SHORT).show();
                                setLogeado(usr.getNom_usuario());
                                Intent login = new Intent(ActivityRegistro.this, ActivityUsuario.class);
                                startActivity(login);
                                finish();
                            }
                            else
                                Toast.makeText(ActivityRegistro.this, "Error",  Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
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

    private boolean comprobarUsuario() {
        Cursor fila = Modelo.leerUsuario(ActivityRegistro.this, campos.get(0).trim());

        if (fila.moveToFirst()) {
            Toast.makeText(ActivityRegistro.this,"Este nombre de usuario ya existe!", Toast.LENGTH_SHORT).show();
            return true;
        }

        else
            return false;
    }

    private boolean comprobarEmail() {
        Cursor fila = Modelo.leerCorreo(ActivityRegistro.this, campos.get(2).trim());

        if (fila.moveToFirst()) {
            Toast.makeText(ActivityRegistro.this,"Este email ya está en uso!", Toast.LENGTH_SHORT).show();
            return true;
        }

        else
            return false;
    }

    private ArrayList<EditText> cargarEditTexts() {
        ArrayList<EditText> eCampos = new ArrayList<>();

        eCampos.add(findViewById(R.id.registerUsuario));
        eCampos.add(findViewById(R.id.registerContrasenia));
        eCampos.add(findViewById(R.id.registerEmail));
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

    private void setLogeado(String usuario) {
        ((App) getApplication()).setLogeado(true);
        ((App) getApplication()).setNomUsuario(usuario);
    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }
}