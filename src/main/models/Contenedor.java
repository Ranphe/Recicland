package main.models;

public class Contenedor {
    private String nombre;
    private int usos;
    public Contenedor(String nombre) {
        this.nombre = nombre;
        this.usos = 1;
    }
    public String getNombre() {
        return nombre;
    }
    public int getUsos() {
        return usos;
    }
    public void incrementarUsos() {
        usos++;
    }
}