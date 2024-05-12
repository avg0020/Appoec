package com.example.tfg;

public class Actividades {
    private String horario;
    private String nombre;
    private String categoria;
    private String icono;
    private String color;
    private int participantes;
    private int participantesMax;

    public Actividades() {

    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public int getParticipantesMax() {
        return participantesMax;
    }

    public void setParticipantesMax(int participantesMax) {
        this.participantesMax = participantesMax;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

}
