package com.example.pdm_alquilermesasproyectofinal.modelos;

public class Empleado {
    private Usuario usuario;
    private Local local;

    public Empleado() {
    }

    public Empleado(Usuario usuario, Local local) {
        this.usuario = usuario;
        this.local = local;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
