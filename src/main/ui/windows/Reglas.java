package main.ui.windows;

import main.ui.components.Boton;
import main.utils.Imagen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Reglas extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Image fondo;
    private Image botonMediano, botonMedianoResaltado;

    // Paneles
    private JPanel panelFondo;
    private JPanel panelObscurecido;
    private JPanel panelContenido;

    public Reglas(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        cargarImagenes();

        // Crear panel principal con capas
        JLayeredPane panelPrincipal = new JLayeredPane();
        panelPrincipal.setLayout(null);

        // Crear panel de fondo
        crearPanelFondo(panelPrincipal);

        // Crear panel oscurecido
        crearPanelObscurecido(panelPrincipal);

        // Crear panel de contenido
        crearPanelContenido(panelPrincipal);

        // Ajustar tamaño y posición de los paneles
        ajustarTamanoPaneles();
        actualizarPosicionesComponentes();

        // Listener para redimensionar la ventana
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
                g.setColor(new Color(0, 0, 0, 150)); // Fondo negro semi-transparente
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
        panelContenido.setLayout(new BorderLayout());
        panelContenido.setBounds(50, 50, getWidth() - 100, getHeight() - 100);

        // Texto con las reglas
        JLabel textoReglas = new JLabel(
                "<html><div style='text-align: center;'>"
                        + "Tu objetivo es clasificar y tratar correctamente <br>"
                        + "distintos desechos que irán apareciendo en tu camino.<br>"
                        + "Si los clasificas y tratas correctamente, se te sumarán los <br>"
                        + "puntos correspondientes, sino perderás vidas.<br>"
                        + "Recuerda que solo tienes 5 vidas, ¡trátalas con cuidado!<br><br>"
                        + "Los puntos obtenidos y las vidas perdidas dependen del nivel:<br>"
                        + "Nivel 1 | Puntos: +10 | Vidas: -1<br>"
                        + "Nivel 2 | Puntos: +15 | Vidas: -1<br>"
                        + "Nivel 3 | Puntos: +20 | Vidas: -2<br><br>"
                        + "¡Recuerda que solo tenemos un planeta, cuídalo!"
                        + "</div></html>"
        );
        textoReglas.setFont(new Font("Minecraft", Font.PLAIN, 16));
        textoReglas.setForeground(Color.WHITE);
        textoReglas.setHorizontalAlignment(SwingConstants.CENTER);

        // Botón para regresar al menú principal
        JButton botonRegresar = Boton.crearBoton(
                botonMediano,
                botonMedianoResaltado,
                197, 40, "Regresar", 16f
        );
        botonRegresar.addActionListener(e -> cardLayout.show(mainPanel, "menuRecicland"));

        // Panel para centrar el contenido
        JPanel panelCentrado = new JPanel();
        panelCentrado.setOpaque(false);
        panelCentrado.setLayout(new GridBagLayout());
        panelCentrado.add(textoReglas);

        // Panel para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(botonRegresar);

        // Añadir componentes al panel de contenido
        panelContenido.add(panelCentrado, BorderLayout.CENTER);
        panelContenido.add(panelBoton, BorderLayout.SOUTH);

        panelPrincipal.add(panelContenido, Integer.valueOf(2));
    }

    private void ajustarTamanoPaneles() {
        panelFondo.setSize(getWidth(), getHeight());
        panelObscurecido.setSize(getWidth(), getHeight());
        panelContenido.setBounds(50, 50, getWidth() - 100, getHeight() - 100);
    }

    private void actualizarPosicionesComponentes() {
        // Aquí puedes ajustar posiciones si es necesario
    }
}