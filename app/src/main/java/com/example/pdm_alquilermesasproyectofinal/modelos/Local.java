package com.example.pdm_alquilermesasproyectofinal.modelos;

import androidx.annotation.NonNull;

public class Local {
    private int idLocal;
    private String nombre;
    private String direccion;
    private String telefono;
    private String coordenadasGps;
    private String foto;

    public Local() {
    }

    public Local(int idLocal, String nombre, String direccion, String telefono, String coordenadasGps, String foto) {
        this.idLocal = idLocal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.coordenadasGps = coordenadasGps;
        this.foto = foto;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCoordenadasGps() {
        return coordenadasGps;
    }

    public void setCoordenadasGps(String coordenadasGps) {
        this.coordenadasGps = coordenadasGps;
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
        return nombre;
    }
}
