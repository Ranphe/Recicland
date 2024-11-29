package main.models;

import java.util.ArrayList;
import java.util.List;

public abstract class Nivel {
    protected String nombreNivel;
    protected String dificultad;
    protected int puntosPorRespuesta;
    protected int vidasPerdidasPorError;
    protected int desechosAClasificar;
    protected int objetivo;
    protected int maxTiempoPorJugador;
    protected List<Desecho> desechos;
    public Nivel() {
        desechos = new ArrayList<>();
    }
    public abstract void configurarNivel();
    public String getNombreNivel() {
        return nombreNivel;
    }
    public String getDificultad() {
        return dificultad;
    }
    public int getPuntosPorRespuesta() {
        return puntosPorRespuesta;
    }
    public int getVidasPerdidasPorError() {
        return vidasPerdidasPorError;
    }
    public int getDesechosAClasificar() {
        return desechosAClasificar;
    }
    public int getObjetivo() {
        return objetivo;
    }
    public int getMaxTiempoPorJugador() {
        return maxTiempoPorJugador;
    }
    public List<Desecho> getDesechos() {
        return desechos;
    }
}