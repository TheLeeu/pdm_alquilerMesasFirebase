package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class EmpleadoMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button BtnDispMesas = findViewById(R.id.button13);
        BtnDispMesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmpleadoMainActivity.this, List_DipsMesas.class);
                intent.putExtra("ACTIVITY", "EmpleadoMainActivity");
                startActivity(intent);            }
        });

        Button BtnReservaciones = findViewById(R.id.button11);
        BtnReservaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmpleadoMainActivity.this, List_Reservaciones.class);
                intent.putExtra("ACTIVITY", "EmpleadoMainActivity");
                startActivity(intent);            }
        });

    }

    public void Perfil(View view){
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
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