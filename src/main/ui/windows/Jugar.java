package main.ui.windows;

import main.Recicland;
import main.models.Jugador;
import main.ui.components.Boton;
import main.utils.Imagen;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Jugar extends JPanel {
    private Image fondo;
    private Image botonGrande, botonGrandeResaltado, botonGrandeDeshabilitado;
    private Image botonMediano, botonMedianoResaltado;
    private Image botonCampoTexto, botonCampoTextoResaltado;
    private Image fondoScroll, scroll;

    // Botones y componentes personalizados
    private JLayeredPane campoNombreJugador;
    private JButton botonAgregarJugador, botonEliminarJugador;
    private JButton botonJugar, botonCancelar;
    private JButton informacion, nombre;

    // Modelo y lista de jugadores
    private DefaultListModel<String> modeloListaJugadores;
    private JList<String> listaJugadores;
    private JScrollPane scrollPaneLista;

    // Paneles
    private JPanel panelFondo;
    private JPanel panelObscurecido;
    private JPanel panelBotones;

    // Variables para el CardLayout y el panel principal
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static List<Jugador> jugadores = new ArrayList<>();

    public Jugar(CardLayout cardLayout, JPanel mainPanel) {
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
        botonGrande = Imagen.cargarImagen("/images/GUI/ButtonLarge.png");
        botonGrandeResaltado = Imagen.cargarImagen("/images/GUI/buttonLargeHighlighted.png");
        botonGrandeDeshabilitado = Imagen.cargarImagen("/images/GUI/buttonLargeDisabled.png");
        botonMediano = Imagen.cargarImagen("/images/GUI/ButtonMedium.png");
        botonMedianoResaltado = Imagen.cargarImagen("/images/GUI/buttonMediumHighlighted.png");
        botonCampoTexto = Imagen.cargarImagen("/images/GUI/textField.png");
        botonCampoTextoResaltado = Imagen.cargarImagen("/images/GUI/textFieldHighlighted.png");
        fondoScroll = Imagen.cargarImagen("/images/GUI/scrollerBackground.png");
        scroll = Imagen.cargarImagen("/images/GUI/scroller.png");
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

        // Inicializar modelo de lista de jugadores
        modeloListaJugadores = new DefaultListModel<>();

        // Crear botones
        crearBotones();

        // Añadir botones al panel
        panelBotones.add(informacion);
        informacion.setEnabled(false);
        panelBotones.add(nombre);
        nombre.setEnabled(false);
        panelBotones.add(campoNombreJugador);
        panelBotones.add(botonAgregarJugador);
        panelBotones.add(botonEliminarJugador);
        panelBotones.add(botonJugar);
        panelBotones.add(botonCancelar);

        // Crear lista de jugadores con scroll
        crearListaJugadores();

        panelBotones.add(scrollPaneLista);

        panelPrincipal.add(panelBotones, Integer.valueOf(2));
    }

    private void crearBotones() {
        informacion = Boton.crearBotonTexto("Información del Jugador", false, 16f);
        nombre = Boton.crearBotonTexto("Nombre", false, 16f);
        campoNombreJugador = Boton.crearCampoTexto(botonCampoTexto, botonCampoTextoResaltado, 404, 40, 16f);
        botonAgregarJugador = Boton.crearBoton(botonGrande, botonGrandeResaltado, botonGrandeDeshabilitado, 404, 40, "Agregar nuevo jugador", 16f);
        botonEliminarJugador = Boton.crearBoton(botonGrande, botonGrandeResaltado, botonGrandeDeshabilitado, 404, 40, "Eliminar jugador", 16f);
        botonJugar = Boton.crearBoton(botonMediano, botonMedianoResaltado, botonGrandeDeshabilitado, 197, 40, "Jugar", 16f);
        botonCancelar = Boton.crearBoton(botonMediano, botonMedianoResaltado, 197, 40, "Cancelar", 16f);
        botonEliminarJugador.setEnabled(false);
        botonAgregarJugador.setEnabled(false);
        botonJugar.setEnabled(false);

        // Acción para botón "Agregar nuevo jugador"
        botonAgregarJugador.addActionListener(e -> agregarJugador());

        // Acción para botón "Eliminar jugador"
        botonEliminarJugador.addActionListener(e -> eliminarJugador());

        // Acción para el botón "Jugar"
        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agregar jugadores a lista statica
                jugadores.clear();
                for (int i = 0; i < modeloListaJugadores.getSize(); i++) {
                    String nombreJugador = modeloListaJugadores.getElementAt(i);
                    jugadores.add(new Jugador(nombreJugador, 5));
                }

                // Crear instancia de MainGame
                MainGame ventanaJuego = new MainGame(cardLayout, mainPanel);

                // Agregar un listener para cambiar la música según el panel activo
                ventanaJuego.addComponentListener(new java.awt.event.ComponentAdapter() {
                    @Override
                    public void componentShown(java.awt.event.ComponentEvent evt) {
                        // Detener la música del menú al entrar en MainGame
                        Recicland.detenerMusicaMenu();
                    }

                    @Override
                    public void componentHidden(java.awt.event.ComponentEvent evt) {
                        // Reiniciar la música del menú al salir de MainGame
                        Recicland.reproducirMusicaMenu();
                    }
                });

                // Agregar ventanaJuego al mainPanel
                mainPanel.add(ventanaJuego, "ventanaJuego");

                // Cambiar al panel de ventanaJuego
                cardLayout.show(mainPanel, "ventanaJuego");
            }
        });

        // Acción para el botón "Cancelar"
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar al panel del menú principal
                cardLayout.show(mainPanel, "menuRecicland");
            }
        });

        // Añadir DocumentListener al campo de texto para habilitar/deshabilitar el botón "Agregar nuevo jugador"
        JTextField campoTexto = obtenerCampoTexto(campoNombreJugador);
        if (campoTexto != null) {
            campoTexto.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    actualizarEstadoBotonAgregar(campoTexto);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    actualizarEstadoBotonAgregar(campoTexto);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    actualizarEstadoBotonAgregar(campoTexto);
                }
            });
        }
    }

    private void crearListaJugadores() {
        listaJugadores = new JList<>(modeloListaJugadores);
        listaJugadores.setOpaque(false);
        listaJugadores.setFont(new Font("minecraft", Font.PLAIN, 20));
        listaJugadores.setFixedCellHeight(40);
        listaJugadores.setFixedCellWidth(600);

        // Configuración del renderizador personalizado para la lista de jugadores
        listaJugadores.setCellRenderer(new DefaultListCellRenderer() {
            private final Image fondoResaltado = Imagen.cargarImagen("/images/GUI/textFieldHighlighted.png");

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                label.setFont(new Font("minecraft", Font.PLAIN, 16));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setForeground(Color.WHITE);

                if (isSelected) {
                    label.setOpaque(false);
                    ImageIcon iconoEscalado = new ImageIcon(fondoResaltado.getScaledInstance(600, 40, Image.SCALE_SMOOTH));
                    label.setIcon(iconoEscalado);
                    label.setHorizontalTextPosition(SwingConstants.CENTER);
                    label.setBackground(null);
                    label.setBorder(null);
                } else {
                    label.setOpaque(false);
                    label.setIcon(null);
                    label.setBackground(null);
                    label.setBorder(null);
                }

                label.setPreferredSize(new Dimension(600, 40));

                return label;
            }
        });

        // Listener para habilitar/deshabilitar el botón "Eliminar jugador"
        listaJugadores.addListSelectionListener(e -> botonEliminarJugador.setEnabled(!listaJugadores.isSelectionEmpty()));

        // Listener para actualizar el estado del botón "Jugar"
        modeloListaJugadores.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                actualizarEstadoBotonJugar();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                actualizarEstadoBotonJugar();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                actualizarEstadoBotonJugar();
            }
        });

        // Configurar el scrollPane
        scrollPaneLista = new JScrollPane(listaJugadores);
        scrollPaneLista.setOpaque(false);
        scrollPaneLista.getViewport().setOpaque(false);

        // Estilo personalizado de scroll
        scrollPaneLista.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(scroll, thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, null);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(fondoScroll, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, null);
                g2.dispose();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                Dimension ceroDim = new Dimension(0, 0);
                button.setPreferredSize(ceroDim);
                button.setMinimumSize(ceroDim);
                button.setMaximumSize(ceroDim);
                return button;
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

        // Posición de "Informacion del Jugador"
        informacion.setLocation((anchoVentana - informacion.getWidth()) / 2, 60);

        // Posición de "Nombre"
        nombre.setLocation((anchoVentana - campoNombreJugador.getWidth()) / 2, informacion.getY() + informacion.getHeight() + 60);

        // Posición del campo de texto para el nombre
        campoNombreJugador.setLocation((anchoVentana - campoNombreJugador.getWidth()) / 2, nombre.getY() + nombre.getHeight() + 10);

        // Posición del botón "Agregar nuevo jugador"
        botonAgregarJugador.setLocation((anchoVentana - botonAgregarJugador.getWidth()) / 2, campoNombreJugador.getY() + campoNombreJugador.getHeight() + 10);

        // Posición del `scrollPaneLista`
        int alturaLista = altoVentana - 500;
        scrollPaneLista.setBounds((anchoVentana - scrollPaneLista.getWidth()) / 2, botonAgregarJugador.getY() + botonAgregarJugador.getHeight() + 40, 620, alturaLista);

        // Posición del botón "Eliminar jugador"
        botonEliminarJugador.setLocation((anchoVentana - botonEliminarJugador.getWidth()) / 2, scrollPaneLista.getY() + scrollPaneLista.getHeight() + 10);

        // Posición de los botones "Cancelar" y "Jugar"
        botonCancelar.setLocation((anchoVentana / 2) - botonCancelar.getWidth() - 5, altoVentana - botonCancelar.getHeight() - 60);
        botonJugar.setLocation((anchoVentana / 2) + 5, altoVentana - botonJugar.getHeight() - 60);
    }

    private JTextField obtenerCampoTexto(JLayeredPane campoTextoPane) {
        for (Component component : campoTextoPane.getComponents()) {
            if (component instanceof JTextField) {
                return (JTextField) component;
            }
        }
        return null;
    }

    private void actualizarEstadoBotonAgregar(JTextField campoTexto) {
        String nombreJugador = campoTexto.getText().trim();
        boolean nombreValido = !nombreJugador.isEmpty() && !existeJugador(nombreJugador);
        botonAgregarJugador.setEnabled(nombreValido);
    }

    private void actualizarEstadoBotonJugar() {
        botonJugar.setEnabled(modeloListaJugadores.getSize() > 0);
    }

    private void agregarJugador() {
        JTextField campoTexto = obtenerCampoTexto(campoNombreJugador);
        if (campoTexto != null) {
            String nombreJugador = campoTexto.getText().trim();
            if (!nombreJugador.isEmpty()) {
                modeloListaJugadores.addElement(nombreJugador);
                campoTexto.setText("");
            }
        }
    }

    private boolean existeJugador(String nombreJugador) {
        for (int i = 0; i < modeloListaJugadores.getSize(); i++) {
            String nombreExistente = modeloListaJugadores.getElementAt(i);
            if (nombreExistente.equalsIgnoreCase(nombreJugador)) {
                return true;
            }
        }
        return false;
    }

    private void eliminarJugador() {
        int indiceSeleccionado = listaJugadores.getSelectedIndex();
        if (indiceSeleccionado != -1) {
            modeloListaJugadores.remove(indiceSeleccionado);
        }
    }
}