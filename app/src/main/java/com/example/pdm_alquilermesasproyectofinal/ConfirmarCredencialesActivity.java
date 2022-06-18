package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmarCredencialesActivity extends AppCompatActivity {
    private String correo;
    private String correoNuevo;
    private String contraseniaNuevo;
    private String tipoU;
    private EditText et_contrasenia;
    private boolean validoCredenciales = false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_credenciales);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        et_contrasenia = (EditText)findViewById(R.id.etContraseniaConfirmarCredencialesActivity);
        correo = getIntent().getStringExtra("CORREO_USUARIO_ACTUAL");
        correoNuevo = getIntent().getStringExtra("CORREO_NUEVO");
        Log.d("CORREO",correoNuevo);
        contraseniaNuevo = getIntent().getStringExtra("CONTRASENIA_NUEVA");
        Log.d("CONTRA",contraseniaNuevo);
        tipoU = getIntent().getStringExtra("TIPO_USUARIO");
        Log.d("TIPO_USUARIO",tipoU);

    }

    public void btnAceptar(View view) {
        if(!et_contrasenia.getText().toString().isEmpty()){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(correo,et_contrasenia.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful() && task.isComplete()){
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                validoCredenciales = true;
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("E",e.toString());
                    if(e.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.")){
                        Toast.makeText(ConfirmarCredencialesActivity.this, "CORREO NO REGISTRADO", Toast.LENGTH_SHORT).show();
                    }else if(e.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.")){
                        Toast.makeText(ConfirmarCredencialesActivity.this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                    }else if(e.toString().equals(MainActivity.ERROR_INTERNET_FIREBASE)){
                        Toast.makeText(ConfirmarCredencialesActivity.this, "Error de conexion a internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(validoCredenciales == false){
            mAuth.signInWithEmailAndPassword(correoNuevo,contraseniaNuevo)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference(MainActivity.TBL_USUARIOS);
                                myRef.child(user.getUid()).removeValue();
                                if(tipoU.equals(MainActivity.USUARIO_TIPO_EMPLEADO.getTipoUsuario())){
                                    Log.d("ENTRA","SI");
                                    DatabaseReference myRef1 = database.getReference(MainActivity.TBL_EMPLEADOS);
                                    myRef1.child(user.getUid()).removeValue();
                                }

                                user.delete();
                            }
                        }
                    });
            finish();
        }
    }
}