package main.utils;

import javax.swing.*;
import java.awt.*;

public class TextoAnimado extends JLabel {
    private static final float MAX_ESCALA = 1.0625f;
    private static final float MIN_ESCALA = 1.0f;
    private float escala = 1.0625f;
    private float incremento = 0.0625f;
    private int delay = 500;
    private Timer temporizador;

    public TextoAnimado(String texto) {
        super(texto);
        setFont(new Font("minecraft", Font.PLAIN, 15));
        setForeground(Color.YELLOW);
        setPreferredSize(new Dimension(calcularAncho(texto), calcularAncho(texto)));
        setSize(getPreferredSize());

        // Iniciar el temporizador para animación
        temporizador = new Timer(delay, e -> animarEscala());
        temporizador.start();
    }

    private int calcularAncho(String texto) {
        FontMetrics metricasFuente = getFontMetrics(getFont());
        return (int) (metricasFuente.stringWidth(texto) * MAX_ESCALA);
    }

    private void animarEscala() {
        if (escala >= MAX_ESCALA || escala <= MIN_ESCALA) incremento = -incremento;
        escala += incremento;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D graficos2D = (Graphics2D) graficos.create();

        // Configurar la rotación y el escalado desde el centro del JLabel
        graficos2D.translate(getWidth() / 2, getHeight() / 2);
        graficos2D.rotate(Math.toRadians(-22.5));
        graficos2D.scale(escala, escala);
        graficos2D.translate(-getWidth() / 2, -getHeight() / 2);

        // Obtener las métricas de la fuente
        FontMetrics metricasFuente = graficos2D.getFontMetrics();
        int x = (getWidth() - metricasFuente.stringWidth(getText())) / 2;
        int y = (getHeight() + metricasFuente.getAscent()) / 2 - metricasFuente.getDescent();

        // Dibujar sombra
        graficos2D.setColor(Color.BLACK);
        graficos2D.drawString(getText(), x + 2, y + 2);

        // Dibujar texto principal
        graficos2D.setColor(getForeground());
        graficos2D.drawString(getText(), x, y);

        graficos2D.dispose();
    }
}