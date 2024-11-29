package main.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Imagen {
    public static Image cargarImagen(String rutaImagen) {
        Image imagen = null;
        try {
            URL url = Imagen.class.getResource(rutaImagen);
            if (url == null) {
                throw new IOException();
            }
            imagen = new ImageIcon(url).getImage();
        } catch (Exception e) {
            System.err.println("Error de E/S al cargar el archivo de imagen: " + rutaImagen);
        }

        // Si la imagen es nula, retorna una imagen por defecto
        if (imagen == null) {
            imagen = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB); // Imagen predeterminada de 100x100 píxeles
        }

        return imagen;
    }
    public static boolean esImagenPredeterminada(Image imagen) {
        // Verifica si la imagen es una instancia de BufferedImage con un tamaño de 100x100 y tipo ARGB (imagen predeterminada)
        if (imagen instanceof BufferedImage) {
            BufferedImage bufferedImage = (BufferedImage) imagen;
            return bufferedImage.getWidth() == 100 && bufferedImage.getHeight() == 100 && bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB;
        }
        return false;
    }
}