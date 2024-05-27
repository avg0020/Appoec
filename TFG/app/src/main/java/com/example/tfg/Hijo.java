package com.example.tfg;

import java.io.Serializable;
import java.util.List;

public class Hijo implements Serializable {
    public String apellido1;
    public String apellido2;
    public String edad;
    public List<String> alergenos;
    public List<String> actividades;

    // Constructor por defecto necesario para DataSnapshot.getValue(Hijo.class)
    public Hijo() {
    }

    public Hijo(String apellido1, String apellido2, String edad, List<String> alergenos, List<String> actividades) {
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.alergenos = alergenos;
        this.actividades = actividades;
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

    public List<String> getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(List<String> alergenos) {
        this.alergenos = alergenos;
    }

    public List<String> getActividades() {
        return actividades;
    }

    public void setActividades(List<String> actividades) {
        this.actividades = actividades;
    }
}
