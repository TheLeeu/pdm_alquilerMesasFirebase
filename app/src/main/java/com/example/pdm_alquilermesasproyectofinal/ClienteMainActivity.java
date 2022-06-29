package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class ClienteMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void btnCerrarSesion(View view) {
        mAuth.signOut();
        cargarLoginActivity();
    }

    public void btnLocales(View view){
        cargarLocalesActivity();
    }

    public void btnLocalesDis(View view){
        Intent intent = new Intent(this, LocalesActivity.class);
        intent.putExtra("ACTIVITY", "ClienteMainActivityDis");
        startActivity(intent);
    }

    public void btnReserva(View view){
        Intent intent = new Intent(this, List_Reservaciones.class);
        intent.putExtra("ACTIVITY", "ClienteMainActivity");
        startActivity(intent);
    }

    public void cargarLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void cargarLocalesActivity(){
        Intent intent = new Intent(this, LocalesActivity.class);
        intent.putExtra("ACTIVITY", "ClienteMainActivity");
        startActivity(intent);
    }
}