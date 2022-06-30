package com.example.pdm_alquilermesasproyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pdm_alquilermesasproyectofinal.R;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;

import java.util.ArrayList;

public class AdaptadorEmpleado extends BaseAdapter {

    private ArrayList<Empleado> data;
    private Context context;

    public AdaptadorEmpleado(ArrayList<Empleado> data, Context context) {
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
        Empleado empleado = (Empleado) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_empleado, null);

        ImageView img = view.findViewById(R.id.imgItemEmpleado);
        TextView nombre = view.findViewById(R.id.tvNombreItemEmpleado);
        TextView apellido = view.findViewById(R.id.tvApellidoItemEmpleado);
        TextView correo = view.findViewById(R.id.tvCorreoItemEmpleado);
        TextView telefono = view.findViewById(R.id.tvTelefonoItemEmpleado);

        Glide.with(context).load(empleado.getUsuario().getFoto()).into(img);
        nombre.setText("Nombre: "+empleado.getUsuario().getNombre());
        apellido.setText("Apellido: "+empleado.getUsuario().getApellido());
        correo.setText("Correo: "+ empleado.getUsuario().getCorreo());
        telefono.setText("Telefono: "+ empleado.getUsuario().getTelefono());

        return view;
    }
}
