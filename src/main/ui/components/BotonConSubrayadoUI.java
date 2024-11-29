package main.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonConSubrayadoUI extends BotonConSombraUI {
    private boolean subrayadoActivo = false;

    public BotonConSubrayadoUI(JButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evento) {
                // Activa el subrayado al entrar el mouse
                subrayadoActivo = true;
                boton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evento) {
                // Desactiva el subrayado al salir el mouse
                subrayadoActivo = false;
                boton.repaint();
            }
        });
    }

    @Override
    public void paint(Graphics graficos, JComponent componente) {
        // Llama al metodo de la superclase para pintar el texto y sombra
        super.paint(graficos, componente);

        // Dibuja el subrayado si está activo
        if (subrayadoActivo) {
            Graphics2D graficos2D = (Graphics2D) graficos;

            // Configurar color y grosor de la línea de subrayado
            graficos2D.setColor(Color.WHITE);
            graficos2D.setStroke(new BasicStroke(2));

            // Calcula la posición vertical del subrayado (cerca de la parte inferior del botón)
            int y = componente.getHeight() - 5;

            // Dibuja una línea horizontal para el subrayado
            graficos2D.drawLine(3, y, componente.getWidth() - 5, y);
        }
    }
}