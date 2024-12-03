package main.ui.windows;

import main.ui.components.Boton;
import main.utils.Imagen;
import main.utils.ImagenAnimada;
import main.utils.TextoAnimado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class MenuRecicland extends JPanel {
    private Image fondo;
    private Image recicland, edition;
    private Image botonGrande, botonGrandeResaltado;
    private Image botonMediano, botonMedianoResaltado;
    private Image botonPequeno, botonPequenoResaltado;
    private Image iconoNotificacionNoVisto, iconoNoticias;
    private Image iconoIdioma, iconoAccesibilidad;
    private ImagenAnimada iconoPruebaDisponibleAnimado;
    private JButton botonJugar, botonReglas;
    private JLayeredPane botonLeaderboard;
    private JButton botonOpciones, botonCerrar;
    private JLayeredPane botonIdioma, botonAccesibilidad;
    private JButton informacion1, informacion2;
    private TextoAnimado textoAnimado;

    // Paneles
    private JPanel panelFondo;
    private JPanel panelImagenes;
    private JPanel panelBotones;

    // Variables de posición y tamaño para la imagen de Recicland
    private int posXImagenRecicland, posYImagenRecicland, anchoImagenRecicland, altoImagenRecicland;
    private int posXImagenEdition, posYImagenEdition, anchoImagenEdition, altoImagenEdition;

    // Variables para el CardLayout y el panel principal
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MenuRecicland(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        // Cargar imágenes
        cargarImagenes();

        // Crear panel principal
        JLayeredPane panelPrincipal = new JLayeredPane();
        panelPrincipal.setLayout(null);

        // Crear panel de imagen de fondo
        crearPanelFondo(panelPrincipal);

        // Crear panel para las imágenes sobre el fondo
        crearPanelImagenes(panelPrincipal);

        // Agregar botones al panel
        crearPanelBotones(panelPrincipal);

        // Listener para redimensionar ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarTamanoPanelFondo();
                ajustarTamanoPanelImagenes();
                ajustarTamanoPanelBotones();
                actualizarPosicionImagenes();
                actualizarPosicionesBotones();
            }
        });

        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void cargarImagenes() {
        fondo = Imagen.cargarImagen("/images/GUI/background.png");
        recicland = Imagen.cargarImagen("/images/GUI/recicland.png");
        edition = Imagen.cargarImagen("/images/GUI/edition.png");
        botonGrande = Imagen.cargarImagen("/images/GUI/ButtonLarge.png");
        botonGrandeResaltado = Imagen.cargarImagen("/images/GUI/buttonLargeHighlighted.png");
        botonMediano = Imagen.cargarImagen("/images/GUI/ButtonMedium.png");
        botonMedianoResaltado = Imagen.cargarImagen("/images/GUI/buttonMediumHighlighted.png");
        botonPequeno = Imagen.cargarImagen("/images/GUI/ButtonSmall.png");
        botonPequenoResaltado = Imagen.cargarImagen("/images/GUI/buttonSmallHighlighted.png");
        iconoIdioma = Imagen.cargarImagen("/images/GUI/language.png");
        iconoAccesibilidad = Imagen.cargarImagen("/images/GUI/accessibility.png");
        iconoNoticias = Imagen.cargarImagen("/images/GUI/news.png");
        iconoNotificacionNoVisto = Imagen.cargarImagen("/images/GUI/unseenNotification.png");

        int[] tiemposFrames = {5, 1, 1, 1, 1, 1}; // Tiempos en décimas de segundo
        iconoPruebaDisponibleAnimado = new ImagenAnimada("/images/GUI/trialAvailable.png", 6, tiemposFrames);
    }

    private void crearPanelFondo(JLayeredPane panelPrincipal) {
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Configurar el tamaño y layout del panelFondo
        panelFondo.setLayout(null); // Layout nulo para que actúe solo como fondo

        // Añadir el panelFondo a la capa más baja del panel principal
        panelPrincipal.add(panelFondo, Integer.valueOf(0));
    }

    private void crearPanelImagenes(JLayeredPane panelPrincipal) {
        panelImagenes = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar la imagen de Recicland en la posición calculada
                g.drawImage(recicland, posXImagenRecicland, posYImagenRecicland, anchoImagenRecicland, altoImagenRecicland, this);
                g.drawImage(edition, posXImagenEdition, posYImagenEdition, anchoImagenEdition, altoImagenEdition, this);
            }
        };
        panelImagenes.setOpaque(false);
        panelPrincipal.add(panelImagenes, Integer.valueOf(1)); // Capa sobre el fondo
    }

    private void crearBotones() {
        botonJugar = Boton.crearBoton(botonGrande, botonGrandeResaltado, 404, 40, "Jugar", 16f);
        botonReglas = Boton.crearBoton(botonGrande, botonGrandeResaltado, 404, 40, "Reglas", 16f);
        botonLeaderboard = Boton.crearBotonConIcono(botonGrande, botonGrandeResaltado, 404, 40, "Leaderboard", 1, 16, 8, 8, 16f, iconoNotificacionNoVisto, iconoNoticias, iconoPruebaDisponibleAnimado);
        botonOpciones = Boton.crearBoton(botonMediano, botonMedianoResaltado, 197, 40, "Opciones...", 16f);
        botonCerrar = Boton.crearBoton(botonMediano, botonMedianoResaltado, 197, 40, "Cerrar Recicland", 16f);
        botonIdioma = Boton.crearBotonConIcono(botonPequeno, botonPequenoResaltado, 40, 40, "", 0, 30, 0, 0, 16f, iconoIdioma);
        botonAccesibilidad = Boton.crearBotonConIcono(botonPequeno, botonPequenoResaltado, 40, 40, "", 0, 30, 0, 0, 16f, iconoAccesibilidad);
        informacion1 = Boton.crearBotonTexto("Recicland 0.80.0", false, 16f);
        informacion2 = Boton.crearBotonTexto("2024 ¡Gratuito y de código abierto!", true, 16f);
        textoAnimado = new TextoAnimado("¡Cuida nuestro planeta!");

        // Acción para el botón "Jugar"
        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar al panel de Jugar
                cardLayout.show(mainPanel, "jugar");
            }
        });

        // Acción para el botón "Reglas"
        botonReglas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "reglas");
            }
        });

        // Agregar ActionListener al botón "Leaderboard"
        JButton innerBotonLeaderboard = getBotonInterno(botonLeaderboard);
        innerBotonLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Leaderboard.actualizarTop10();
                cardLayout.show(mainPanel, "leaderboard");
            }
        });

        // Agregar ActionListener al botón "Idioma"
        JButton innerBotonIdioma = getBotonInterno(botonIdioma);
        innerBotonIdioma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "idioma");
            }
        });

        // Agregar ActionListener al botón "Opciones..."
        botonOpciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "opciones");
            }
        });

        // Agregar ActionListener al botón "Accesibilidad"
        JButton innerBotonAccesibilidad = getBotonInterno(botonAccesibilidad);
        innerBotonAccesibilidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accesibilidad.actualizarListaJugadores();
                cardLayout.show(mainPanel, "accesibilidad");
            }
        });

        // Acción para el botón "Cerrar Recicland"
        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la aplicación
                System.exit(0);
            }
        });

        // Acción para el botón "informacion2"
        informacion2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Verifica si Desktop está soportado en el sistema y si puede manejar URIs
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://github.com/Ranphe/Recicland"));
                    } else {
                        JOptionPane.showMessageDialog(null, "No se puede abrir el enlace en el navegador.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al intentar abrir el enlace.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void crearPanelBotones(JLayeredPane panelPrincipal) {
        panelBotones = new JPanel();
        panelBotones.setOpaque(false); // Para que sea transparente
        panelBotones.setLayout(null); // Establecer layout nulo para posicionamiento manual

        crearBotones();

        // Añadir los botones al panel
        panelBotones.add(botonJugar);
        panelBotones.add(botonReglas);
        panelBotones.add(botonLeaderboard);
        panelBotones.add(botonOpciones);
        panelBotones.add(botonCerrar);
        panelBotones.add(botonIdioma);
        panelBotones.add(botonAccesibilidad);
        panelBotones.add(informacion1);
        informacion1.setEnabled(false);
        panelBotones.add(informacion2);
        panelBotones.add(textoAnimado);

        panelPrincipal.add(panelBotones, Integer.valueOf(2)); // Añadir los botones en una capa superior
    }

    private void ajustarTamanoPanelFondo() {
        panelFondo.setSize(getWidth(), getHeight());
    }

    private void ajustarTamanoPanelImagenes() {
        panelImagenes.setSize(getWidth(), getHeight());
    }

    private void ajustarTamanoPanelBotones() {
        panelBotones.setSize(getWidth(), getHeight());
    }

    private void actualizarPosicionImagenes() {
        int anchoVentana = getWidth();

        // Tamaños para las imágenes
        anchoImagenRecicland = 512;
        altoImagenRecicland = 128;
        anchoImagenEdition = 256;
        altoImagenEdition = 32;

        // Calcular posición centrada para Recicland en la parte superior
        posXImagenRecicland = ((anchoVentana - anchoImagenRecicland) / 2);
        posYImagenRecicland = 40;  // A 40 píxeles desde la parte superior

        // Posición de Edition justo debajo de Recicland
        posXImagenEdition = ((anchoVentana - anchoImagenEdition) / 2);
        posYImagenEdition = posYImagenRecicland + altoImagenRecicland - 35;

        // Posición de Texto Animado justo en la esquina inferior derecha de Recicland
        textoAnimado.setLocation(posXImagenRecicland + 375, posYImagenRecicland - 25);
    }

    private void actualizarPosicionesBotones() {
        // Obtener dimensiones de la ventana
        int anchoVentana = getWidth();
        int altoVentana = getHeight();

        // Espaciados
        final int ESPACIADO_PEQUENO = 10;
        final int ESPACIADO_GRANDE = 40;

        // Dimensiones de los botones
        final int ANCHO_BOTON_GRANDE = 404;
        final int ALTO_BOTON_GRANDE = 40;
        final int ANCHO_BOTON_MEDIANO = 197;
        final int TAMANO_BOTON_PEQUENO = 40;

        // Calcular posición central en X para los botones grandes
        int centroX = ((anchoVentana - ANCHO_BOTON_GRANDE) / 2);

        // Calcular posición central en Y para el botón "Leaderboard"
        int centroY = Math.max((altoVentana - ALTO_BOTON_GRANDE) / 2, 315);

        // Posicionar el botón "Leaderboard" en el centro de la pantalla
        botonLeaderboard.setLocation(centroX, centroY);

        // Posicionar los demás botones grandes
        botonReglas.setLocation(centroX, centroY - ALTO_BOTON_GRANDE - ESPACIADO_PEQUENO);
        botonJugar.setLocation(centroX, centroY - 2 * (ALTO_BOTON_GRANDE + ESPACIADO_PEQUENO));

        // Calcular ancho total de los botones medianos y pequeños con espaciados
        int anchoTotalBotones = (ANCHO_BOTON_MEDIANO * 2) + (TAMANO_BOTON_PEQUENO * 2) + (ESPACIADO_PEQUENO * 3);

        // Calcular posición inicial en X para los botones medianos y pequeños
        int inicioXBotones = ((anchoVentana - anchoTotalBotones) / 2);

        // Posición en Y para los botones medianos y pequeños, debajo del botón "Leaderboard"
        int posicionYBotones = centroY + ALTO_BOTON_GRANDE + ESPACIADO_GRANDE;

        // Posicionar botones medianos y pequeños de izquierda a derecha
        int posicionXActual = inicioXBotones;

        // Botón Idioma (pequeño)
        botonIdioma.setLocation(posicionXActual, posicionYBotones);
        posicionXActual += TAMANO_BOTON_PEQUENO + ESPACIADO_PEQUENO;

        // Botón Opciones (mediano)
        botonOpciones.setLocation(posicionXActual, posicionYBotones);
        posicionXActual += ANCHO_BOTON_MEDIANO + ESPACIADO_PEQUENO;

        // Botón Cerrar (mediano)
        botonCerrar.setLocation(posicionXActual, posicionYBotones);
        posicionXActual += ANCHO_BOTON_MEDIANO + ESPACIADO_PEQUENO;

        // Botón Accesibilidad (pequeño)
        botonAccesibilidad.setLocation(posicionXActual, posicionYBotones);

        // Posición vertical para las etiquetas de información (desde la parte inferior)
        int posicionYInformacion = altoVentana - informacion1.getHeight();

        // Posicionar etiqueta de información 1 en la esquina inferior izquierda
        informacion1.setLocation(0, posicionYInformacion);

        // Calcular la posición para que esté pegado a la derecha
        informacion2.setLocation(anchoVentana - informacion2.getWidth(), posicionYInformacion);
    }

    // Metodo auxiliar para obtener el JButton interno de un JLayeredPane
    private JButton getBotonInterno(JLayeredPane pane) {
        for (Component c : pane.getComponents()) {
            if (c instanceof JButton) {
                return (JButton) c;
            }
        }
        return null;
    }
}