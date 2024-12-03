package main;

import main.ui.windows.*;
import main.utils.Fuente;
import main.utils.Imagen;
import main.utils.Sonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Recicland {
    private static Sonido musicaMenu;

    public static void main(String[] args) {
        // Cargar fuente del proyecto
        Fuente.cargarFuente("/fonts/minecraft.ttf");

        // Crear el JFrame principal
        JFrame frame = new JFrame("Recicland 0.80.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.setLocationRelativeTo(null);
        frame.setIconImage(Imagen.cargarImagen("/images/GUI/windowIcon.png"));

        // Crear el CardLayout y el panel principal
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Crear instancias de los paneles
        MenuRecicland menuReciclandPanel = new MenuRecicland(cardLayout, mainPanel);
        Jugar jugarPanel = new Jugar(cardLayout, mainPanel);
        Reglas reglas = new Reglas(cardLayout, mainPanel);
        Leaderboard leaderboard = new Leaderboard(cardLayout, mainPanel);
        Idioma idioma = new Idioma(cardLayout, mainPanel);
        Opciones opciones = new Opciones(cardLayout, mainPanel);
        Accesibilidad accesibilidad = new Accesibilidad(cardLayout, mainPanel);

        // Agregar los paneles al mainPanel con identificadores
        mainPanel.add(menuReciclandPanel, "menuRecicland");
        mainPanel.add(jugarPanel, "jugar");
        mainPanel.add(reglas, "reglas");
        mainPanel.add(leaderboard, "leaderboard");
        mainPanel.add(idioma, "idioma");
        mainPanel.add(opciones, "opciones");
        mainPanel.add(accesibilidad, "accesibilidad");

        // Establecer el mainPanel como contenido del frame
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        // Crear y reproducir la música de fondo para los menús
        musicaMenu = new Sonido("/sounds/menuMusic.wav");
        musicaMenu.reproducirEnBucle();
        musicaMenu.setVolumen(0.8f);

        // Detener la música al cerrar la aplicación
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                musicaMenu.detener();
                super.windowClosing(e);
            }
        });
    }

    public static void detenerMusicaMenu() {
        if (musicaMenu != null) {
            musicaMenu.detener();
        }
    }

    public static void reproducirMusicaMenu() {
        if (musicaMenu != null) {
            musicaMenu.reproducirEnBucle();
        }
    }
}