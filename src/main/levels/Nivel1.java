package main.levels;

import main.models.Nivel;
import main.models.RepositorioDesechos;

public class Nivel1 extends Nivel {
    public Nivel1() {
        super();
        configurarNivel();
    }

    @Override
    public void configurarNivel() {
        nombreNivel = "1";
        dificultad = "Baja (desechos comunes, pocas categorías).";
        puntosPorRespuesta = 10;
        vidasPerdidasPorError = 1;
        desechosAClasificar = 10;
        objetivo = 7;
        maxTiempoPorJugador = 3 * 60 * 1000;

        desechos = RepositorioDesechos.getDesechosNivel1();
    }
}