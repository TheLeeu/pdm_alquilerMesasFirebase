package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Perfil extends AppCompatActivity {

    private ImageView img;
    private Uri urlImage;
    private String foto = MainActivity.FOTO_USUARIOS_NUEVOS_DEFAULT;
    private EditText TxtNombre, TxtApellido, TxtTelefono, TxtEdad;
    private Button btn_modificar;
    public FirebaseDatabase database;
    public DatabaseReference referenData;
    private DatabaseReference referenciaData;
    private DatabaseReference referenciaData2;
    private FirebaseStorage storage;
    private StorageReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        database = FirebaseDatabase.getInstance();
        referenData = database.getReference();

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        img = findViewById(R.id.ImgPerfil);
        TxtNombre = findViewById(R.id.TxtPefilNombre);
        TxtApellido = findViewById(R.id.TxtPefilApellido);
        TxtTelefono = findViewById(R.id.TxtPefilTelefono);
        TxtEdad = findViewById(R.id.TxtPefilEdad);
        btn_modificar = (Button) findViewById(R.id.BtnModificarPerfil);

        Log.d("ID", currentUser.getUid());

        referenData.child(MainActivity.TBL_USUARIOS).child(currentUser.getUid()).addValueEventListener(getUsuario);

        referenciaData = database.getReference(MainActivity.TBL_USUARIOS).child(currentUser.getUid());
        referenciaData2 = database.getReference(MainActivity.TBL_EMPLEADOS).child(currentUser.getUid());

        Log.d("Nombre", ""+usuario.getNombre());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

    }

    public ValueEventListener getUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                usuario = snapshot.getValue(Usuario.class);

                Glide.with(getApplicationContext()).load(usuario.getFoto()).into(img);
                TxtNombre.setText(usuario.getNombre());
                TxtApellido.setText(usuario.getApellido());
                TxtTelefono.setText(usuario.getTelefono());
                TxtEdad.setText(""+usuario.getEdad());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/");
        startActivityForResult(intent,1);
    }

    public void btnModificarPerfil(View view){

        modificarDatos();

    }

    public void modificarDatos(){

        //VALIDAR QUE LOS CAMPOS NO ESTEN VACIOS
        if(!TxtNombre.getText().toString().isEmpty() && !TxtApellido.getText().toString().isEmpty()
                && !TxtTelefono.getText().toString().isEmpty() && !TxtEdad.getText().toString().isEmpty()){

            Usuario user = new Usuario();
            user.setIdUsuario(usuario.getIdUsuario());
            user.setNombre(TxtNombre.getText().toString());
            user.setApellido(TxtApellido.getText().toString());
            user.setTelefono(TxtTelefono.getText().toString());
            user.setEdad(Integer.parseInt(TxtEdad.getText().toString()));
            user.setFoto(usuario.getFoto());
            user.setCorreo(usuario.getCorreo());
            user.setEstado(usuario.getEstado());
            user.setTipo(usuario.getTipo());

            referenciaData2.setValue(user);
            referenciaData.setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            referenData.child(MainActivity.TBL_USUARIOS).child(currentUser.getUid()).addValueEventListener(getUsuario);
                            Toast.makeText(Perfil.this, "DATOS MODIFICADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "DEBE LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            urlImage = data.getData();
            StorageReference file = reference.child("Perfil").child(urlImage.getLastPathSegment());
            UploadTask subir = file.putFile(urlImage);
            Task<Uri> uriTask = subir.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return file.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri urlDescarga = task.getResult();
                    foto = urlDescarga.toString();


                    Usuario user = new Usuario();
                    user.setIdUsuario(usuario.getIdUsuario());
                    user.setNombre(TxtNombre.getText().toString());
                    user.setApellido(TxtApellido.getText().toString());
                    user.setTelefono(TxtTelefono.getText().toString());
                    user.setEdad(Integer.parseInt(TxtEdad.getText().toString()));
                    user.setFoto(foto);
                    user.setCorreo(usuario.getCorreo());
                    user.setEstado(usuario.getEstado());
                    user.setTipo(usuario.getTipo());

                    referenciaData2.setValue(user);
                    referenciaData.setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    referenData.child(MainActivity.TBL_USUARIOS).child(currentUser.getUid()).addValueEventListener(getUsuario);
                                    Toast.makeText(Perfil.this, "DATOS MODIFICADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                }
                            });




                    Log.i("URLl", urlDescarga.toString());
                    Glide.with(getApplicationContext()).load(urlDescarga).into(img);

                }
            });
        }
    }

}