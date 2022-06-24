package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
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

import java.util.ArrayList;

public class CRUDMesasActivity extends AppCompatActivity {

    private ImageView img;
    private EditText et_numero, et_capacidad, et_precio;
    private Spinner sp_estado;
    private Button btn_agregar, btn_modificar, btn_eliminar;
    private Uri urlImage;
    private ArrayList<EstadoMesa> listEstadoMesa;
    private DatabaseReference ref;
    private EstadoMesa estadoSeleccionado;
    private int idLocal;
    private String nombreLocal;
    private String direccionLocal;
    private String telefonoLocal;
    private String coordenadasLocal;
    private String fotoLocal;
    private String foto = MainActivity.FOTO_MESA_NUEVO_DEFAULT;
    public FirebaseStorage storage;
    public StorageReference reference;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudmesas);

        img = (ImageView) findViewById(R.id.imgCRUDMesas);
        et_numero = (EditText) findViewById(R.id.etNumeroCRUDMesas);
        et_capacidad = (EditText) findViewById(R.id.etCapacidadCRUDMesas);
        et_precio = (EditText) findViewById(R.id.etPrecioCRUDMesas);
        sp_estado = (Spinner) findViewById(R.id.spEstadoCRUDMesas);
        btn_agregar = (Button) findViewById(R.id.btnAgregarCRUDMesas);
        btn_eliminar = (Button) findViewById(R.id.btnEliminarCRUDMesas);
        btn_modificar = (Button) findViewById(R.id.btnModificarCRUDMesas);

        listEstadoMesa = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child(MainActivity.TBL_ESTADO_MESA).addValueEventListener(cargarEstadoMesaSpinner);
        sp_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estadoSeleccionado = new EstadoMesa();
                estadoSeleccionado = listEstadoMesa.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img.setImageResource(R.drawable.mesa);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        if(getIntent().getStringExtra("ACTIVITY").equals("CRUDLocalActivity") ||
                getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity_agregar")) {
            idLocal = Integer.parseInt(getIntent().getStringExtra("idLocal"));
            nombreLocal = getIntent().getStringExtra("nombreLocal");
            direccionLocal = getIntent().getStringExtra("direccionLocal");
            telefonoLocal = getIntent().getStringExtra("telefonoLocal");
            coordenadasLocal = getIntent().getStringExtra("coordenadasLocal");
            fotoLocal = getIntent().getStringExtra("fotoLocal");


            local = new Local(idLocal, nombreLocal, direccionLocal, telefonoLocal, coordenadasLocal, fotoLocal);

            btn_modificar.setEnabled(false);
            btn_eliminar.setEnabled(false);
            btn_eliminar.setVisibility(View.INVISIBLE);
            btn_modificar.setVisibility(View.INVISIBLE);
        }else if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity_item")){

            et_numero.setText(getIntent().getStringExtra("numeroMesa"));
            et_capacidad.setText(getIntent().getStringExtra("capacidadMesa"));
            et_precio.setText(getIntent().getStringExtra("precioMesa"));

            foto = getIntent().getStringExtra("fotoMesa");
            Glide.with(getApplicationContext()).load(foto).into(img);



            idLocal = Integer.parseInt(getIntent().getStringExtra("idLocal"));
            nombreLocal = getIntent().getStringExtra("nombreLocal");
            direccionLocal = getIntent().getStringExtra("direccionLocal");
            telefonoLocal = getIntent().getStringExtra("telefonoLocal");
            coordenadasLocal = getIntent().getStringExtra("coordenadasLocal");
            fotoLocal = getIntent().getStringExtra("fotoLocal");

            local = new Local(idLocal, nombreLocal, direccionLocal, telefonoLocal, coordenadasLocal, fotoLocal);
            btn_agregar.setVisibility(View.INVISIBLE);
            btn_agregar.setEnabled(false);
        }


        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
    }

    public ValueEventListener cargarEstadoMesaSpinner = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listEstadoMesa.clear();
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()){
                    EstadoMesa estado = items.getValue(EstadoMesa.class);
                    listEstadoMesa.add(estado);


                }
                ArrayAdapter<EstadoMesa> adapter = new ArrayAdapter<>(CRUDMesasActivity.this, android.R.layout.simple_dropdown_item_1line, listEstadoMesa);
                sp_estado.setAdapter(adapter);

                if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity_item")){
                    for(int i = 0; i < listEstadoMesa.size();i++){
                        Log.d("validacion", "este "+ String.valueOf(listEstadoMesa.get(i).getEstadoMesa().equals(getIntent().getStringExtra("estadoMesa"))) );
                        if(listEstadoMesa.get(i).getEstadoMesa().equals(getIntent().getStringExtra("estadoMesa"))){
                            sp_estado.setSelection(i);
                            Log.d("spinner", String.valueOf(i));
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

    public void btnAgregar(View view) {

        //VALIDAR CAMPOS VACIOS
        if(!et_numero.getText().toString().isEmpty() && !et_capacidad.getText().toString().isEmpty() &&
        !et_precio.getText().toString().isEmpty()){

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_MESAS);

            String idMesa = et_numero.getText().toString() + "_local_" + idLocal;
            Mesas mesa = new Mesas();
            mesa.setIdMesa(idMesa);
            mesa.setNumeroMesa(Integer.parseInt(et_numero.getText().toString()));
            mesa.setCapacidad(Integer.parseInt(et_capacidad.getText().toString()));
            mesa.setPrecioReserva(Double.parseDouble(et_precio.getText().toString()));
            mesa.setFoto(foto);
            mesa.setEstado(estadoSeleccionado);
            mesa.setLocal(local);

            myRef.child(idMesa).setValue(mesa);
            Toast.makeText(this, "Mesa agregada", Toast.LENGTH_SHORT).show();

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
            StorageReference file = reference.child("Mesas").child(urlImage.getLastPathSegment());
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
                    if(btn_modificar.isEnabled()){
                        modificarDatos();
                    }

                }
            });
        }
    }

    public void btnModificar(View view) {

        modificarDatos();

    }

    private void modificarDatos() {
        //VALIDAR CAMPOS VACIOS
        if(!et_numero.getText().toString().isEmpty() && !et_capacidad.getText().toString().isEmpty() &&
                !et_precio.getText().toString().isEmpty()){

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_MESAS);

            String idMesa = getIntent().getStringExtra("idMesa");
            Mesas mesa = new Mesas();
            mesa.setIdMesa(idMesa);
            mesa.setNumeroMesa(Integer.parseInt(et_numero.getText().toString()));
            mesa.setCapacidad(Integer.parseInt(et_capacidad.getText().toString()));
            mesa.setPrecioReserva(Double.parseDouble(et_precio.getText().toString()));
            mesa.setFoto(foto);
            mesa.setEstado(estadoSeleccionado);
            mesa.setLocal(local);

            myRef.child(idMesa).setValue(mesa);
            Toast.makeText(this, "Mesa modificada", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnEliminar(View view) {
        Toast.makeText(this, "Falta programar", Toast.LENGTH_SHORT).show();
    }
}