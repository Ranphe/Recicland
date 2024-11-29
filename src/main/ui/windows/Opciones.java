package main.ui.windows;

import main.ui.components.Boton;
import main.utils.Imagen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Opciones extends JPanel {
    private Image fondo;
    private Image botonMediano, botonMedianoResaltado;

    // Botones y componentes personalizados
    private JButton botonRegresar;

    // Paneles
    private JPanel panelFondo;
    private JPanel panelObscurecido;
    private JPanel panelBotones;

    // Variables para el CardLayout y el panel principal
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Opciones(CardLayout cardLayout, JPanel mainPanel) {
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

        // Crear botones y lista
        crearPanelBotones(panelPrincipal);

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

    private void crearPanelBotones(JLayeredPane panelPrincipal) {
        panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(null);

        // Crear botones
        crearBotones();

        // Añadir botones al panel
        panelBotones.add(botonRegresar);

        panelPrincipal.add(panelBotones, Integer.valueOf(2));
    }

    private void crearBotones() {
        botonRegresar = Boton.crearBoton(botonMediano, botonMedianoResaltado, 197, 40, "Regresar", 16f);

        // Acción para el botón "Cancelar"
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar al panel del menú principal
                cardLayout.show(mainPanel, "menuRecicland");
            }
        });
    }

    private void ajustarTamanoPaneles() {
        panelFondo.setSize(getWidth(), getHeight());
        panelObscurecido.setSize(getWidth(), getHeight());
        panelBotones.setSize(getWidth(), getHeight());
    }

    private void actualizarPosicionesComponentes() {
        int anchoVentana = getWidth();
        int altoVentana = getHeight();

        // Posición de los botones "Cancelar""
        botonRegresar.setLocation((anchoVentana - botonRegresar.getWidth()) / 2, altoVentana - botonRegresar.getHeight() - 40);
    }
}