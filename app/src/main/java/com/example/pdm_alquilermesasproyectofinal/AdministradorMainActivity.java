package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdministradorMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void btnCerrarSesion(View view) {
        mAuth.signOut();
        cargarLoginActivity();
    }

    public void cargarLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void cargarRegistroUsuariosAdministradorActivity(){
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = new Intent(this, RegistroUsuariosAdministradoActivity.class);
        startActivityForResult(intent,1);
    }

    public void btnRegistrarUsuarios(View view) {
        cargarRegistroUsuariosAdministradorActivity();
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void btnPruebas(View view) {
        cargarCRUDReservasActivity();
    }
    public void cargarCRUDReservasActivity(){
        Intent intent = new Intent(this, CRUDReservasActivity.class);
        startActivity(intent);
    }

    public void btnLocales(View view) {
        cargarListaLocalesActivity();
    }

    public void cargarListaLocalesActivity(){
        Intent intent = new Intent(this, ListaLocalesActivity.class);
        startActivity(intent);
    }
}