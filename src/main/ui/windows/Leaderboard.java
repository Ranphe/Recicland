package main.ui.windows;

import main.ui.components.Boton;
import main.utils.Imagen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.*;

public class Leaderboard extends JPanel {
    public CardLayout cardLayout;
    public JPanel mainPanel;
    private static DefaultListModel<String> modeloLista;

    private Image fondo;
    private Image botonMediano, botonMedianoResaltado;

    // Componentes y paneles
    private JButton botonRegresar;
    private JPanel panelFondo;
    private JPanel panelObscurecido;
    private JPanel panelContenido;
    private JScrollPane scrollLista;

    public Leaderboard(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        // Cargar imágenes
        cargarImagenes();

        // Crear panel principal
        JLayeredPane panelPrincipal = new JLayeredPane();
        panelPrincipal.setLayout(null);

        // Crear panel de fondo
        crearPanelFondo(panelPrincipal);

        // Crear panel oscurecido
        crearPanelObscurecido(panelPrincipal);

        // Crear panel de contenido
        crearPanelContenido(panelPrincipal);

        // Ajustar tamaño y posición antes de mostrar el panel
        ajustarTamanoPaneles();
        actualizarPosicionesComponentes();

        // Listener para redimensionar ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarTamanoPaneles();
                actualizarPosicionesComponentes();
            }
        });

        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void cargarImagenes() {
        fondo = Imagen.cargarImagen("/images/GUI/background.png");
        botonMediano = Imagen.cargarImagen("/images/GUI/ButtonMedium.png");
        botonMedianoResaltado = Imagen.cargarImagen("/images/GUI/buttonMediumHighlighted.png");
    }

    private void crearPanelFondo(JLayeredPane panelPrincipal) {
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setLayout(null);
        panelFondo.setBounds(0, 0, getWidth(), getHeight());
        panelPrincipal.add(panelFondo, Integer.valueOf(0));
    }

    private void crearPanelObscurecido(JLayeredPane panelPrincipal) {
        panelObscurecido = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150)); // Color negro con transparencia
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelObscurecido.setOpaque(false);
        panelObscurecido.setBounds(0, 0, getWidth(), getHeight());
        panelPrincipal.add(panelObscurecido, Integer.valueOf(1));
    }

    private void crearPanelContenido(JLayeredPane panelPrincipal) {
        panelContenido = new JPanel();
        panelContenido.setOpaque(false);
        panelContenido.setLayout(null);

        // Crear y añadir los componentes
        crearListaLeaderboard();
        crearBotones();

        panelContenido.add(scrollLista);
        panelContenido.add(botonRegresar);

        panelPrincipal.add(panelContenido, Integer.valueOf(2));
    }

    private void crearListaLeaderboard() {
        modeloLista = new DefaultListModel<>();
        JList<String> listaLeaderboard = new JList<>(modeloLista);
        listaLeaderboard.setFont(new Font("Minecraft", Font.PLAIN, 16));
        listaLeaderboard.setOpaque(false); // Fondo transparente
        listaLeaderboard.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setOpaque(false); // Fondo transparente
                renderer.setForeground(Color.WHITE); // Letras en blanco
                if (isSelected) {
                    renderer.setBackground(new Color(255, 255, 255, 50)); // Fondo semi-transparente para selección
                    renderer.setOpaque(true); // Hacer el fondo visible solo al seleccionar
                }
                return renderer;
            }
        });

        scrollLista = new JScrollPane(listaLeaderboard);
        scrollLista.setOpaque(false);
        scrollLista.getViewport().setOpaque(false);
    }

    private void crearBotones() {
        botonRegresar = Boton.crearBoton(
                botonMediano,
                botonMedianoResaltado,
                197, 40, "Regresar", 16f
        );

        botonRegresar.addActionListener(e -> cardLayout.show(mainPanel, "menuRecicland"));
    }

    private void ajustarTamanoPaneles() {
        panelFondo.setSize(getWidth(), getHeight());
        panelObscurecido.setSize(getWidth(), getHeight());
        panelContenido.setSize(getWidth(), getHeight());

        if (scrollLista != null) {
            scrollLista.setBounds(50, 50, getWidth() - 100, getHeight() - 200);
        }
    }

    private void actualizarPosicionesComponentes() {
        int anchoVentana = getWidth();
        int altoVentana = getHeight();

        // Posición del botón "Regresar"
        botonRegresar.setLocation(
                (anchoVentana - botonRegresar.getWidth()) / 2,
                altoVentana - botonRegresar.getHeight() - 40
        );
    }

    public static void actualizarTop10() {
        modeloLista.clear();

        // Obtener jugadores y puntajes
        File[] archivos = new File(".").listFiles((dir, name) -> name.endsWith(".dat"));
        if (archivos == null || archivos.length == 0) {
            modeloLista.addElement("No hay registros disponibles");
            return;
        }

        Map<String, Integer> puntajes = new HashMap<>();
        for (File archivo : archivos) {
            String nombreJugador = archivo.getName().replace(".dat", "");
            int record = 0;

            if (archivo.exists() && archivo.length() > 0) {
                try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
                    record = dis.readInt();
                } catch (IOException e) {
                    System.err.println("Archivo corrupto o vacío para el jugador " + nombreJugador + ". Se omitirá este registro.");
                    continue;
                }
                puntajes.put(nombreJugador, record);
            }
        }

        puntajes.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> modeloLista.addElement(entry.getKey() + " - " + entry.getValue() + " puntos"));
    }
}