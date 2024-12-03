package main.ui.windows;

import main.ui.components.Boton;
import main.utils.Imagen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

public class Accesibilidad extends JPanel {
    // Variables para el CardLayout y el panel principal
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private  JScrollPane scrollLista;

    private static DefaultListModel<String> modeloLista;
    private JList<String> listaJugadores;

    private Image fondo;
    private Image botonMediano, botonMedianoResaltado;

    // Botones y componentes personalizados
    private JButton botonRegresar;

    // Paneles
    private JPanel panelFondo;
    private JPanel panelObscurecido;
    private JPanel panelBotones;



    public Accesibilidad(CardLayout cardLayout, JPanel mainPanel) {
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

        // Modelo y lista de jugadores
        modeloLista = new DefaultListModel<>();
        listaJugadores = new JList<>(modeloLista);
        listaJugadores.setFont(new Font("Minecraft", Font.PLAIN, 16));
        listaJugadores.setOpaque(false); // Fondo transparente
        listaJugadores.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setOpaque(false); // Fondo transparente
                renderer.setForeground(Color.WHITE); // Texto en blanco
                if (isSelected) {
                    renderer.setBackground(new Color(255, 255, 255, 50)); // Fondo semi-transparente
                }
                return renderer;
            }
        });


        listaJugadores.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String jugadorSeleccionado = listaJugadores.getSelectedValue();
                if (jugadorSeleccionado != null && !jugadorSeleccionado.equals("No hay registro de jugadores")) {
                    mostrarRegistroJugador(jugadorSeleccionado + ".dat");
                }
            }
        });


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

        scrollLista = new JScrollPane(listaJugadores);
        scrollLista.setOpaque(false);
        scrollLista.getViewport().setOpaque(false);
        //panelPrincipal.add(scrollLista, BorderLayout.CE   NTER);
        panelPrincipal.add(scrollLista, Integer.valueOf(2)); // Asegurar que este al frente
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
        // Ajustar el tamaño del JScrollPane
        if (scrollLista != null) { // Asegurar que no es nulo
            scrollLista.setBounds(50, 50, getWidth() - 100, getHeight() - 200);
        }


    }

    private void actualizarPosicionesComponentes() {
        int anchoVentana = getWidth();
        int altoVentana = getHeight();

        // Posición de los botones "Cancelar""
        botonRegresar.setLocation((anchoVentana - botonRegresar.getWidth()) / 2, altoVentana - botonRegresar.getHeight() - 40);
    }

    // Metodo para actualizar la lista de jugadores
    public static void actualizarListaJugadores() {
        modeloLista.clear(); // Limpiar la lista actual
        File[] archivos = new File(".").listFiles((dir, name) -> name.endsWith(".dat"));
        if (archivos != null && archivos.length > 0) {
            Arrays.stream(archivos)
                    .map(archivo -> archivo.getName().replace(".dat", "")) // Eliminar extensión
                    .forEach(modeloLista::addElement);
        } else {
            modeloLista.addElement("No hay registro de jugadores");
        }
    }

    private void mostrarRegistroJugador(String archivoJugador) {
        // Crear imágenes y el panel principal
        cargarImagenes();
        JLayeredPane panelPrincipal = new JLayeredPane();
        panelPrincipal.setLayout(null);

        // Fondo
        JPanel panelFondo = new JPanel() {
            private Image fondo = Imagen.cargarImagen("/images/GUI/background.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setBounds(0, 0, getWidth(), getHeight());
        panelPrincipal.add(panelFondo, Integer.valueOf(0));

        // Capa oscurecida
        JPanel panelObscurecido = new JPanel() {
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

        // Contenido del registro
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BorderLayout());
        panelContenido.setOpaque(false);
        panelContenido.setBounds(50, 50, getWidth() - 100, getHeight() - 100);
        panelPrincipal.add(panelContenido, Integer.valueOf(2));

        JLabel tituloRegistro = new JLabel("Registro de Jugador: " + archivoJugador.replace(".dat", ""));
        tituloRegistro.setFont(new Font("Minecraft", Font.PLAIN, 20));
        tituloRegistro.setForeground(Color.WHITE); // Texto en blanco
        tituloRegistro.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Minecraft", Font.PLAIN, 14));
        areaTexto.setForeground(Color.WHITE); // Texto en blanco
        areaTexto.setOpaque(false); // Fondo transparente

        StringBuilder contenido = new StringBuilder();
        File archivo = new File(archivoJugador);
        if (archivo.exists() && archivo.length() > 0) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
                int record = dis.readInt();
                contenido.append("Record: ").append(record).append("\n\n");
                int numEventos = dis.readInt();
                for (int i = 0; i < numEventos; i++) {
                    long fechaLong = dis.readLong();
                    String mensaje = dis.readUTF();
                    Date fecha = new Date(fechaLong);
                    contenido.append("Fecha: ").append(fecha.toString()).append("\n");
                    contenido.append("Evento: ").append(mensaje).append("\n\n");
                }
                areaTexto.setText(contenido.toString());
            } catch (IOException e) {
                e.printStackTrace();
                areaTexto.setText("Error al cargar el registro del jugador.");
            }
        } else {
            areaTexto.setText("No hay registros disponibles para este jugador.");
        }

        JScrollPane scrollRegistro = new JScrollPane(areaTexto);
        scrollRegistro.setOpaque(false);
        scrollRegistro.getViewport().setOpaque(false);

        JButton botonRegresarLista = Boton.crearBoton(
                Imagen.cargarImagen("/images/GUI/ButtonMedium.png"),
                Imagen.cargarImagen("/images/GUI/buttonMediumHighlighted.png"),
                197, 40, "Regresar", 16f
        );
        botonRegresarLista.addActionListener(e -> cardLayout.show(mainPanel, "accesibilidad"));

        panelContenido.add(tituloRegistro, BorderLayout.NORTH);
        panelContenido.add(scrollRegistro, BorderLayout.CENTER);
        panelContenido.add(botonRegresarLista, BorderLayout.SOUTH);

        mainPanel.add(panelPrincipal, "ventanaRegistroJugador");
        cardLayout.show(mainPanel, "ventanaRegistroJugador");
    }
}