package com.example.pdm_alquilermesasproyectofinal.modelos;

import androidx.annotation.NonNull;

public class Mesas {
    private String idMesa;
    private int capacidad;
    private EstadoMesa estado;
    private Local local;
    private int numeroMesa;
    private double precioReserva;
    private String foto;

    public Mesas() {
    }

    public Mesas(String idMesa, int capacidad, EstadoMesa estado, Local local, int numeroMesa, double precioReserva, String foto) {
        this.idMesa = idMesa;
        this.capacidad = capacidad;
        this.estado = estado;
        this.local = local;
        this.numeroMesa = numeroMesa;
        this.precioReserva = precioReserva;
        this.foto = foto;
    }

    public String getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(String idMesa) {
        this.idMesa = idMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public double getPrecioReserva() {
        return precioReserva;
    }

    public void setPrecioReserva(double precioReserva) {
        this.precioReserva = precioReserva;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(numeroMesa);
    }
}
