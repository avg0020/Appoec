package com.example.tfg;

import java.io.Serializable;
import java.util.List;

public class Alumno implements Serializable {
    public String apellido1;
    public String apellido2;
    public String edad;
    public List<String> actividades;
    public List<String> alergeno;

    public Alumno(String apellido1, String apellido2, String edad, List<String> actividades, List<String> alergeno) {
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.actividades = actividades;
        this.alergeno = alergeno;
    }

    public Alumno() {
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

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public List<String> getActividades() {
        return actividades;
    }

    public void setActividades(List<String> actividades) {
        this.actividades = actividades;
    }

    public List<String> getAlergeno() {
        return alergeno;
    }

    public void setAlergeno(List<String> alergeno) {
        this.alergeno = alergeno;
    }
}
