package com.example.pdm_alquilermesasproyectofinal.modelos;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String apellido;
    private int edad;
    private String foto;
    private String correo;
    private String telefono;
    //ADMINISTRADOR, CLIENTE, EMPLEADO
    private TipoUsuario tipo;
    //USUARIO ACTIVO, USUARIO DE BAJA, BLOQUEADO, ETC
    private EstadoUsuario estado;

    public Usuario() {
    }

    public Usuario(String idUsuario, String nombre, String apellido, int edad, String foto, String correo, String telefono, TipoUsuario tipo, EstadoUsuario estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.foto = foto;
        this.correo = correo;
        this.telefono = telefono;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
}
