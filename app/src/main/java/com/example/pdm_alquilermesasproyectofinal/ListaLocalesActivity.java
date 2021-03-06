package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorLocal;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaLocalesActivity extends AppCompatActivity {

    private ArrayList<Local> listLocal;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private ListView lv_locales;
    private int posicionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_locales);

        lv_locales = (ListView)findViewById(R.id.listHorariosAtencion);

        listLocal = new ArrayList<Local>();
        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);

        lv_locales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posicionItem = i;
                abrirDialogo();
            }
        });
    }

    public ValueEventListener cargarLocales = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                listLocal.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    Local local = items.getValue(Local.class);
                    listLocal.add(local);
                }
                AdaptadorLocal ap = new AdaptadorLocal(listLocal, getApplicationContext());
                lv_locales.setAdapter(ap);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        referenciaData.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);
    }

    public void btnAgregar(View view) {
        cargarCRUDLocalActivity("btnAgregar");
    }

    public void cargarCRUDLocalActivity(String elemento){
        Intent intent = new Intent(this, CRUDLocalActivity.class);
        if(elemento.equals("btnAgregar")){
            intent.putExtra("ListaLocalesActivity", elemento);
            startActivity(intent);
        }else if(elemento.equals("item")){
            intent.putExtra("ListaLocalesActivity", elemento);
            intent.putExtra("idLocal", String.valueOf(listLocal.get(posicionItem).getIdLocal()));
            intent.putExtra("nombre", listLocal.get(posicionItem).getNombre());
            intent.putExtra("direccion",listLocal.get(posicionItem).getDireccion());
            intent.putExtra("telefono", listLocal.get(posicionItem).getTelefono());
            intent.putExtra("coordenadas", listLocal.get(posicionItem).getCoordenadasGps());
            intent.putExtra("foto", listLocal.get(posicionItem).getFoto());
            startActivity(intent);
        }



    }

    public void abrirDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione una opcion")
                .setItems(new String[]{"Modificar datos de local", "Horarios de atenci??n", "Mesas de local",
                "Empleados de local", "Reservas de local"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Log.d("which", String.valueOf(which));
                        switch (which){
                            case 0:
                                cargarCRUDLocalActivity("item");
                                break;
                            case 1:
                                Intent i = new Intent(getApplicationContext(), ListaHorariosAtencionActivity.class);
                                i.putExtra("ACTIVITY", "ListaLocalesActivity");
                                i.putExtra("idLocal", String.valueOf(listLocal.get(posicionItem).getIdLocal()));
                                i.putExtra("nombreLocal", listLocal.get(posicionItem).getNombre());
                                i.putExtra("direccionLocal", listLocal.get(posicionItem).getDireccion());
                                i.putExtra("telefonoLocal", listLocal.get(posicionItem).getTelefono());
                                i.putExtra("coordenadasLocal", listLocal.get(posicionItem).getCoordenadasGps());
                                i.putExtra("fotoLocal", listLocal.get(posicionItem).getFoto());
                                startActivity(i);
                                break;
                            case 2:
                                Intent intent = new Intent(getApplicationContext(), ListaMesasActivity.class);
                                intent.putExtra("ACTIVITY", "ListaLocalesActivity");
                                intent.putExtra("idLocal", String.valueOf(listLocal.get(posicionItem).getIdLocal()));
                                intent.putExtra("nombreLocal", listLocal.get(posicionItem).getNombre());
                                intent.putExtra("direccionLocal", listLocal.get(posicionItem).getDireccion());
                                intent.putExtra("telefonoLocal", listLocal.get(posicionItem).getTelefono());
                                intent.putExtra("coordenadasLocal", listLocal.get(posicionItem).getCoordenadasGps());
                                intent.putExtra("fotoLocal", listLocal.get(posicionItem).getFoto());
                                startActivity(intent);
                                break;
                            case 3:
                                Intent it = new Intent(getApplicationContext(), ListaEmpleadosActivity.class);
                                it.putExtra("ACTIVITY", "ListaLocalesActivity");
                                it.putExtra("idLocal", String.valueOf(listLocal.get(posicionItem).getIdLocal()));
                                it.putExtra("nombreLocal", listLocal.get(posicionItem).getNombre());
                                it.putExtra("direccionLocal", listLocal.get(posicionItem).getDireccion());
                                it.putExtra("telefonoLocal", listLocal.get(posicionItem).getTelefono());
                                it.putExtra("coordenadasLocal", listLocal.get(posicionItem).getCoordenadasGps());
                                it.putExtra("fotoLocal", listLocal.get(posicionItem).getFoto());
                                startActivity(it);
                                break;
                            case 4:
                                Intent ite = new Intent(getApplicationContext(), ListaReservasLocalAdministradorActivity.class);
                                ite.putExtra("ACTIVITY", "ListaLocalesActivity");
                                ite.putExtra("idLocal", String.valueOf(listLocal.get(posicionItem).getIdLocal()));
                                ite.putExtra("nombreLocal", listLocal.get(posicionItem).getNombre());
                                ite.putExtra("direccionLocal", listLocal.get(posicionItem).getDireccion());
                                ite.putExtra("telefonoLocal", listLocal.get(posicionItem).getTelefono());
                                ite.putExtra("coordenadasLocal", listLocal.get(posicionItem).getCoordenadasGps());
                                ite.putExtra("fotoLocal", listLocal.get(posicionItem).getFoto());
                                startActivity(ite);
                                break;
                        }
                    }
                }).show();
    }

}