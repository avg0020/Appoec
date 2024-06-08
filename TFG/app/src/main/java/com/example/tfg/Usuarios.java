package com.example.tfg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuarios implements Serializable {

    public String correo;
    public String nombre;
    public String password;
    public String telefono;
    public String apellido1;
    public String apellido2;
    public String rol;
    public Map<String, Hijo> hijos;

    private List<String> actividades;

    // Constructor


    public Usuarios(String correo, String nombre, String password, String telefono, String apellido1, String apellido2, String rol, Map<String, Hijo> hijos) {
        this.correo = correo;
        this.nombre = nombre;
        this.password = password;
        this.telefono = telefono;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.rol = rol;
        this.hijos = hijos;
    }

    public Usuarios() {
    }

    public List<String> getActividades() {
        return actividades;
    }

    public void setActividades(List<String> actividades) {
        this.actividades = actividades;
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

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Map<String, Hijo> getHijos() {
        return hijos;
    }

    public void setHijos(Map<String, Hijo> hijos) {
        this.hijos = hijos;
    }
}
