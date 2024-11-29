package main.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagenAnimada extends JLabel {

    private BufferedImage[] fotogramas;
    private int[] tiemposFotogramas;
    private int fotogramaActual = 0;
    private Timer temporizador;

    public ImagenAnimada(String rutaImagen, int cantidadFotogramas, int[] tiemposFotogramas) {
        this.tiemposFotogramas = tiemposFotogramas;
        cargarFotogramas(rutaImagen, cantidadFotogramas);
        iniciarAnimacion();
    }

    private void cargarFotogramas(String rutaImagen, int cantidadFotogramas) {
        Image hojaSpritesImage = Imagen.cargarImagen(rutaImagen);

        BufferedImage hojaSprites;

        // Verificar si la imagen cargada es un BufferedImage
        if (hojaSpritesImage instanceof BufferedImage) {
            hojaSprites = (BufferedImage) hojaSpritesImage;
        } else {
            hojaSprites = new BufferedImage(
                    hojaSpritesImage.getWidth(null),
                    hojaSpritesImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
            );

            // Dibujar la imagen original en el nuevo BufferedImage
            Graphics2D g2d = hojaSprites.createGraphics();
            g2d.drawImage(hojaSpritesImage, 0, 0, null);
            g2d.dispose();
        }

        // Calcular las dimensiones de cada fotograma
        int alturaFotograma = hojaSprites.getHeight() / cantidadFotogramas;
        int anchoFotograma = hojaSprites.getWidth();

        // Crear el arreglo de fotogramas
        fotogramas = new BufferedImage[cantidadFotogramas];

        // Extraer cada fotograma de la hoja de sprites
        for (int i = 0; i < cantidadFotogramas; i++) {
            fotogramas[i] = hojaSprites.getSubimage(0, i * alturaFotograma, anchoFotograma, alturaFotograma);
        }
    }

    private void iniciarAnimacion() {
        // Crear un temporizador que actualice el fotograma actual según los tiempos especificados
        temporizador = new Timer(tiemposFotogramas[fotogramaActual] * 100, e -> actualizarFotograma());
        temporizador.start();
    }

    private void actualizarFotograma() {
        // Avanzar al siguiente fotograma (cíclico)
        fotogramaActual = (fotogramaActual + 1) % fotogramas.length;

        // Redibujar el componente para mostrar el nuevo fotograma
        repaint();

        // Actualizar el tiempo de retraso del temporizador para el nuevo fotograma
        temporizador.setDelay(tiemposFotogramas[fotogramaActual] * 100);
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);

        // Dibujar el fotograma actual si los fotogramas están cargados
        if (fotogramas != null && fotogramas.length > 0) {
            Image fotogramaActualImagen = fotogramas[fotogramaActual];
            graficos.drawImage(fotogramaActualImagen, 0, 0, getWidth(), getHeight(), null);
        }
    }
}