package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_registrate;
    private EditText et_correo, et_contrasenia;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        et_correo = (EditText)findViewById(R.id.etCorreoLoginActivity);
        et_contrasenia = (EditText)findViewById(R.id.etContraseniaLoginActivity);
        tv_registrate = (TextView) findViewById(R.id.tvRegistrateLoginActivity);
        tv_registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRegistrateActivity();
            }
        });
    }

    public void cargarRegistrateActivity(){
        Intent intent = new Intent(this, RegistroClienteActivity.class);
        //abro la actividad de login pero le indico que espero una respuesta de esa actividad
        //y la respuesta que espero es 1 de que si me manda algo, si es 0 ocurrio algo en el proceso
        //LO QUE ESPERO ES EL USUARIO Y CONTRASEÑA PARA PONERLOS EN LOS EDITTEXT DE INICIO DE SESION
        startActivityForResult(intent, 1);
    }

    //METODO PARA VALIDAR LA RESPUESTA DE LA ACTIVDAD REGISTRATE
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                et_correo.setText(data.getStringExtra("CORREO"));
                et_contrasenia.setText(data.getStringExtra("CONTRASENIA"));
            }
        }
    }

    public void cargarClienteMainActivity(){
        Intent intent = new Intent(this, ClienteMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cargarAdministradorMainActivity(){
        Intent intent = new Intent(this, AdministradorMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnIniciarSesion(View view) {
        //VALIDAR QUE LOS CAMPOS NO ESTEN VACIOS
        if(!et_correo.getText().toString().isEmpty() && !et_contrasenia.getText().toString().isEmpty()){
            String email = et_correo.getText().toString();
            String password = et_contrasenia.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                validarUsuario(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FAILURE", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("E",e.toString());
                    if(e.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.")){
                        Toast.makeText(LoginActivity.this, "CORREO NO REGISTRADO", Toast.LENGTH_SHORT).show();
                    }else if(e.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.")){
                        Toast.makeText(LoginActivity.this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    public void validarUsuario(FirebaseUser user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(MainActivity.TBL_USUARIOS).child(user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                //VALIDAR QUE USUARIO TENGA ESTADO ACTIVO EN SU CUENTA
                //SI ES 0 = ACTIVO
                if(usuario.getEstado().getIdEstado() == 0){
                    //VALIDAR QUE TIPO DE USUARIO ES
                    //SI ES 0 = Administrador
                    //DEPENDIENDO DE SU TIPO SE LE MANDA A UNA ACTIVIDAD
                    if(usuario.getTipo().getIdTipoUsuario() == 0){
                        cargarAdministradorMainActivity();
                    }else if(usuario.getTipo().getIdTipoUsuario() == 1){ //SI ES 1 = Cliente
                        cargarClienteMainActivity();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "La cuenta se encuentra inactiva", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ON_CANCELLED", "Failed to read value.", error.toException());
            }
        });

    }
}