package com.example.tfg;

public class Usuario {
    private String correo;
    private String nombre;
    private String password;
    private String telefono;

    // Constructor
    public Usuario(String correo, String nombre, String password, String telefono) {
        this.correo = correo;
        this.nombre = nombre;
        this.password = password;
        this.telefono = telefono;
    }

    // Getters y setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
