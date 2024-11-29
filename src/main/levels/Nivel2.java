package main.levels;

import main.models.Nivel;
import main.models.RepositorioDesechos;

public class Nivel2 extends Nivel {
    public Nivel2() {
        super();
        configurarNivel();
    }

    @Override
    public void configurarNivel() {
        nombreNivel = "2";
        dificultad = "Media (más categorías y desechos menos comunes).";
        puntosPorRespuesta = 15;
        vidasPerdidasPorError = 1;
        desechosAClasificar = 10;
        objetivo = 8;
        maxTiempoPorJugador = 2 * 60 * 1000;

        desechos = RepositorioDesechos.getDesechosNivel2();
        desechos.addAll(RepositorioDesechos.getDesechosNivel1());
    }
}
