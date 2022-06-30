package com.example.pdm_alquilermesasproyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pdm_alquilermesasproyectofinal.R;
import com.example.pdm_alquilermesasproyectofinal.modelos.Reservacion;

import java.util.ArrayList;

public class AdaptadorReservas extends BaseAdapter {

    private ArrayList<Reservacion> data;
    private Context context;

    public AdaptadorReservas(ArrayList<Reservacion> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Reservacion reserva = (Reservacion) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_reserva, null);

        TextView idMesa = view.findViewById(R.id.tvIdMesaItemReserva);
        TextView fecha = view.findViewById(R.id.tvFechaItemReserva);
        TextView horaE = view.findViewById(R.id.tvHoraEntradaItemReserva);
        TextView horaS = view.findViewById(R.id.tvHoraSalidaItemReserva);
        TextView nombre = view.findViewById(R.id.tvNombreUsuarioItemReserva);
        TextView telefono = view.findViewById(R.id.tvTelefonoUsuarioItemReserva);


        idMesa.setText("id mesa: "+reserva.getMesa().getIdMesa());
        fecha.setText("fecha: " + reserva.getFecha());
        horaE.setText("Hora Entrada: "+reserva.getHoraEntrada());
        horaS.setText("Hora Salida: "+ reserva.getHoraSalida());
        nombre.setText("Nombre cliente: "+reserva.getPago().getUsuario().getNombre());
        telefono.setText("Telefono cliente: "+reserva.getPago().getUsuario().getTelefono());

        return view;
    }
}
