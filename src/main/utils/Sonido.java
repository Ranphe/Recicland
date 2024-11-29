package main.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sonido {
    private Clip clip;

    public Sonido(String rutaSonido) {
        try {
            URL url = getClass().getResource(rutaSonido);
            if (url == null) {
                throw new IOException();
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            // Inicializa el Clip y carga los datos del flujo de audio
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            System.err.println("Error de E/S al cargar el archivo de sonido: " + rutaSonido);
        }
    }

    public void reproducir() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void reproducirEnBucle() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void detener() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}