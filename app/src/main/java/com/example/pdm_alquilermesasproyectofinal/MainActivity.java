package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.TipoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String TBL_USUARIOS = "USUARIOS";
    public static final TipoUsuario USUARIO_TIPO_ADMINISTRADOR = new TipoUsuario(0,"Administrador");
    public static final TipoUsuario USUARIO_TIPO_CLIENTE = new TipoUsuario(1,"Cliente");
    public static final EstadoUsuario USUARIO_ESTADO_ACTIVO = new EstadoUsuario(0,"Activo");
    public static final EstadoUsuario USUARIO_ESTADO_BAJA = new EstadoUsuario(1,"Baja");
    public static final String ERROR_INTERNET_FIREBASE = "com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred.";
    public static final String FOTO_USUARIOS_NUEVOS_DEFAULT = "https://firebasestorage.googleapis.com/v0/b/pdmalquilermesasproyectofinal.appspot.com/o/perfil.png?alt=media&token=62e07752-76cc-4cad-9ca8-1aef18598b4e";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void validarUsuario(FirebaseUser user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TBL_USUARIOS).child(user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                //VALIDAR QUE USUARIO TENGA ESTADO ACTIVO EN SU CUENTA
                //SI ES 0 = ACTIVO
                if(usuario.getEstado().getIdEstado() == USUARIO_TIPO_ADMINISTRADOR.getIdTipoUsuario()){
                    //VALIDAR QUE TIPO DE USUARIO ES
                    //SI ES 0 = Administrador
                    //DEPENDIENDO DE SU TIPO SE LE MANDA A UNA ACTIVIDAD
                    if(usuario.getTipo().getIdTipoUsuario() == 0){
                        cargarAdministradorMainActivity();
                    }else if(usuario.getTipo().getIdTipoUsuario() == USUARIO_TIPO_CLIENTE.getIdTipoUsuario()){ //SI ES 1 = Cliente
                        cargarClienteMainActivity();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "La cuenta se encuentra inactiva", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ON_CANCELLED", "Failed to read value.", error.toException());
            }
        });

    }


    public void cargarLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void onStart() {
        super.onStart();
        //VERIFICAR QUE SE TENGA CONEXION A INTERNET
        ConnectivityManager cm =
                (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            // Verifica si el usuario tiene sesion iniciada (non-null) y actualiza la vista
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                validarUsuario(currentUser);
            }else{
                cargarLoginActivity();
            }
        }else{
            cargarSinInternetActivity();
        }

    }

    public void cargarSinInternetActivity(){
        Intent intent = new Intent(this, SinInternetActivity.class);
        startActivity(intent);
        finish();
    }

    public void crearUsuarioAdministrador(){
        String correo = "admin@admin.com";
        String contrasenia = "admin1";
        mAuth.createUserWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            registrarDatosUsuario(user);
                            Log.d("ADMINISTRADOR", "createUserWithEmail:success");
                            Toast.makeText(MainActivity.this, "SE CREO ADMINISTRADOR", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ADMINISTRADOR", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registrarDatosUsuario(FirebaseUser user){

        //ESCRIBIR DATOS DE USUARIO EN REALTIME DATABASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TBL_USUARIOS);
        Usuario usuario = new Usuario();
        usuario.setNombre("Administrador");
        usuario.setApellido("Administrador");
        usuario.setEdad(25);
        usuario.setFoto(FOTO_USUARIOS_NUEVOS_DEFAULT);
        usuario.setCorreo(user.getEmail());

        //PARA TIPO DE USUARIO
        TipoUsuario tipoU = new TipoUsuario();
        tipoU.setIdTipoUsuario(USUARIO_TIPO_ADMINISTRADOR.getIdTipoUsuario());
        tipoU.setTipoUsuario(USUARIO_TIPO_ADMINISTRADOR.getTipoUsuario());
        usuario.setTipo(tipoU);

        //PARA ESTADO USUARIO
        EstadoUsuario estadoU = new EstadoUsuario();
        estadoU.setIdEstado(USUARIO_ESTADO_ACTIVO.getIdEstado());
        estadoU.setEstado(USUARIO_ESTADO_ACTIVO.getEstado());
        usuario.setEstado(estadoU);

        myRef.child(user.getUid()).setValue(usuario);
    }
}