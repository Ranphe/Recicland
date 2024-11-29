package main.models;

import java.util.List;

public class Desecho {
    private String nombre;
    private Contenedor tipoContenedor;
    private List<String> pasosTratamiento;
    public Desecho(String nombre, Contenedor tipoContenedor, List<String> pasosTratamiento) {
        this.nombre = nombre;
        this.tipoContenedor = tipoContenedor;
        this.pasosTratamiento = pasosTratamiento;
    }
    public String getNombre() {
        return nombre;
    }
    public Contenedor getTipoContenedor() {
        return tipoContenedor;
    }
    public List<String> getPasosTratamiento() {
        return pasosTratamiento;
    }
}