package main.levels;

import main.models.Nivel;
import main.models.RepositorioDesechos;

public class Nivel3 extends Nivel {
    public Nivel3() {
        super();
        configurarNivel();
    }

    @Override
    public void configurarNivel() {
        nombreNivel = "3";
        dificultad = "Alta (más categorías y desechos que son difíciles de clasificar).";
        puntosPorRespuesta = 20;
        vidasPerdidasPorError = 2;
        desechosAClasificar = 10;
        objetivo = 9;
        maxTiempoPorJugador = 1 * 60 * 1000;

        desechos = RepositorioDesechos.getDesechosNivel3();
        desechos.addAll(RepositorioDesechos.getDesechosNivel1());
        desechos.addAll(RepositorioDesechos.getDesechosNivel2());
    }
}