package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

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
}