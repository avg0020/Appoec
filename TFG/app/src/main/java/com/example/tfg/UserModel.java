package com.example.tfg;

public class UserModel {
    private String nombre, apellidos;


    public UserModel() {

    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String toString() {
        return nombre+apellidos;
    }
}