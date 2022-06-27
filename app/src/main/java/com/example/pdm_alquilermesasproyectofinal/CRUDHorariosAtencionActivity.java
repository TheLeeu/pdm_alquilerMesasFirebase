package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.HorarioAtencion;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CRUDHorariosAtencionActivity extends AppCompatActivity {

    private Spinner sp_horaApertura, sp_horaCierre;
    private CheckBox cb_lunes, cb_martes, cb_miercoles, cb_jueves, cb_viernes, cb_sabado, cb_domingo;
    Button btn_agregar,btn_modificar, btn_eliminar;
    private Local local;
    private ArrayList<String> listHoras;
    private long nuevoId =0;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudhorarios_atencion);

        sp_horaApertura = (Spinner) findViewById(R.id.spHoraAperturaCRUDHorariosAtencion);
        sp_horaCierre = (Spinner) findViewById(R.id.spHoraCierreCRUDHorariosAtencion);
        cb_lunes = (CheckBox) findViewById(R.id.cbLunes);
        cb_martes = (CheckBox) findViewById(R.id.cbMartes);
        cb_miercoles = (CheckBox) findViewById(R.id.cbMiercoles);
        cb_jueves = (CheckBox) findViewById(R.id.cbJueves);
        cb_viernes = (CheckBox) findViewById(R.id.cbViernes);
        cb_sabado = (CheckBox) findViewById(R.id.cbSabado);
        cb_domingo = (CheckBox) findViewById(R.id.cbDomingo);
        btn_agregar = (Button)findViewById(R.id.btnAgregarCRUDHorariosAtencion);
        btn_modificar = (Button)findViewById(R.id.btnModificarCRUDHorariosAtencion);
        btn_eliminar = (Button)findViewById(R.id.btnEliminarCRUDHorariosAtencion);

        listHoras = new ArrayList<>();
        cargarHorasSpinner();

        if(getIntent().getStringExtra("ACTIVITY").equals("ListaHorariosAtencionActivity btnAgregar")){
            local = new Local(Integer.parseInt(getIntent().getStringExtra("idLocal")),
                    getIntent().getStringExtra("nombreLocal"),
                    getIntent().getStringExtra("direccionLocal"),
                    getIntent().getStringExtra("telefonoLocal"),
                    getIntent().getStringExtra("coordenadasLocal"),
                    getIntent().getStringExtra("fotoLocal"));
            btn_modificar.setVisibility(View.INVISIBLE);
            btn_modificar.setEnabled(false);
            btn_eliminar.setVisibility(View.INVISIBLE);
            btn_eliminar.setEnabled(false);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_HORARIOS_ATENCION);
            //OBTENER EL NUEVO ID PARA INGRESAR
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        nuevoId = (snapshot.getChildrenCount());
                        Log.d("NUEVO_ID_LOCAL  myRef", String.valueOf(nuevoId));
                    }else{
                        Log.d("NUEVO_ID_LOCAL myRef", String.valueOf(nuevoId));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else if(getIntent().getStringExtra("ACTIVITY").equals("ListaHorariosAtencionActivity item")){
            local = new Local(Integer.parseInt(getIntent().getStringExtra("idLocal")),
                    getIntent().getStringExtra("nombreLocal"),
                    getIntent().getStringExtra("direccionLocal"),
                    getIntent().getStringExtra("telefonoLocal"),
                    getIntent().getStringExtra("coordenadasLocal"),
                    getIntent().getStringExtra("fotoLocal"));

            id = Integer.parseInt(getIntent().getStringExtra("idHorario"));

            for(int i = 0; i < listHoras.size(); i++){
                if(listHoras.get(i).equals(getIntent().getStringExtra("HoraApertura"))){
                    sp_horaApertura.setSelection(i);
                    break;
                }
            }
            for(int i = 0; i < listHoras.size(); i++){
                if(listHoras.get(i).equals(getIntent().getStringExtra("HoraCierre"))){
                    sp_horaCierre.setSelection(i);
                    break;
                }
            }
            if(getIntent().getStringExtra("dia").equals("Lunes")){
                cb_lunes.setChecked(true);
                cb_martes.setEnabled(false);
                cb_miercoles.setEnabled(false);
                cb_jueves.setEnabled(false);
                cb_viernes.setEnabled(false);
                cb_sabado.setEnabled(false);
                cb_domingo.setEnabled(false);
            }else if(getIntent().getStringExtra("dia").equals("Martes")){
                cb_lunes.setEnabled(false);
                cb_martes.setChecked(true);
                cb_miercoles.setEnabled(false);
                cb_jueves.setEnabled(false);
                cb_viernes.setEnabled(false);
                cb_sabado.setEnabled(false);
                cb_domingo.setEnabled(false);
            }else if(getIntent().getStringExtra("dia").equals("Miercoles")){
                cb_lunes.setEnabled(false);
                cb_martes.setEnabled(false);
                cb_miercoles.setChecked(true);
                cb_jueves.setEnabled(false);
                cb_viernes.setEnabled(false);
                cb_sabado.setEnabled(false);
                cb_domingo.setEnabled(false);
            }else if(getIntent().getStringExtra("dia").equals("Jueves")){
                cb_lunes.setEnabled(false);
                cb_martes.setEnabled(false);
                cb_miercoles.setEnabled(false);
                cb_jueves.setChecked(true);
                cb_viernes.setEnabled(false);
                cb_sabado.setEnabled(false);
                cb_domingo.setEnabled(false);
            }else if(getIntent().getStringExtra("dia").equals("Viernes")){
                cb_lunes.setEnabled(false);
                cb_martes.setEnabled(false);
                cb_miercoles.setEnabled(false);
                cb_jueves.setEnabled(false);
                cb_viernes.setChecked(true);
                cb_sabado.setEnabled(false);
                cb_domingo.setEnabled(false);
            }else if(getIntent().getStringExtra("dia").equals("Sabado")){
                cb_lunes.setEnabled(false);
                cb_martes.setEnabled(false);
                cb_miercoles.setEnabled(false);
                cb_jueves.setEnabled(false);
                cb_viernes.setEnabled(false);
                cb_sabado.setChecked(true);
                cb_domingo.setEnabled(false);
            }else if(getIntent().getStringExtra("dia").equals("Domingo")){
                cb_lunes.setEnabled(false);
                cb_martes.setEnabled(false);
                cb_miercoles.setEnabled(false);
                cb_jueves.setEnabled(false);
                cb_viernes.setEnabled(false);
                cb_sabado.setEnabled(false);
                cb_domingo.setChecked(true);
            }
            btn_agregar.setVisibility(View.INVISIBLE);
            btn_agregar.setEnabled(false);

        }
    }

    public void cargarHorasSpinner(){
        for(int i = 0; i < MainActivity.HORAS.length; i++){
            listHoras.add(MainActivity.HORAS[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listHoras);
        sp_horaApertura.setAdapter(adapter);
        sp_horaCierre.setAdapter(adapter);
    }

    public void btnAgregar(View view) {
        //validar que al menos un checkbox este seleccionado
        if(cb_lunes.isChecked() || cb_martes.isChecked() || cb_miercoles.isChecked() || cb_jueves.isChecked() ||
        cb_viernes.isChecked() || cb_sabado.isChecked() || cb_domingo.isChecked()){

            if(cb_lunes.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Lunes");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","lunes");
            }
            if(cb_martes.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Martes");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","martes");
            }
            if(cb_miercoles.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Miercoles");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","miercoles");
            }
            if(cb_jueves.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Jueves");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","jueves");
            }
            if(cb_viernes.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Viernes");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","viernes");
            }
            if(cb_sabado.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Sabado");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","sabado");
            }
            if(cb_domingo.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Domingo");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","domingo");
            }
            Toast.makeText(this, "Horario agregado", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Debe seleccionar al menos un día", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificar(View view){
        //validar que al menos un checkbox este seleccionado
        if(cb_lunes.isChecked() || cb_martes.isChecked() || cb_miercoles.isChecked() || cb_jueves.isChecked() ||
                cb_viernes.isChecked() || cb_sabado.isChecked() || cb_domingo.isChecked()){

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_HORARIOS_ATENCION);
            //OBTENER EL NUEVO ID PARA INGRESAR
            nuevoId = Long.parseLong(getIntent().getStringExtra("idHorario"));
            if(cb_lunes.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Lunes");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","lunes");
            }
            if(cb_martes.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Martes");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","martes");
            }
            if(cb_miercoles.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Miercoles");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","miercoles");
            }
            if(cb_jueves.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Jueves");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","jueves");
            }
            if(cb_viernes.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Viernes");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","viernes");
            }
            if(cb_sabado.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Sabado");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","sabado");
            }
            if(cb_domingo.isChecked()){
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference(MainActivity.TBL_HORARIOS_ATENCION);

                Log.d("NUEVO_ID_LOCAL", String.valueOf(nuevoId));
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorarioAtencion(Integer.parseInt(String.valueOf(nuevoId)));
                horario.setDia("Domingo");
                horario.setHoraApertura(sp_horaApertura.getSelectedItem().toString());
                horario.setHoraCierre(sp_horaCierre.getSelectedItem().toString());
                horario.setLocal(local);

                myRef1.child(String.valueOf(nuevoId)).setValue(horario);
                nuevoId += 1;
                Log.d("dia","domingo");
            }
            Toast.makeText(this, "Horario Modificado", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Debe seleccionar al menos un día", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnEliminar(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(MainActivity.TBL_HORARIOS_ATENCION);
        myRef.child(String.valueOf(id)).removeValue();
        Toast.makeText(this, "Horario Eliminado", Toast.LENGTH_SHORT).show();
        finish();
    }
}