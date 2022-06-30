package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.TipoUsuario;
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

import java.util.ArrayList;
import java.util.List;

public class RegistroUsuariosAdministradoActivity extends AppCompatActivity {
    private EditText et_nombre, et_apellido, et_edad, et_correo, et_contrasenia, et_contrasenia2, et_telefono;
    private Spinner sp_tipoUsuario, sp_estadoUsuario, sp_locales;
    private ArrayList<TipoUsuario> listTipoUsuario;
    private ArrayList<EstadoUsuario> listEstadoUsuario;
    private ArrayList<Local> listLocal;
    private TipoUsuario tipoU = new TipoUsuario();
    private EstadoUsuario estadoU = new EstadoUsuario();
    private FirebaseUser userCreado;

    private DatabaseReference mDatabase;
    private String userActual;
    private FirebaseAuth mA ;
    private Local local = new Local();
    private Local localIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios_administrado);

        listTipoUsuario = new ArrayList<>();
        listEstadoUsuario = new ArrayList<>();
        listLocal = new ArrayList<>();

        et_nombre = (EditText) findViewById(R.id.etNombreRegistroAdministrador);
        et_apellido = (EditText) findViewById(R.id.etApellidoRegistroAdministrador);
        et_edad = (EditText) findViewById(R.id.etEdadRegistroAdministrador);
        et_correo = (EditText) findViewById(R.id.etCorreoRegistroAdministrador);
        et_contrasenia = (EditText) findViewById(R.id.etContraseniaRegistroAdministrador);
        et_contrasenia2 = (EditText) findViewById(R.id.etContrasenia2RegistroAdministrador);
        sp_tipoUsuario = (Spinner) findViewById(R.id.spTipoUsuarioRegistroAdministrador);
        sp_estadoUsuario = (Spinner) findViewById(R.id.spEstadoUsuarioRegistroAdministrador);
        et_telefono = (EditText)findViewById(R.id.etTelefonoRegistroAdministrador);
        sp_locales = (Spinner) findViewById(R.id.spLocalesRegistroAdministrador);

        sp_locales.setVisibility(View.INVISIBLE);
        sp_locales.setEnabled(false);


        if(getIntent().getStringExtra("ACTIVITY").equals("ListaEmpeladosActivity btn")){
            localIntent = new Local(Integer.parseInt(getIntent().getStringExtra("idLocal")),
                    getIntent().getStringExtra("nombreLocal"),
                    getIntent().getStringExtra("direccionLocal"),
                    getIntent().getStringExtra("telefonoLocal"),
                    getIntent().getStringExtra("coordenadasLocal"),
                    getIntent().getStringExtra("fotoLocal"));

        }else if(getIntent().getStringExtra("ACTIVITY").equals("ListaEmpeladosActivity item")){

        }


        mA = FirebaseAuth.getInstance();
        FirebaseUser current = mA.getCurrentUser();

        userActual = current.getEmail();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(MainActivity.TBL_TIPO_USUARIOS).addValueEventListener(cargarTipoUsuario);
        mDatabase.child(MainActivity.TBL_ESTADO_USUARIOS).addValueEventListener(cargarEstadoUsuario);

        sp_tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoU.setIdTipoUsuario(listTipoUsuario.get(i).getIdTipoUsuario());
                tipoU.setTipoUsuario(listTipoUsuario.get(i).getTipoUsuario());
                if(tipoU.getTipoUsuario().equals(MainActivity.USUARIO_TIPO_EMPLEADO.getTipoUsuario())){
                    sp_locales.setVisibility(View.VISIBLE);
                    sp_locales.setEnabled(true);
                    mDatabase.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);

                }else{
                    sp_locales.setVisibility(View.INVISIBLE);
                    sp_locales.setEnabled(false);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_estadoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estadoU.setIdEstado(listEstadoUsuario.get(i).getIdEstado());
                estadoU.setEstado(listEstadoUsuario.get(i).getEstado());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_locales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                local.setIdLocal(listLocal.get(i).getIdLocal());
                local.setNombre(listLocal.get(i).getNombre());
                local.setDireccion(listLocal.get(i).getDireccion());
                local.setTelefono(listLocal.get(i).getTelefono());
                local.setCoordenadasGps(listLocal.get(i).getCoordenadasGps());
                local.setFoto(listLocal.get(i).getFoto());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public ValueEventListener cargarEstadoUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()) {
                    EstadoUsuario estado_usuario = items.getValue(EstadoUsuario.class);
                    listEstadoUsuario.add(estado_usuario);
                }
                ArrayAdapter<EstadoUsuario> adaptador = new ArrayAdapter<>(RegistroUsuariosAdministradoActivity.this, android.R.layout.simple_dropdown_item_1line,listEstadoUsuario);
                sp_estadoUsuario.setAdapter(adaptador);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("onCancelledEstadoU",error.toString());
        }
    };

    public ValueEventListener cargarTipoUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()) {
                    TipoUsuario tipo_usuario = items.getValue(TipoUsuario.class);
                    listTipoUsuario.add(tipo_usuario);
                }
                ArrayAdapter<TipoUsuario> adaptador = new ArrayAdapter<>(RegistroUsuariosAdministradoActivity.this, android.R.layout.simple_dropdown_item_1line, listTipoUsuario);
                sp_tipoUsuario.setAdapter(adaptador);

                if(getIntent().getStringExtra("ACTIVITY").equals("ListaEmpeladosActivity btn") ||
                getIntent().getStringExtra("ACTIVITY").equals("ListaEmpeladosActivity item")){
                    for(int i = 0; i < listTipoUsuario.size(); i++){
                        if(listTipoUsuario.get(i).getTipoUsuario().equals(MainActivity.USUARIO_TIPO_EMPLEADO.getTipoUsuario())){
                            sp_tipoUsuario.setSelection(i);
                            sp_tipoUsuario.setEnabled(false);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("onCancellerTipoUsuario",error.toString());
        }
    };

    public ValueEventListener cargarLocales = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()){
                    Local local = items.getValue(Local.class);
                    listLocal.add(local);
                }
                ArrayAdapter<Local> adapter = new ArrayAdapter<>(RegistroUsuariosAdministradoActivity.this, android.R.layout.simple_dropdown_item_1line, listLocal);
                sp_locales.setAdapter(adapter);
                if(getIntent().getStringExtra("ACTIVITY").equals("ListaEmpeladosActivity btn") ||
                        getIntent().getStringExtra("ACTIVITY").equals("ListaEmpeladosActivity item")){
                    for(int i = 0; i < listLocal.size(); i++){
                        if(listLocal.get(i).getIdLocal() == localIntent.getIdLocal()){
                            sp_locales.setSelection(i);
                            sp_locales.setEnabled(false);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("onCancellerLocales",error.toString());
        }
    };

    public void btnRegistrar(View view) {
        //VALIDAR QUE NINGUN CAMPO ESTE VACIO
        if (!et_nombre.getText().toString().isEmpty() &&
                !et_apellido.getText().toString().isEmpty() &&
                !et_edad.getText().toString().isEmpty() &&
                !et_correo.getText().toString().isEmpty() &&
                !et_contrasenia.getText().toString().isEmpty() &&
                !et_contrasenia2.getText().toString().isEmpty() &&
                !et_telefono.getText().toString().isEmpty()) {

            //VALIDAR QUE LAS CONTRASEÑAS SEAN IGUALES
            if (et_contrasenia.getText().toString().equals(et_contrasenia2.getText().toString())) {
                //VALIDAR QUE LAS CONTRASEÑAS TENGAS UNA LONGITUD MAYOR A 6 CARACTERES
                if (et_contrasenia.getText().toString().length() >= 6) {
                    //CREACION DEL USUARIO
                    FirebaseAuth myAuth = FirebaseAuth.getInstance();
                    myAuth.createUserWithEmailAndPassword(et_correo.getText().toString(), et_contrasenia.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseUser user = myAuth.getCurrentUser();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference(MainActivity.TBL_USUARIOS);
                                        Usuario usuario = new Usuario();
                                        usuario.setIdUsuario(user.getUid());
                                        usuario.setNombre(et_nombre.getText().toString());
                                        usuario.setApellido(et_apellido.getText().toString());
                                        usuario.setEdad(Integer.parseInt(et_edad.getText().toString()));
                                        usuario.setFoto(MainActivity.FOTO_USUARIOS_NUEVOS_DEFAULT);
                                        usuario.setCorreo(et_correo.getText().toString());
                                        usuario.setTelefono(et_telefono.getText().toString());

                                        //PARA TIPO DE USUARIO
                                        usuario.setTipo(tipoU);

                                        //PARA ESTADO USUARIO
                                        usuario.setEstado(estadoU);

                                        myRef.child(user.getUid()).setValue(usuario);


                                        //SI EL SPINNER DE LOCALES ESTA VISIBLE SIGNIFICA QUE REGISTRAREMOS UN EMPLEADO
                                        //ENTONCES REGISTRAREMOS EL LOCAL ASIGNADO EN LA TABLA LOCALES
                                        if (sp_locales.getVisibility() == View.VISIBLE) {
                                            FirebaseDatabase databaseE = FirebaseDatabase.getInstance();
                                            DatabaseReference myRefE = database.getReference(MainActivity.TBL_EMPLEADOS);
                                            Empleado empleado = new Empleado();
                                            empleado.setUsuario(usuario);
                                            empleado.setLocal(local);
                                            myRefE.child(user.getUid()).setValue(empleado);
                                        }

                                        //SE CIERRA SESION PORQUE AL CREAR EL USUARIO QUEDA LA SESION INICIA
                                        myAuth.signOut();
                                        registroCompleto(user, et_contrasenia.getText().toString());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e.toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")) {
                                Toast.makeText(RegistroUsuariosAdministradoActivity.this, "EL CORREO YA SE ENCUENTRA REGISTRADO CON UNA CUENTA", Toast.LENGTH_LONG).show();
                            }
                            Log.d("error", e.toString());
                        }
                    });
                } else
                    Toast.makeText(this, "Contraseña muy pequeña", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    public void registroCompleto(FirebaseUser user, String contrasenia){
        userCreado = user;
        Intent intent = new Intent(this, ConfirmarCredencialesActivity.class);
        intent.putExtra("CORREO_USUARIO_ACTUAL",userActual);
        intent.putExtra("CORREO_NUEVO",user.getEmail());
        intent.putExtra("CONTRASENIA_NUEVA",contrasenia);
        intent.putExtra("TIPO_USUARIO",tipoU.getTipoUsuario());
        startActivityForResult(intent,1);
    }

    //METODO PARA VALIDAR LA RESPUESTA DE LA ACTIVDAD CONFIRMAR CREDENCIALES
    //SI EL USUARIO INGRESO SU CONTRASEÑA CORRECTAMENTE ENTONCES BORRAMOS LIMPIAMOS LOS CAMPOS
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK);
                finish();
            }else{
                finish();
            }
        }
    }

}