package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Pago;
import com.example.pdm_alquilermesasproyectofinal.modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class PagoReservaActivity extends AppCompatActivity {
    private TextView tv_mensaje;
    private EditText et_nombre, et_numeroTarjeta, et_fechaV, et_CVV;
    Usuario usuario = new Usuario();
    private DatabaseReference ref;
    private long nuevoId = 0;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_reserva);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference();

        tv_mensaje = (TextView) findViewById(R.id.tvMensajePagoReservaActivity);
        et_nombre = (EditText) findViewById(R.id.etNombrePagoReservaActivity);
        et_numeroTarjeta = (EditText) findViewById(R.id.etNumeroTarjetaPagoReservaActivity);
        et_fechaV = (EditText) findViewById(R.id.etFechaVencimientoPagoReservaActivity);
        et_CVV = (EditText) findViewById(R.id.etCVVPagoReservaActivity);

        tv_mensaje.setText("Ingrese los datos para efectuar su reservación. Total a pagar: $"+getIntent().getStringExtra("PRECIO"));
        ref.child(MainActivity.TBL_USUARIOS).child(currentUser.getUid()).addValueEventListener(getUsuario);

        DatabaseReference r = FirebaseDatabase.getInstance().getReference(MainActivity.TBL_PAGOS);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nuevoId = (snapshot.getChildrenCount());
                    Log.d("NUEVO_ID_PAGO", String.valueOf(nuevoId));
                }else{
                    Log.d("NUEVO_ID_PAGO", String.valueOf(nuevoId));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void btnPagar(View view) {
        //VERIFICAR QUE LOS CAMPOS NO ESTEN VACIOS
        if(!et_nombre.getText().toString().isEmpty() && !et_numeroTarjeta.getText().toString().isEmpty() && ! et_fechaV.getText().toString().isEmpty() &&
        !et_CVV.getText().toString().isEmpty()){

            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(MainActivity.TBL_PAGOS);
            Pago pago = new Pago();
            pago.setIdPago((int) nuevoId);
            pago.setNombre(et_nombre.getText().toString());
            pago.setnTarjeta(et_numeroTarjeta.getText().toString());
            pago.setCvv(et_CVV.getText().toString());
            pago.setPrecio(Double.parseDouble(getIntent().getStringExtra("PRECIO")));
            pago.setUsuario(usuario);
            myRef.child(String.valueOf(nuevoId)).setValue(pago);
            Intent intent = new Intent();
            intent.putExtra("ID_PAGO", String.valueOf(nuevoId));
            setResult(RESULT_OK,intent);
            finish();

        }else{
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public ValueEventListener getUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                usuario = snapshot.getValue(Usuario.class);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}