package main.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sonido {
    private Clip clip;
    private FloatControl controlVolumen;

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

            // Obtener el control de volumen
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                controlVolumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.err.println("Control de volumen no soportado.");
            }
        } catch (Exception e) {
            System.err.println("Error de E/S al cargar el archivo de sonido: " + rutaSonido);
        }
    }

    public void setVolumen(float nivel) {
        if (controlVolumen != null && nivel >= 0.0f && nivel <= 1.0f) {
            float min = controlVolumen.getMinimum();
            float max = controlVolumen.getMaximum();
            float dB = min + (max - min) * nivel;
            controlVolumen.setValue(dB);
        } else {
            System.err.println("Nivel de volumen fuera de rango o control no disponible.");
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