package com.example.tfg;

import java.util.List;

public class Empleado {
    private List<String> actividades;
    private boolean comedor;
    private String correo;
    private String nombre;
    private String password;
    private String tel;

    // Constructor
    public Empleado( List<String> actividades, boolean comedor, String correo, String nombre, String password, String tel) {
        this.actividades = actividades;
        this.comedor = comedor;
        this.correo = correo;
        this.nombre = nombre;
        this.password = password;
        this.tel = tel;
    }

    // Getters y setters
    public List<String> getActividades() {
        return actividades;
    }

    public void setActividades(List<String> actividades) {
        this.actividades = actividades;
    }

    public boolean isComedor() {
        return comedor;
    }

    public void setComedor(boolean comedor) {
        this.comedor = comedor;
    }

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
