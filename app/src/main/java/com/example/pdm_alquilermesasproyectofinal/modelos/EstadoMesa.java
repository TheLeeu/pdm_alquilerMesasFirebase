package com.example.pdm_alquilermesasproyectofinal.modelos;

import androidx.annotation.NonNull;

public class EstadoMesa {
    private int idEstadoMesa;
    private String estadoMesa;

    public EstadoMesa(int idEstadoMesa, String estadoMesa) {
        this.idEstadoMesa = idEstadoMesa;
        this.estadoMesa = estadoMesa;
    }

    public EstadoMesa() {
    }

    public int getIdEstadoMesa() {
        return idEstadoMesa;
    }

    public void setIdEstadoMesa(int idEstadoMesa) {
        this.idEstadoMesa = idEstadoMesa;
    }

    public String getEstadoMesa() {
        return estadoMesa;
    }

    public void setEstadoMesa(String estadoMesa) {
        this.estadoMesa = estadoMesa;
    }

    @NonNull
    @Override
    public String toString() {
        return estadoMesa;
    }
}
