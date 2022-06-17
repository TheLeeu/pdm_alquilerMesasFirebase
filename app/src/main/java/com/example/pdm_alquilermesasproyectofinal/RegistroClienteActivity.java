package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.TipoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroClienteActivity extends AppCompatActivity {
    private EditText et_nombre, et_apellido, et_edad, et_correo, et_contrasenia, et_contrasenia2, et_telefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        //CONECTAMOS LA VISTA CON LA CLASE
        et_nombre = (EditText) findViewById(R.id.etNombreRegistroClienteActivity);
        et_apellido = (EditText) findViewById(R.id.etApellidoRegistroClienteActivity);
        et_edad = (EditText) findViewById(R.id.etEdadRegistroClienteActivity);
        et_correo = (EditText) findViewById(R.id.etCorreoRegistroClienteActivity);
        et_contrasenia = (EditText) findViewById(R.id.etContraseniaRegistroClienteActivity);
        et_contrasenia2 = (EditText) findViewById(R.id.etContrasenia2RegistroClienteActivity);
        et_telefono = (EditText) findViewById(R.id.etTelefonoRegistroClienteActivity);
    }

    public void registroCompleto(String correo, String contrasenia){
        Intent intent = new Intent();
        intent.putExtra("CORREO",correo);
        intent.putExtra("CONTRASENIA",contrasenia);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void btnRegistrateCliente(View view) {
        //VALIDAR QUE NINGUN CAMPO ESTE VACIO
        if(!et_nombre.getText().toString().isEmpty()
        && !et_apellido.getText().toString().isEmpty()
        && !et_edad.getText().toString().isEmpty()
        && !et_correo.getText().toString().isEmpty()
        && !et_contrasenia.getText().toString().isEmpty()
        && !et_contrasenia2.getText().toString().isEmpty()
        && !et_telefono.getText().toString().isEmpty()){
            //VALIDAR QUE LAS CONTRASEÑAS SEAN IGUALES
            if(et_contrasenia.getText().toString().equals(et_contrasenia2.getText().toString())){

                //VALIDAR QUE LA CONTRASEÑA TENGA UNA LONGITUD MAYOR O IGUAL A 6 CARACTERES
                if(et_contrasenia.getText().toString().length() >= 6){

                    //CREACION DEL USUARIO
                    FirebaseAuth myAuth = FirebaseAuth.getInstance();

                    myAuth.createUserWithEmailAndPassword(et_correo.getText().toString(), et_contrasenia.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        //ESCRIBIR DATOS DE USUARIO EN REALTIME DATABASE
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference(MainActivity.TBL_USUARIOS);
                                        Usuario usuario = new Usuario();
                                        usuario.setNombre(et_nombre.getText().toString());
                                        usuario.setApellido(et_apellido.getText().toString());
                                        usuario.setEdad(Integer.parseInt(et_edad.getText().toString()));
                                        usuario.setFoto(MainActivity.FOTO_USUARIOS_NUEVOS_DEFAULT);
                                        usuario.setCorreo(et_correo.getText().toString());
                                        usuario.setTelefono(et_telefono.getText().toString());

                                        //PARA TIPO DE USUARIO
                                        TipoUsuario tipoU = new TipoUsuario();
                                        tipoU.setIdTipoUsuario(MainActivity.USUARIO_TIPO_CLIENTE.getIdTipoUsuario());
                                        tipoU.setTipoUsuario(MainActivity.USUARIO_TIPO_CLIENTE.getTipoUsuario());
                                        usuario.setTipo(tipoU);

                                        //PARA ESTADO USUARIO
                                        EstadoUsuario estadoU = new EstadoUsuario();
                                        estadoU.setIdEstado(MainActivity.USUARIO_ESTADO_ACTIVO.getIdEstado());
                                        estadoU.setEstado(MainActivity.USUARIO_ESTADO_ACTIVO.getEstado());
                                        usuario.setEstado(estadoU);

                                        FirebaseUser user = myAuth.getCurrentUser();

                                        myRef.child(user.getUid()).setValue(usuario);
                                        //SE CIERRA SESION PORQUE AL CREAR EL USUARIO QUEDA LA SESION INICIA
                                        myAuth.signOut();
                                        registroCompleto(et_correo.getText().toString(), et_contrasenia.getText().toString());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e.toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")){
                                Toast.makeText(RegistroClienteActivity.this, "EL CORREO YA SE ENCUENTRA REGISTRADO CON UNA CUENTA", Toast.LENGTH_LONG).show();
                            }
                            Log.d("error", e.toString());
                        }
                    });

                }else{
                    Toast.makeText(RegistroClienteActivity.this, "Contraseña muy pequeña", Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(this, "Las contraseñas no coincides", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }
}