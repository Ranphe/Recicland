package main.ui.components;

import javax.swing.*;
import java.awt.*;

public class BotonConSombraUI extends javax.swing.plaf.basic.BasicButtonUI {
    protected void dibujarSombraYTexto(Graphics2D graficos2D, AbstractButton boton, String texto, int x, int y) {
        // Dibujar sombra
        graficos2D.setColor(Color.BLACK);
        graficos2D.drawString(texto, x + 2, y + 2);

        // Dibujar texto principal
        graficos2D.setColor(boton.getForeground());
        graficos2D.drawString(texto, x, y);
    }

    @Override
    protected void paintText(Graphics graficos, AbstractButton boton, Rectangle rectanguloTexto, String texto) {
        // Crear un contexto gráfico 2D
        Graphics2D graficos2D = (Graphics2D) graficos.create();
        graficos2D.setFont(boton.getFont());

        // Calcular las métricas de la fuente para centrar el texto
        FontMetrics metricasFuente = graficos2D.getFontMetrics();
        int x = (boton.getWidth() - metricasFuente.stringWidth(texto)) / 2;
        int y = (boton.getHeight() + metricasFuente.getAscent()) / 2 - metricasFuente.getDescent();

        // Dibujar el texto con sombra
        dibujarSombraYTexto(graficos2D, boton, texto, x, y);

        // Liberar los recursos gráficos
        graficos2D.dispose();
    }
}