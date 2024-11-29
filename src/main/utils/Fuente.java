package main.utils;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Fuente {
    public static void cargarFuente(String rutaFuente) {
        Font fuente;
        try {
            URL url = Fuente.class.getResource(rutaFuente);
            if (url == null) {
                throw new IOException();
            }
            fuente = Font.createFont(Font.TRUETYPE_FONT, url.openStream());

            // Registrar la fuente en el entorno gráfico local.
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fuente);
        } catch (Exception e) {
            System.err.println("Error de E/S al cargar el archivo de fuente: " + rutaFuente);
        }
    }
}