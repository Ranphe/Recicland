package main.models;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private int vidas;
    private int puntos;
    private boolean eliminado;
    private List<Desecho> desechosCorrectamenteClasificados = new ArrayList<>();
    private List<Contenedor> contenedoresUsados;
    public Jugador(String nombre, int vidas) {
        this.nombre = nombre;
        this.vidas = vidas;
        this.puntos = 0;
        this.eliminado = false;
        this.desechosCorrectamenteClasificados = new ArrayList<>();
        this.contenedoresUsados = new ArrayList<>();
    }
    public String getNombre() {
        return nombre;
    }
    public int getVidas() {
        return vidas;
    }
    public void restarVida(int cantidad) {
        vidas -= cantidad;
    }
    public int getPuntos() {
        return puntos;
    }
    public void sumarPuntos(int cantidad) {
        puntos += cantidad;
    }
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    public boolean isEliminado() {
        return eliminado;
    }
    public void agregarDesechoCorrectamenteClasificado(Desecho desecho) {
        if (!desechosCorrectamenteClasificados.contains(desecho)) {
            desechosCorrectamenteClasificados.add(desecho);
        }
    }
    public List<Desecho> getDesechosCorrectamenteClasificados() {
        return desechosCorrectamenteClasificados;
    }
    public void usarContenedor(Contenedor contenedor) {
        int i = 0;
        boolean encontrado = false;
        while (i < contenedoresUsados.size() && !encontrado) {
            if (contenedoresUsados.get(i).getNombre().equalsIgnoreCase(contenedor.getNombre())) {
                contenedoresUsados.get(i).incrementarUsos();
                encontrado = true;
            }
            i++;
        }
        if (!encontrado) contenedoresUsados.add(contenedor);
    }
    public Contenedor contenedorMasUsado() {
        if (contenedoresUsados.isEmpty()) {
            return null;
        }
        Contenedor maxContenedor = contenedoresUsados.get(0);
        for (int i = 1; i < contenedoresUsados.size(); i++) {
            if (contenedoresUsados.get(i).getUsos() > maxContenedor.getUsos()) {
                maxContenedor = contenedoresUsados.get(i);
            }
        }
        return maxContenedor;
    }
}