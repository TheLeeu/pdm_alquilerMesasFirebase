package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.HorarioAtencion;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.example.pdm_alquilermesasproyectofinal.modelos.Pago;
import com.example.pdm_alquilermesasproyectofinal.modelos.Reservacion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CRUDReservasActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private long id = 0;
    private ArrayList<Local> listLocales;
    private ArrayList<Mesas> listMesas;
    private  ArrayList<String> listHoras;
    private ArrayList<HorarioAtencion> listHorarioAtencion;
    private Spinner sp_locales, sp_mesas, sp_horaEntrada, sp_horaSalida;
    private EditText et_precio, et_fecha;
    Button btn_reservas;
    private Mesas mesaSeleccionada = new Mesas();
    private int idLocalSeleccionado;
    private int dia, mes, anio;
    private Date eventDate, presentDate;
    private Calendar fecha;
    private String localHoraApertura, localHoraCierre;
    private String diaElegido = "";
    private Pago pago = new Pago();
    private ArrayList<Reservacion> listReservaciones;
    private int idLocal;
    private String nombreLocal;
    private String direccionLocal;
    private String telefonoLocal;
    private String coordenadasLocal;
    private String fotoLocal;
    private Local local;
    private Mesas mesa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudreservas);

        fecha = Calendar.getInstance();


        sp_locales = (Spinner)findViewById(R.id.spLocalesCRUDReservasActivity);
        sp_mesas = (Spinner)findViewById(R.id.spMesasCRUDReservasActivity);
        sp_horaEntrada = (Spinner)findViewById(R.id.spHoraEntradaCRUDReservasActivity);
        sp_horaSalida = (Spinner)findViewById(R.id.spHoraSalidaCRUDReservasActivity);
        et_precio = (EditText)findViewById(R.id.etPrecioCRUDReservasActivity);
        et_fecha = (EditText)findViewById(R.id.etFechaCRUDReservasActivity);
        btn_reservas = (Button)findViewById(R.id.btnReservarCRUDReservasActivity);

        et_precio.setEnabled(false);
        et_fecha.setEnabled(false);
        sp_horaEntrada.setEnabled(false);
        sp_horaSalida.setEnabled(false);

        listLocales = new ArrayList<>();
        listMesas = new ArrayList<>();
        listHoras = new ArrayList<>();
        listReservaciones = new ArrayList<>();
        listHorarioAtencion = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);
        ref.child(MainActivity.TBL_RESERVACIONES).addValueEventListener(getReservaciones);

        //PARA CAPTURAR CUANTAS RESERVACIONES SE HAN HECHO Y TOMAR EL VALOR DEL ID PARA EL NUEVO REGISTRO
        DatabaseReference r = FirebaseDatabase.getInstance().getReference(MainActivity.TBL_RESERVACIONES);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //CAPTURAR ID DE LOCAL AL SELECCIONAR UNO EN SPINNER Y SUS HORARIOS
        sp_locales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idLocalSeleccionado = listLocales.get(i).getIdLocal();
                ref.child(MainActivity.TBL_MESAS).addValueEventListener(cargarMesas);
                ref.child(MainActivity.TBL_HORARIOS_ATENCION).addValueEventListener(getHorariosAtencion);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //CARGAR PRECIO DE RESERVACION DE CADA MES CUANDO SE SELECCIONE EN EL SPINNER
        sp_mesas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mesaSeleccionada = listMesas.get(i);
                et_precio.setText(String.valueOf(listMesas.get(i).getPrecioReserva()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //cargarHorasEntrada();

        sp_horaEntrada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarHorasSalida(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity")){

            idLocal = Integer.parseInt(getIntent().getStringExtra("idLocal"));
            nombreLocal = getIntent().getStringExtra("nombreLocal");
            direccionLocal = getIntent().getStringExtra("direccionLocal");
            telefonoLocal = getIntent().getStringExtra("telefonoLocal");
            coordenadasLocal = getIntent().getStringExtra("coordenadasLocal");
            fotoLocal = getIntent().getStringExtra("fotoLocal");

            local = new Local(idLocal, nombreLocal, direccionLocal, telefonoLocal, coordenadasLocal, fotoLocal);
            EstadoMesa estado = new EstadoMesa(Integer.parseInt(getIntent().getStringExtra("idEstadoMesa")),getIntent().getStringExtra("estadoMesa"));
            mesa = new Mesas(getIntent().getStringExtra("idMesa"), Integer.parseInt(getIntent().getStringExtra("capacidadMesa")),
                    estado, local, Integer.parseInt(getIntent().getStringExtra("numeroMesa")), Double.parseDouble(getIntent().getStringExtra("precioMesa")),
                    getIntent().getStringExtra("fotoMesa"));


            sp_mesas.setEnabled(false);
            sp_locales.setEnabled(false);
        }

        //INTERACCION DEL EMPLEADO
        if(getIntent().getStringExtra("ACTIVITY").equals("List_DipsMesas")){

            idLocal = Integer.parseInt(getIntent().getStringExtra("idLocal"));
            nombreLocal = getIntent().getStringExtra("nombreLocal");
            direccionLocal = getIntent().getStringExtra("direccionLocal");
            telefonoLocal = getIntent().getStringExtra("telefonoLocal");
            coordenadasLocal = getIntent().getStringExtra("coordenadasLocal");
            fotoLocal = getIntent().getStringExtra("fotoLocal");

            local = new Local(idLocal, nombreLocal, direccionLocal, telefonoLocal, coordenadasLocal, fotoLocal);
            EstadoMesa estado = new EstadoMesa(Integer.parseInt(getIntent().getStringExtra("idEstadoMesa")),getIntent().getStringExtra("estadoMesa"));
            mesa = new Mesas(getIntent().getStringExtra("idMesa"), Integer.parseInt(getIntent().getStringExtra("capacidadMesa")),
                    estado, local, Integer.parseInt(getIntent().getStringExtra("numeroMesa")), Double.parseDouble(getIntent().getStringExtra("precioMesa")),
                    getIntent().getStringExtra("fotoMesa"));

            sp_mesas.setEnabled(false);
            sp_locales.setEnabled(false);
        }

    }

    public ValueEventListener cargarLocales = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()){
                    Local local = items.getValue(Local.class);
                    listLocales.add(local);
                }
                ArrayAdapter<Local> adapter = new ArrayAdapter<>(CRUDReservasActivity.this, android.R.layout.simple_dropdown_item_1line, listLocales);
                sp_locales.setAdapter(adapter);

                if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity")){
                    for(int i = 0; i < listLocales.size(); i++){
                        if(String.valueOf(listLocales.get(i).getIdLocal()).equals(String.valueOf(local.getIdLocal()))){
                            sp_locales.setSelection(i);
                            break;
                        }
                    }
                }

                //INTERACCION DEL EMPLEADO
                if(getIntent().getStringExtra("ACTIVITY").equals("List_DipsMesas")){
                    for(int i = 0; i < listLocales.size(); i++){
                        if(String.valueOf(listLocales.get(i).getIdLocal()).equals(String.valueOf(local.getIdLocal()))){
                            sp_locales.setSelection(i);
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

    public ValueEventListener getHorariosAtencion = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                listHorarioAtencion.clear();
                for(DataSnapshot items: snapshot.getChildren()){
                    HorarioAtencion horarioA = items.getValue(HorarioAtencion.class);
                    if(horarioA.getLocal().getIdLocal() == idLocalSeleccionado) {
                        listHorarioAtencion.add(horarioA);
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void cargarHorasEntrada(ArrayList<Reservacion> reservacionesDelDia){
        listHoras.clear();
        //RECORRER LA LISTA DE LOS HORARIOS DE LOCAL HASTA ENCONTRAR EL HORARIO DEL DIA SELECCIONADO EN FECHA
        boolean comienza = false;
        boolean termina = false;
        boolean siEntrada = false;
        int posicionEntrada = -1;
        for(int i = 0; i < listHorarioAtencion.size(); i++){
            if(listHorarioAtencion.get(i).getDia().equals(diaElegido)){
                for(int j = 0; j < MainActivity.HORAS.length; j++){
                    if(listHorarioAtencion.get(i).getHoraApertura().equals(MainActivity.HORAS[j])){
                        comienza = true;
                    }else if(listHorarioAtencion.get(i).getHoraCierre().equals(MainActivity.HORAS[j])){
                        termina = true;
                    }
                    if(comienza == true && termina != true){
                        if(reservacionesDelDia.size() > 0) {
                            if(!siEntrada) {
                                for (int w = 0; w < reservacionesDelDia.size(); w++) {
                                    if (MainActivity.HORAS[j].equals(reservacionesDelDia.get(w).getHoraEntrada())) {
                                        siEntrada = true;
                                        posicionEntrada = w;
                                        break;
                                    }
                                }
                            }

                            if(siEntrada){
                                if(reservacionesDelDia.get(posicionEntrada).getHoraSalida().equals(MainActivity.HORAS[j])){
                                    siEntrada = false;
                                }
                            }else{
                                listHoras.add(MainActivity.HORAS[j]);
                            }

                        }else {
                            listHoras.add(MainActivity.HORAS[j]);
                        }
                    }else if(termina){
                        listHoras.add(MainActivity.HORAS[j]);
                        break;
                    }
                }
                break;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CRUDReservasActivity.this, android.R.layout.simple_dropdown_item_1line, listHoras);
        sp_horaEntrada.setAdapter(adapter);

        if(listHoras.size() > 0){
            sp_horaEntrada.setEnabled(true);
            sp_horaSalida.setEnabled(true);
            btn_reservas.setEnabled(true);
        }else{
            sp_horaEntrada.setEnabled(false);
            sp_horaSalida.setEnabled(false);
            btn_reservas.setEnabled(false);
        }
    }

    public void cargarHorasSalida(int inicio){
        ArrayList<String> listHoras2 = new ArrayList<>();
        if(listHoras.size() > 0){
            for(int i = inicio; i < listHoras.size(); i++){
                listHoras2.add(listHoras.get(i));
            }
        }else{
            listHoras2.clear();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CRUDReservasActivity.this, android.R.layout.simple_dropdown_item_1line, listHoras2);
        sp_horaSalida.setAdapter(adapter);
    }

    public ValueEventListener getReservaciones = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for(DataSnapshot items: snapshot.getChildren()){
                    Reservacion reservacion = items.getValue(Reservacion.class);
                    if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity") ||
                            getIntent().getStringExtra("ACTIVITY").equals("List_DipsMesas")) {
                        if (reservacion.getMesa().getIdMesa().equals(mesa.getIdMesa())) {
                            listReservaciones.add(reservacion);
                        }
                    }else{
                        listReservaciones.add(reservacion);

                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public ValueEventListener cargarMesas = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listMesas.clear();
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()){
                    Mesas mesa = items.getValue(Mesas.class);
                    if(mesa.getLocal().getIdLocal() == idLocalSeleccionado){
                        listMesas.add(mesa);
                    }


                }
                ArrayAdapter<Mesas> adapter = new ArrayAdapter<>(CRUDReservasActivity.this, android.R.layout.simple_dropdown_item_1line, listMesas);
                sp_mesas.setAdapter(adapter);
                if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity")){
                    for(int i = 0; i < listMesas.size(); i++){
                        if(listMesas.get(i).getIdMesa().equals(mesa.getIdMesa())){
                            sp_mesas.setSelection(i);
                            sp_mesas.setEnabled(false);
                            break;
                        }
                    }
                }
                /*
                if(listMesas.size() > 0 && !getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity")){
                    sp_mesas.setEnabled(true);
                }else{
                    sp_mesas.setEnabled(false);
                }*/

                //INTERACCION DEL EMPLEADO
                if(getIntent().getStringExtra("ACTIVITY").equals("List_DipsMesas")){
                    for(int i = 0; i < listMesas.size(); i++){
                        if(listMesas.get(i).getIdMesa().equals(mesa.getIdMesa())){
                            sp_mesas.setSelection(i);
                            break;
                        }
                    }
                }
                if(listMesas.size() > 0 && !getIntent().getStringExtra("ACTIVITY").equals("List_DipsMesas")){
                    sp_mesas.setEnabled(true);
                }else{
                    sp_mesas.setEnabled(false);
                }

                //VERIFICAR SI HAY MESAS DISPONIBLES PARA ALQUILAR SINO OCULTAR BOTON DE RESERVA
                if(listMesas.size() > 0){
                    btn_reservas.setEnabled(true);
                }else{
                    btn_reservas.setEnabled(false);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("onCancellerLocales",error.toString());
        }
    };

    public void btnFecha(View view) {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        anio = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String d = "";
                String m = "";
                if(i2 < 10){
                    d = "0" + i2;
                }else{
                    d = String.valueOf(i2);
                }
                if((i1+1) < 10){
                    m = "0" + (i1+1);
                }else{
                    m = String.valueOf(i1+1);
                }
                et_fecha.setText(d + "/" + m + "/" + i);


                ArrayList<Reservacion> listReservacionesDelDia = new ArrayList<>();
                //recorrer la lista de reservaciones y filtrarlas por la fecha seleccionada
                for(int q = 0; q < listReservaciones.size(); q++){
                    if(listReservaciones.get(q).getFecha().equals(et_fecha.getText().toString())){
                        listReservacionesDelDia.add(listReservaciones.get(q));

                        Log.d("Entra", "entra");
                    }
                }

                fecha.set(Calendar.YEAR, i);
                fecha.set(Calendar.MONTH, i1);
                fecha.set(Calendar.DAY_OF_MONTH, i2);
                switch(fecha.get(Calendar.DAY_OF_WEEK)){
                    case 1:
                        diaElegido = "Domingo";
                        Log.d("DIA","Domingo");
                        break;
                    case 2:
                        diaElegido = "Lunes";
                        Log.d("DIA","Lunes");
                        break;
                    case 3:
                        diaElegido = "Martes";
                        Log.d("DIA","Martes");
                        break;
                    case 4:
                        diaElegido = "Miercoles";
                        Log.d("DIA","Miercoles");
                        break;
                    case 5:
                        diaElegido = "Jueves";
                        Log.d("DIA","Jueves");
                        break;
                    case 6:
                        diaElegido = "Viernes";
                        Log.d("DIA","Viernes");
                        break;
                    case 7:
                        diaElegido = "Sabado";
                        Log.d("DIA","Sabado");
                        break;
                }
                cargarHorasEntrada(listReservacionesDelDia);

            }
        }
                , anio, mes, dia);
        datePickerDialog.show();
    }


    public void btnReservar(View view) {
        if(!et_precio.getText().toString().isEmpty() && !et_fecha.getText().toString().isEmpty()){
            cargarPagoReservaActivity();
        }else{
            Toast.makeText(this, "Debe lLenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarPagoReservaActivity(){
        Intent intent = new Intent(this, PagoReservaActivity.class);
        intent.putExtra("PRECIO", et_precio.getText().toString());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                DatabaseReference r = FirebaseDatabase.getInstance().getReference(MainActivity.TBL_PAGOS).child(data.getStringExtra("ID_PAGO"));
                r.addValueEventListener(getPago);
            }
        }
    }

    public ValueEventListener getPago = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                pago = snapshot.getValue(Pago.class);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(MainActivity.TBL_RESERVACIONES);

                Reservacion reservacion = new Reservacion();
                reservacion.setIdReservacion((int) id);
                reservacion.setFecha(et_fecha.getText().toString());
                reservacion.setHoraEntrada(sp_horaEntrada.getSelectedItem().toString());
                reservacion.setHoraSalida(sp_horaSalida.getSelectedItem().toString());
                reservacion.setMesa(mesaSeleccionada);
                reservacion.setPago(pago);

                myRef.child(String.valueOf(id)).setValue(reservacion);
                finish();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}