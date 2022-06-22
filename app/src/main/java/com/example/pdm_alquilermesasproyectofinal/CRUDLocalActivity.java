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
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CRUDLocalActivity extends AppCompatActivity {
    
    private ImageView img;
    private EditText et_nombre, et_direccion, et_telefono, et_coordenadas;
    private Button btn_agregar, btn_modificar, btn_eliminar;
    private long nuevoId =0;
    public Uri urlImage;
    private String foto = MainActivity.FOTO_LOCAL_NUEVO_DEFAULT;
    public FirebaseStorage storage;
    public StorageReference reference;
    private int idLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudlocal);
        
        //Se conectan los componentes con la vista
        img = (ImageView)  findViewById(R.id.imgCRUDLocalActivity);
        et_nombre = (EditText) findViewById(R.id.etNombreCRUDLocalActivity);
        et_direccion = (EditText) findViewById(R.id.etDireccionCRUDLocalActivity);
        et_telefono = (EditText) findViewById(R.id.etTelefonoCRUDLocalActivity);
        et_coordenadas = (EditText) findViewById(R.id.etCoordenadasCRUDLocalActivity);
        btn_agregar = (Button) findViewById(R.id.btnRegistrarCRUDLocalActivity);
        btn_modificar = (Button) findViewById(R.id.btnModificarCRUDLocalActivity);
        btn_eliminar = (Button) findViewById(R.id.btnEliminarCRUDLocalActivity);

        img.setImageResource(R.drawable.local);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();



        //OCultar componentes dependiendo de la actividad que venga y de la accion que se quiera realizar
        if(getIntent().getStringExtra("ListaLocalesActivity").equals("btnAgregar")){
            btn_modificar.setEnabled(false);
            btn_eliminar.setEnabled(false);
            btn_eliminar.setVisibility(View.INVISIBLE);
            btn_modificar.setVisibility(View.INVISIBLE);
            //obtener un nuevo id para el local
            DatabaseReference r = FirebaseDatabase.getInstance().getReference(MainActivity.TBL_LOCALES);
            r.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        nuevoId = (snapshot.getChildrenCount());
                        Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                    }else{
                        Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(getIntent().getStringExtra("ListaLocalesActivity").equals("item")){
            idLocal = Integer.parseInt(getIntent().getStringExtra("idLocal"));
            et_nombre.setText(getIntent().getStringExtra("nombre"));
            et_direccion.setText(getIntent().getStringExtra("direccion"));
            et_telefono.setText(getIntent().getStringExtra("telefono"));
            et_coordenadas.setText(getIntent().getStringExtra("coordenadas"));
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("foto")).into(img);
            foto = getIntent().getStringExtra("foto");
            btn_agregar.setEnabled(false);
            btn_agregar.setVisibility(View.INVISIBLE);
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        btn_eliminar.setEnabled(false);
    }

    public void btnAgregar(View view) {
        //Validar que los campos no esten vacios
        if(!et_nombre.getText().toString().isEmpty() && !et_direccion.getText().toString().isEmpty() && !et_telefono.getText().toString().isEmpty()
        && !et_coordenadas.getText().toString().isEmpty()){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_LOCALES);

            Local local = new Local();
            local.setIdLocal((int) nuevoId);
            local.setNombre(et_nombre.getText().toString());
            local.setDireccion(et_direccion.getText().toString());
            local.setTelefono(et_telefono.getText().toString());
            local.setCoordenadasGps(et_coordenadas.getText().toString());
            local.setFoto(foto);

            myRef.child(String.valueOf(nuevoId)).setValue(local);
            Toast.makeText(this, "nuevo local registrado", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        
    }

    public void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            urlImage = data.getData();
            StorageReference file = reference.child("Locales").child(urlImage.getLastPathSegment());
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
                    Log.i("URLl", urlDescarga.toString());
                    Glide.with(getApplicationContext()).load(urlDescarga).into(img);
                }
            });
        }
    }

    public void btnModificar(View view) {
        //Validar que los campos no esten vacios
        if(!et_nombre.getText().toString().isEmpty() && !et_direccion.getText().toString().isEmpty() && !et_telefono.getText().toString().isEmpty()
                && !et_coordenadas.getText().toString().isEmpty()){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_LOCALES);

            Local local = new Local();
            local.setIdLocal((int) nuevoId);
            local.setNombre(et_nombre.getText().toString());
            local.setDireccion(et_direccion.getText().toString());
            local.setTelefono(et_telefono.getText().toString());
            local.setCoordenadasGps(et_coordenadas.getText().toString());
            local.setFoto(foto);

            myRef.child(String.valueOf(idLocal)).setValue(local);
            Toast.makeText(this, "local modificado", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnEliminar(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(MainActivity.TBL_LOCALES).child(String.valueOf(idLocal));
        myRef.removeValue();

        //remover las demas referencias de las tablas donde aparesca un local

    }
}