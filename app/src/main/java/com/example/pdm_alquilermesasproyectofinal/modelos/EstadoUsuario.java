package com.example.pdm_alquilermesasproyectofinal.modelos;

public class EstadoUsuario {
    private int idEstado;
    private String estado;

    public EstadoUsuario(int idEstado, String estado) {
        this.idEstado = idEstado;
        this.estado = estado;
    }

    public EstadoUsuario() {
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return estado;
    }
}
