package com.example.tfg;

import java.util.List;

public class Hijo {
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
}
