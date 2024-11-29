package main.ui.windows;

import main.levels.*;
import main.models.*;
import main.ui.components.Boton;
import main.utils.Imagen;
import main.utils.Sonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainGame extends JPanel {
    // Imágenes
    private Image imagenFondo;
    private Image botonGrande, botonGrandeResaltado, botonGrandeDeshabilitado;
    private Image imagenVida;

    // Botones individuales
    private JButton botonDesecho;
    private JButton contenedorResaltadoPorArrastre = null;
    private JButton informacionNivel, informacionTurno, informacionPuntos, informacionVida;
    private JButton informacionInstruccion;

    // Colecciones de botones
    private JButton[] botonesTratamiento;
    private JButton[] casillasTratamiento;
    private List<JButton> botonesContenedores;
    private List<String> nombresContenedores;

    // Variables booleanas
    private boolean[] casillasOcupadas;
    private boolean tiempoAgotado = false;

    // Posiciones y puntos
    private Point posicionInicialDesecho;
    private Point clicInicial;

    // Sonido
    private Sonido musicaJuego;

    // Temporizador
    private Timer cronometroTimer;
    private int tiempoRestante;
    private JLabel cronometroLabel;

    // Lógica del juego
    private List<Nivel> niveles;
    private int indiceNivelActual = 0;
    private Nivel nivelActual;
    private List<Jugador> jugadores;
    private int indiceJugadorActual = -1; // Iniciar en -1 para usar pasarAlSiguienteJugador()
    private Jugador jugadorActual;
    private int desechosClasificadosCorrectamente = 0;
    private List<Desecho> desechosNivelActual;
    private Desecho desechoActual;
    private List<Contenedor> contenedoresNivelActual;
    private List<String> pasosTratamientoDesordenados;
    private List<String> pasosTratamientoCorrectos;

    // Paneles
    private JPanel panelFondo;
    private JPanel panelBotones;

    // Variables para el CardLayout y el panel principal
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public MainGame(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        // Inicializar los niveles antes de configurar el panel
        niveles = new ArrayList<>();
        niveles.add(new Nivel1());
        niveles.add(new Nivel2());
        niveles.add(new Nivel3());
        nivelActual = niveles.get(indiceNivelActual);

        // Cargar música del juego
        musicaJuego = new Sonido("/sounds/gameMusic.wav");

        // Configurar el panel (inicializa los componentes gráficos)
        configurarPanel();

        // Asegurarnos de que los botones están posicionados y posicionInicialDesecho está inicializado
        ajustarTamanoPanelFondo();
        ajustarTamanoPanelBotones();
        actualizarPosicionesBotones();

        // Inicializar el juego (usa los componentes gráficos)
        inicializarJuego();

        // Listener para iniciar/detener la música del juego al mostrar u ocultar el panel
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                musicaJuego.reproducirEnBucle(); // Iniciar música al mostrar el panel
                iniciarCronometro(); // Iniciar el cronómetro
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                musicaJuego.detener(); // Detener música al ocultar el panel
                if (cronometroTimer != null) {
                    cronometroTimer.stop(); // Detener el cronómetro
                }
            }
        });
    }

    private void configurarPanel() {
        cargarImagenes();

        JLayeredPane panelCapaPrincipal = new JLayeredPane();
        panelCapaPrincipal.setLayout(null);

        crearPanelFondo(panelCapaPrincipal);
        crearPanelBotones(panelCapaPrincipal);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarTamanoPanelFondo();
                ajustarTamanoPanelBotones();
                actualizarPosicionesBotones();
            }
        });

        setLayout(new BorderLayout());
        add(panelCapaPrincipal, BorderLayout.CENTER);
    }

    private void cargarImagenes() {
        imagenFondo = Imagen.cargarImagen("/images/GUI/gameBackground.png");
        botonGrande = Imagen.cargarImagen("/images/GUI/ButtonLarge.png");
        botonGrandeResaltado = Imagen.cargarImagen("/images/GUI/buttonLargeHighlighted.png");
        botonGrandeDeshabilitado = Imagen.cargarImagen("/images/GUI/buttonLargeDisabled.png");
        imagenVida = Imagen.cargarImagen("/images/GUI/full.png");
    }

    private void cargarImagenesContenedores() {
        // Inicializar las listas
        botonesContenedores = new ArrayList<>();
        nombresContenedores = new ArrayList<>();

        // Obtener los contenedores únicos desde el repositorio
        List<Contenedor> todosLosContenedores = RepositorioDesechos.obtenerContenedores();

        // Crear botones para cada contenedor utilizando crearBotonImagen
        for (Contenedor contenedor : todosLosContenedores) {
            String nombreContenedor = normalizarNombre(contenedor.getNombre());

            // Cargar imágenes del contenedor
            Image imagenContenedor = Imagen.cargarImagen("/images/contenedores/" + nombreContenedor + ".png");
            Image imagenContenedorResaltado = Imagen.cargarImagen("/images/contenedores/" + nombreContenedor + "Highlighted.png");

            // Usar imagen genérica si no se encuentra la específica
            if (imagenContenedor == null || Imagen.esImagenPredeterminada(imagenContenedor)) {
                imagenContenedor = Imagen.cargarImagen("/images/contenedores/contenedorGenerico.png");
            }
            if (imagenContenedorResaltado == null || Imagen.esImagenPredeterminada(imagenContenedorResaltado)) {
                imagenContenedorResaltado = Imagen.cargarImagen("/images/contenedores/contenedorGenerico.png");
            }

            // Crear el botón del contenedor utilizando crearBotonImagen
            JButton botonContenedor = Boton.crearBotonImagen(imagenContenedor, imagenContenedorResaltado, 100, 200);

            // Añadir el botón a las listas
            botonesContenedores.add(botonContenedor);
            nombresContenedores.add(nombreContenedor);
        }
    }

    private void crearPanelFondo(JLayeredPane panelCapaPrincipal) {
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelFondo.setLayout(null);
        panelCapaPrincipal.add(panelFondo, Integer.valueOf(0));
    }

    private void crearPanelBotones(JLayeredPane panelCapaPrincipal) {
        panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(null);

        crearBotones();

        // Añadir botones al panel
        panelBotones.add(botonDesecho);

        // Añadir botones de contenedores al panel
        for (JButton botonContenedor : botonesContenedores) {
            panelBotones.add(botonContenedor);
        }

        // Añadir otros componentes
        for (JButton botonTratamiento : botonesTratamiento) {
            panelBotones.add(botonTratamiento);
        }

        for (JButton casillaTratamiento : casillasTratamiento) {
            panelBotones.add(casillaTratamiento);
        }

        panelBotones.add(informacionInstruccion);
        informacionInstruccion.setEnabled(false);
        panelBotones.add(informacionNivel);
        informacionNivel.setEnabled(false);
        panelBotones.add(informacionTurno);
        informacionTurno.setEnabled(false);
        panelBotones.add(informacionPuntos);
        informacionPuntos.setEnabled(false);
        panelBotones.add(informacionVida);
        informacionVida.setEnabled(false);

        // Añadir el cronómetro
        panelBotones.add(cronometroLabel);

        panelCapaPrincipal.add(panelBotones, Integer.valueOf(1));
    }

    private void ajustarTamanoPanelFondo() {
        panelFondo.setSize(getWidth(), getHeight());
    }

    private void ajustarTamanoPanelBotones() {
        panelBotones.setSize(getWidth(), getHeight());
    }

    private void actualizarPosicionesBotones() {
        int anchoVentana = getWidth();
        int altoVentana = getHeight();

        // Posicionar el botón de desecho
        int posicionYDesecho = altoVentana / 2 - 120;
        botonDesecho.setLocation((anchoVentana - botonDesecho.getWidth()) / 2, posicionYDesecho);
        posicionInicialDesecho = botonDesecho.getLocation();

        final int ANCHO_TRATAMIENTO = botonesTratamiento[0].getWidth();
        final int ALTO_TRATAMIENTO = botonesTratamiento[0].getHeight();
        final int ESPACIO_ENTRE_TRATAMIENTOS = 40;

        // Posicionar las casillas de tratamiento (siempre visibles)
        int posicionYCasillas = altoVentana - casillasTratamiento[0].getHeight() - ESPACIO_ENTRE_TRATAMIENTOS;
        int numCasillasVisibles = casillasTratamiento.length;
        int posicionInicialCasillasX = (anchoVentana - ANCHO_TRATAMIENTO * numCasillasVisibles - ESPACIO_ENTRE_TRATAMIENTOS * (numCasillasVisibles - 1)) / 2;

        for (int i = 0; i < casillasTratamiento.length; i++) {
            casillasTratamiento[i].setSize(ANCHO_TRATAMIENTO, ALTO_TRATAMIENTO);
            casillasTratamiento[i].setLocation(posicionInicialCasillasX + ((ANCHO_TRATAMIENTO + ESPACIO_ENTRE_TRATAMIENTOS) * i), posicionYCasillas);
        }

        // Posicionar botones de tratamiento (si están visibles)
        if (botonesTratamiento[0].isVisible()) {
            int posicionYTratamientos = posicionYDesecho;
            int numBotonesVisibles = botonesTratamiento.length;
            int posicionInicialBotonesX = (anchoVentana - ANCHO_TRATAMIENTO * numBotonesVisibles - ESPACIO_ENTRE_TRATAMIENTOS * (numBotonesVisibles - 1)) / 2;

            for (int i = 0; i < botonesTratamiento.length; i++) {
                botonesTratamiento[i].setLocation(posicionInicialBotonesX + ((ANCHO_TRATAMIENTO + ESPACIO_ENTRE_TRATAMIENTOS) * i), posicionYTratamientos);
            }
        }

        // Posicionar los contenedores
        final int ANCHO_CONTENEDOR = 100;
        final int ALTO_CONTENEDOR = 200;
        final int ESPACIO_ENTRE_CONTENEDORES = 40;

        List<JButton> contenedoresVisibles = getContenedoresVisibles();
        int totalAnchoContenedores = (ANCHO_CONTENEDOR * contenedoresVisibles.size()) + (ESPACIO_ENTRE_CONTENEDORES * (contenedoresVisibles.size() - 1));
        int posicionInicialX = (anchoVentana - totalAnchoContenedores) / 2;
        int posicionYContenedores = altoVentana - ALTO_CONTENEDOR - 140;

        for (int i = 0; i < contenedoresVisibles.size(); i++) {
            JButton botonContenedor = contenedoresVisibles.get(i);
            botonContenedor.setSize(ANCHO_CONTENEDOR, ALTO_CONTENEDOR);
            botonContenedor.setLocation(posicionInicialX + i * (ANCHO_CONTENEDOR + ESPACIO_ENTRE_CONTENEDORES), posicionYContenedores);
        }

        // Posicionar la información
        informacionNivel.setLocation(20, 20);
        informacionTurno.setLocation(20, 60);
        informacionPuntos.setLocation(20, 100);
        informacionVida.setLocation(anchoVentana - informacionVida.getWidth() - 20, 20);

        // Ajustar el tamaño del botón de instrucción si es necesario
        informacionInstruccion.setLocation((anchoVentana - informacionInstruccion.getWidth()) / 2, 40);

        // Posicionar el cronómetro al lado izquierdo de las vidas
        int espacioEntreCronometroYVida = 40;
        cronometroLabel.setLocation(
                informacionVida.getX() - cronometroLabel.getWidth() - espacioEntreCronometroYVida,
                informacionVida.getY() + (informacionVida.getHeight() - cronometroLabel.getHeight()) / 2
        );

        // Actualizar el estado de las casillas y los tratamientos al redimensionar
        actualizarEstadoTratamientosYCasillas();
    }

    private void crearBotones() {
        // Botón de Desecho
        botonDesecho = Boton.crearBotonImagen(Imagen.cargarImagen("/images/desechos/desechoGenerico.png"), 100, 100);

        // Añadir mouse listeners para arrastrar el 'botonDesecho'
        botonDesecho.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clicInicial = e.getPoint();
                botonDesecho.getParent().setComponentZOrder(botonDesecho, 0); // Traer al frente
                botonDesecho.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                verificarSolapamientoConContenedores();
            }
        });

        botonDesecho.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                moverBoton(botonDesecho, e);
            }
        });

        // Contenedores
        cargarImagenesContenedores();

        // Añadir botones de contenedores al panel
        for (JButton botonContenedor : botonesContenedores) {
            panelBotones.add(botonContenedor);
        }

        // Botones de Tratamiento (Inicialmente invisibles)
        botonesTratamiento = new JButton[]{
                Boton.crearBoton(botonGrande, botonGrandeResaltado, 380, 40, "Tratamiento 1", 16f),
                Boton.crearBoton(botonGrande, botonGrandeResaltado, 380, 40, "Tratamiento 2", 16f),
                Boton.crearBoton(botonGrande, botonGrandeResaltado, 380, 40, "Tratamiento 3", 16f)
        };

        casillasTratamiento = new JButton[]{
                Boton.crearBotonImagen(botonGrandeDeshabilitado, 380, 40),
                Boton.crearBotonImagen(botonGrandeDeshabilitado, 380, 40),
                Boton.crearBotonImagen(botonGrandeDeshabilitado, 380, 40)
        };

        casillasOcupadas = new boolean[3];

        // Los botones de tratamiento están ocultos inicialmente
        for (JButton botonTratamiento : botonesTratamiento) {
            botonTratamiento.setVisible(false);
        }

        // Las casillas de tratamiento son visibles desde el inicio
        for (JButton casillaTratamiento : casillasTratamiento) {
            casillaTratamiento.setVisible(true);
            casillaTratamiento.setEnabled(true);
        }

        // Añadir mouse listeners a los botones de tratamiento
        for (JButton botonTratamiento : botonesTratamiento) {
            botonTratamiento.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    clicInicial = e.getPoint();
                    botonTratamiento.getParent().setComponentZOrder(botonTratamiento, 0);
                    botonTratamiento.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    colocarEnCasilla(botonTratamiento);
                }
            });

            botonTratamiento.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    moverBoton(botonTratamiento, e);
                }
            });
        }

        // Información adicional
        informacionInstruccion = Boton.crearBotonTexto("Clasifica el desecho", false, 32f);

        informacionNivel = Boton.crearBotonTexto("Nivel: ", false, 32f);
        informacionTurno = Boton.crearBotonTexto("Turno: ", false, 32f);
        informacionPuntos = Boton.crearBotonTexto("Puntos: 0", false, 32f);
        informacionVida = Boton.crearBoton(imagenVida, imagenVida, imagenVida, 80, 80, "", 32f);

        // Crear el cronómetro
        cronometroLabel = new JLabel("3:00");
        cronometroLabel.setFont(new Font("minecraft", Font.PLAIN, 32));
        cronometroLabel.setForeground(Color.WHITE);
        cronometroLabel.setSize(80, 40);
    }

    private void actualizarTamanoInstruccion() {
        informacionInstruccion.setSize(informacionInstruccion.getPreferredSize());
        int anchoVentana = getWidth();
        informacionInstruccion.setLocation((anchoVentana - informacionInstruccion.getWidth()) / 2, informacionInstruccion.getY());
    }

    private void inicializarJuego() {
        // Obtener la lista de jugadores desde Jugar
        jugadores = new ArrayList<>(Jugar.jugadores);

        // Establecer vidas iniciales y estado de eliminación para cada jugador
        for (Jugador jugador : jugadores) {
            jugador.setEliminado(false);
        }

        // Iniciar el primer turno
        pasarAlSiguienteJugador();
    }

    private void pasarAlSiguienteJugador() {
        do {
            indiceJugadorActual++;
        } while (indiceJugadorActual < jugadores.size() && jugadores.get(indiceJugadorActual).isEliminado());

        if (indiceJugadorActual < jugadores.size()) {
            jugadorActual = jugadores.get(indiceJugadorActual);
            desechosClasificadosCorrectamente = 0;
            cargarDesechosYContenedoresDelNivel();
            iniciarCronometro();
        } else {
            // Todos los jugadores han jugado el nivel actual
            pasarAlSiguienteNivel();
        }
    }

    private void cargarDesechosYContenedoresDelNivel() {
        // Obtener todos los desechos del nivel
        List<Desecho> todosLosDesechos = new ArrayList<>(nivelActual.getDesechos());

        // Remover los desechos que el jugador ya ha clasificado correctamente
        List<Desecho> desechosClasificadosPorJugador = jugadorActual.getDesechosCorrectamenteClasificados();
        todosLosDesechos.removeAll(desechosClasificadosPorJugador);

        // Verificar si quedan desechos
        if (todosLosDesechos.isEmpty()) {
            // No hay desechos restantes para este nivel
            mostrarMensaje("No hay más desechos para clasificar en este nivel.");
            verificarFinDeTurno(); // O pasar al siguiente nivel si corresponde
            return;
        }

        Collections.shuffle(todosLosDesechos); // Mezclar desechos

        // Obtener la cantidad de desechos a clasificar para este nivel
        int desechosAClasificar = nivelActual.getDesechosAClasificar();

        // Limitar la lista de desechos al número especificado
        if (todosLosDesechos.size() > desechosAClasificar) {
            desechosNivelActual = new ArrayList<>(todosLosDesechos.subList(0, desechosAClasificar));
        } else {
            desechosNivelActual = todosLosDesechos;
        }

        // Obtener los contenedores únicos asociados a los desechos actuales
        contenedoresNivelActual = new ArrayList<>();
        for (Desecho desecho : desechosNivelActual) {
            Contenedor contenedor = RepositorioDesechos.obtenerContenedor(desecho.getTipoContenedor().getNombre());
            if (!contenedoresNivelActual.contains(contenedor)) {
                contenedoresNivelActual.add(contenedor);
            }
        }

        actualizarVisibilidadContenedores(); // Mostrar los contenedores del nivel actual

        actualizarPosicionesBotones(); // Asegurar que los contenedores se posicionen correctamente

        cargarSiguienteDesecho(); // Cargar el siguiente desecho
    }

    private void iniciarCronometro() {
        if (cronometroTimer != null) {
            cronometroTimer.stop();
        }
        tiempoRestante = nivelActual.getMaxTiempoPorJugador() / 1000; // Convertir a segundos
        tiempoAgotado = false;

        cronometroLabel.setText(String.format("%d:%02d", tiempoRestante / 60, tiempoRestante % 60));

        cronometroTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    tiempoRestante--;
                    int minutos = tiempoRestante / 60;
                    int segundos = tiempoRestante % 60;
                    cronometroLabel.setText(String.format("%d:%02d", minutos, segundos));
                } else {
                    cronometroTimer.stop();
                    tiempoAgotado = true;
                    verificarFinDeTurno();
                }
            }
        });
        cronometroTimer.start();
    }

    private void cargarSiguienteDesecho() {
        if (!desechosNivelActual.isEmpty()) {
            desechoActual = desechosNivelActual.remove(0);

            // Intentar cargar la imagen específica del desecho
            String nombreImagenDesecho = normalizarNombre(desechoActual.getNombre()) + ".png";
            Image imagenDesecho = Imagen.cargarImagen("/images/desechos/" + nombreImagenDesecho);

            // Si no se pudo cargar la imagen específica, usar la genérica
            if (imagenDesecho == null || Imagen.esImagenPredeterminada(imagenDesecho)) {
                imagenDesecho = Imagen.cargarImagen("/images/desechos/desechoGenerico.png");
            }

            // Configurar la imagen en el botón de desecho
            botonDesecho.setIcon(new ImageIcon(imagenDesecho.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

            // Actualizar la información en pantalla
            actualizarInformacionPantalla();

            // Mostrar el desecho y ocultar los tratamientos
            botonDesecho.setVisible(true);
            for (JButton botonTratamiento : botonesTratamiento) {
                botonTratamiento.setVisible(false);
                botonTratamiento.setEnabled(true);
                botonTratamiento.putClientProperty("casillaIndex", null);
            }
            for (JButton casilla : casillasTratamiento) {
                casilla.setEnabled(true);
                casilla.setVisible(true);
            }
            informacionInstruccion.setText("Clasifica el desecho");
            actualizarTamanoInstruccion();

            // Restablecer la posición inicial del desecho
            botonDesecho.setLocation(posicionInicialDesecho);
        } else {
            // Verificar si el jugador cumplió el objetivo del nivel
            verificarFinDeTurno();
        }
    }

    private void verificarFinDeTurno() {
        if (jugadorActual.getVidas() <= 0) {
            // El jugador ha perdido todas sus vidas y es eliminado
            mostrarMensaje("¡" + jugadorActual.getNombre() + " ha perdido todas sus vidas y ha sido eliminado!");
            jugadorActual.setEliminado(true);
            pasarAlSiguienteJugador();
        } else if (desechosClasificadosCorrectamente >= nivelActual.getObjetivo()) {
            // El jugador ha alcanzado el objetivo y puede avanzar
            mostrarMensaje(jugadorActual.getNombre() + " ha completado el nivel.");
            pasarAlSiguienteJugador();
        } else if (tiempoAgotado || desechosNivelActual.isEmpty()) {
            // Se acabó el tiempo o ya no hay desechos
            if (desechosClasificadosCorrectamente >= nivelActual.getObjetivo()) {
                // El jugador alcanzó el objetivo
                mostrarMensaje(jugadorActual.getNombre() + " ha completado el nivel.");
                pasarAlSiguienteJugador();
            } else {
                // El jugador no alcanzó el objetivo y es eliminado
                mostrarMensaje("¡" + jugadorActual.getNombre() + " no alcanzó el objetivo y ha sido eliminado!");
                jugadorActual.setEliminado(true);
                pasarAlSiguienteJugador();
            }
        } else {
            // Continuar con el siguiente desecho
            cargarSiguienteDesecho();
        }
    }

    private void pasarAlSiguienteNivel() {
        // Verificar si hay jugadores no eliminados
        boolean hayJugadoresNoEliminados = false;
        int i = 0;

        while (i < jugadores.size() && !hayJugadoresNoEliminados) {
            if (!jugadores.get(i).isEliminado()) {
                hayJugadoresNoEliminados = true;
            }
            i++;
        }

        if (!hayJugadoresNoEliminados) {
            // Todos los jugadores han sido eliminados
            mostrarMensaje("Todos los jugadores han sido eliminados. Fin del juego.");
            declararGanadorPorPuntos();
            cardLayout.show(panelPrincipal, "menuRecicland");
            return;
        }

        indiceNivelActual++;
        if (indiceNivelActual < niveles.size()) {
            nivelActual = niveles.get(indiceNivelActual);
            indiceJugadorActual = -1; // Reiniciamos el índice de jugador
            pasarAlSiguienteJugador(); // Iniciamos el siguiente nivel con los jugadores restantes
        } else {
            // Fin del juego
            mostrarMensaje("¡Fin del juego!");
            declararGanadorPorPuntos();
            cardLayout.show(panelPrincipal, "menuRecicland");
        }
    }

    private void declararGanadorPorPuntos() {
        int maxPuntos = -1;
        List<Jugador> ganadores = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            if (jugador.getPuntos() > maxPuntos) {
                maxPuntos = jugador.getPuntos();
                ganadores.clear();
                ganadores.add(jugador);
            } else if (jugador.getPuntos() == maxPuntos) {
                ganadores.add(jugador);
            }
        }
        if (ganadores.size() == 1) {
            mostrarMensaje("El ganador es: " + ganadores.get(0).getNombre() + " con " + maxPuntos + " puntos.");
        } else {
            StringBuilder mensaje = new StringBuilder("Hay un empate entre los jugadores con " + maxPuntos + " puntos:\n");
            for (Jugador jugador : ganadores) {
                mensaje.append("- ").append(jugador.getNombre()).append("\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }

    private void moverBoton(JButton boton, MouseEvent e) {
        int thisX = boton.getLocation().x;
        int thisY = boton.getLocation().y;
        int xMoved = e.getX() - clicInicial.x;
        int yMoved = e.getY() - clicInicial.y;
        int X = thisX + xMoved;
        int Y = thisY + yMoved;
        int anchoPanel = panelBotones.getWidth();
        int altoPanel = panelBotones.getHeight();

        if (X < 0) {
            X = 0;
        } else if (X + boton.getWidth() > anchoPanel) {
            X = anchoPanel - boton.getWidth();
        }

        if (Y < 0) {
            Y = 0;
        } else if (Y + boton.getHeight() > altoPanel) {
            Y = altoPanel - boton.getHeight();
        }

        boton.setLocation(X, Y);

        // Verificar solapamiento con contenedores durante el arrastre
        if (boton == botonDesecho) {
            verificarSolapamientoConContenedoresDuranteArrastre();
        }
    }

    private void verificarSolapamientoConContenedores() {
        Rectangle limitesDesecho = botonDesecho.getBounds();

        // Lista de botones de contenedores visibles
        List<JButton> contenedoresVisibles = getContenedoresVisibles();

        boolean encontrado = false;
        int i = 0;
        while (i < contenedoresVisibles.size() && !encontrado) {
            JButton contenedor = contenedoresVisibles.get(i);
            if (limitesDesecho.intersects(contenedor.getBounds())) {
                // El desecho está sobre un contenedor
                String nombreContenedorSeleccionado = getNombreContenedor(contenedor);
                if (nombreContenedorSeleccionado.equalsIgnoreCase(normalizarNombre(desechoActual.getTipoContenedor().getNombre()))) {
                    // Contenedor correcto
                    jugadorActual.usarContenedor(desechoActual.getTipoContenedor());
                    mostrarTratamientos();
                    botonDesecho.setVisible(false);
                } else {
                    // Contenedor incorrecto
                    jugadorActual.restarVida(nivelActual.getVidasPerdidasPorError());
                    mostrarMensaje("Contenedor incorrecto. Vidas restantes: " + jugadorActual.getVidas());
                    actualizarInformacionPantalla();
                    if (jugadorActual.getVidas() <= 0) {
                        verificarFinDeTurno();
                    } else {
                        cargarSiguienteDesecho();
                    }
                }
                encontrado = true;
            }
            i++;
        }
    }

    private void verificarSolapamientoConContenedoresDuranteArrastre() {
        Rectangle limitesDesecho = botonDesecho.getBounds();

        // Lista de botones de contenedores visibles
        List<JButton> contenedoresVisibles = getContenedoresVisibles();

        JButton contenedorActual = null;
        int i = 0;

        while (i < contenedoresVisibles.size() && contenedorActual == null) {
            if (limitesDesecho.intersects(contenedoresVisibles.get(i).getBounds())) {
                contenedorActual = contenedoresVisibles.get(i);
            }
            i++;
        }

        if (contenedorActual != null) {
            if (contenedorResaltadoPorArrastre != contenedorActual) {
                // Resetear el contenedor resaltado previamente
                if (contenedorResaltadoPorArrastre != null) {
                    resetearImagenContenedor(contenedorResaltadoPorArrastre);
                }
                // Resaltar el nuevo contenedor
                resaltarImagenContenedor(contenedorActual);
                contenedorResaltadoPorArrastre = contenedorActual;
            }
        } else {
            if (contenedorResaltadoPorArrastre != null) {
                resetearImagenContenedor(contenedorResaltadoPorArrastre);
                contenedorResaltadoPorArrastre = null;
            }
        }
    }

    private void colocarEnCasilla(JButton botonTratamiento) {
        Integer casillaIndex = (Integer) botonTratamiento.getClientProperty("casillaIndex");

        // Si el botón ya estaba en una casilla, liberar esa casilla
        if (casillaIndex != null && casillasOcupadas[casillaIndex]) {
            casillasOcupadas[casillaIndex] = false;
            casillasTratamiento[casillaIndex].setEnabled(true); // Hacer visible la casilla original
        }

        boolean colocado = false;
        int i = 0;

        while (i < casillasTratamiento.length && !colocado) {
            if (!casillasOcupadas[i] && botonTratamiento.getBounds().intersects(casillasTratamiento[i].getBounds()) && casillasTratamiento[i].isEnabled()) {
                // Colocar el botón en la nueva casilla y deshabilitar la casilla
                casillasTratamiento[i].setEnabled(false);
                botonTratamiento.setLocation(casillasTratamiento[i].getLocation());
                casillasOcupadas[i] = true;
                botonTratamiento.putClientProperty("casillaIndex", i); // Guardar el índice de la nueva casilla ocupada
                colocado = true;
            }
            i++;
        }

        // Si el botón fue movido fuera de todas las casillas, limpiar la propiedad de casilla
        if (!colocado) {
            botonTratamiento.putClientProperty("casillaIndex", null);
        }

        // Verificar el orden del tratamiento
        if (todasCasillasNecesariasOcupadas()) {
            verificarOrdenTratamiento();
        }
    }

    private void actualizarVisibilidadContenedores() {
        // Ocultar todos los contenedores inicialmente
        ocultarTodosLosContenedores();

        // Mostrar solo los contenedores correspondientes al nivel actual
        for (Contenedor contenedor : contenedoresNivelActual) {
            String nombreContenedor = normalizarNombre(contenedor.getNombre());
            JButton botonContenedor = getBotonContenedorPorNombre(nombreContenedor);
            if (botonContenedor != null) {
                botonContenedor.setVisible(true);
            }
        }
        panelBotones.revalidate();
        panelBotones.repaint();
    }

    private void mostrarTratamientos() {
        // Obtener los pasos de tratamiento
        pasosTratamientoCorrectos = new ArrayList<>(desechoActual.getPasosTratamiento());
        pasosTratamientoDesordenados = new ArrayList<>(pasosTratamientoCorrectos);
        Collections.shuffle(pasosTratamientoDesordenados);

        // Actualizar los textos de los botones de tratamiento
        int numPasos = pasosTratamientoDesordenados.size();
        for (int i = 0; i < botonesTratamiento.length; i++) {
            botonesTratamiento[i].setVisible(true);
            casillasTratamiento[i].setVisible(true);
            botonesTratamiento[i].setText(pasosTratamientoDesordenados.get(i));
            botonesTratamiento[i].setEnabled(true);
            casillasTratamiento[i].setEnabled(true);
            casillasOcupadas[i] = false;
        }

        // Ajustar el tamaño de los botones de tratamiento
        actualizarPosicionesBotones();

        // Cambiar el texto de la instrucción
        informacionInstruccion.setText("Ordena su tratamiento");
        actualizarTamanoInstruccion();
    }

    private void actualizarInformacionPantalla() {
        informacionNivel.setText("Nivel: " + nivelActual.getNombreNivel());
        informacionNivel.setSize(informacionNivel.getPreferredSize());
        informacionTurno.setText("Turno: " + jugadorActual.getNombre());
        informacionTurno.setSize(informacionTurno.getPreferredSize());
        informacionPuntos.setText("Puntos: " + jugadorActual.getPuntos());
        informacionPuntos.setSize(informacionPuntos.getPreferredSize());
        informacionVida.setText(String.valueOf(jugadorActual.getVidas()));
        informacionVida.setSize(informacionVida.getPreferredSize());
    }

    private void actualizarEstadoTratamientosYCasillas() {
        for (int i = 0; i < botonesTratamiento.length; i++) {
            JButton botonTratamiento = botonesTratamiento[i];
            Integer casillaIndex = (Integer) botonTratamiento.getClientProperty("casillaIndex");

            boolean estaSobreCasilla = false;
            int indiceCasilla = -1;
            int j = 0;

            while (j < casillasTratamiento.length && !estaSobreCasilla) {
                if (botonTratamiento.getBounds().intersects(casillasTratamiento[j].getBounds())) {
                    estaSobreCasilla = true;
                    indiceCasilla = j;
                }
                j++;
            }

            if (estaSobreCasilla) {
                if (casillaIndex == null || casillaIndex != indiceCasilla) {
                    // Si estaba en otra casilla, liberar la anterior
                    if (casillaIndex != null && casillasOcupadas[casillaIndex]) {
                        casillasOcupadas[casillaIndex] = false;
                        casillasTratamiento[casillaIndex].setEnabled(true);
                    }
                    // Ocupar la nueva casilla
                    casillasOcupadas[indiceCasilla] = true;
                    casillasTratamiento[indiceCasilla].setEnabled(false);
                    botonTratamiento.setLocation(casillasTratamiento[indiceCasilla].getLocation());
                    botonTratamiento.putClientProperty("casillaIndex", indiceCasilla);
                }
            } else {
                // Si no está sobre ninguna casilla
                if (casillaIndex != null && casillasOcupadas[casillaIndex]) {
                    casillasOcupadas[casillaIndex] = false;
                    casillasTratamiento[casillaIndex].setEnabled(true);
                }
                botonTratamiento.putClientProperty("casillaIndex", null);
            }
        }
    }

    private void ocultarTodosLosContenedores() {
        JButton[] todosLosBotones = getTodosLosBotonesContenedores();
        for (JButton botonContenedor : todosLosBotones) {
            botonContenedor.setVisible(false);
        }
    }

    private void resaltarImagenContenedor(JButton contenedor) {
        // Forzamos el estado de rollover
        contenedor.getModel().setRollover(true);
        contenedor.repaint();
    }

    private void resetearImagenContenedor(JButton contenedor) {
        // Cancelamos el estado de rollover
        contenedor.getModel().setRollover(false);
        contenedor.repaint();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private String normalizarNombre(String nombre) {
        nombre = nombre.toLowerCase().replaceAll("\\s+", "");
        nombre = nombre.replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u").replace("ñ", "n");
        return nombre;
    }

    private boolean todasCasillasNecesariasOcupadas() {
        int numCasillasNecesarias = pasosTratamientoCorrectos.size();
        int numCasillasOcupadas = 0;
        for (int i = 0; i < numCasillasNecesarias; i++) {
            if (casillasOcupadas[i]) {
                numCasillasOcupadas++;
            }
        }
        return numCasillasOcupadas == numCasillasNecesarias;
    }

    private JButton[] getTodosLosBotonesContenedores() {
        return botonesContenedores.toArray(new JButton[0]);
    }

    private List<JButton> getContenedoresVisibles() {
        List<JButton> contenedoresVisibles = new ArrayList<>();
        for (JButton botonContenedor : botonesContenedores) {
            if (botonContenedor.isVisible()) {
                contenedoresVisibles.add(botonContenedor);
            }
        }
        return contenedoresVisibles;
    }

    private JButton getBotonContenedorPorNombre(String nombreContenedor) {
        for (int i = 0; i < nombresContenedores.size(); i++) {
            if (nombresContenedores.get(i).equalsIgnoreCase(nombreContenedor)) {
                return botonesContenedores.get(i);
            }
        }
        return null;
    }

    private String getNombreContenedor(JButton botonContenedor) {
        for (int i = 0; i < botonesContenedores.size(); i++) {
            if (botonesContenedores.get(i) == botonContenedor) {
                return nombresContenedores.get(i);
            }
        }
        return "";
    }

    private void verificarOrdenTratamiento() {
        boolean ordenCorrecto = true;

        for (int i = 0; i < pasosTratamientoCorrectos.size() && ordenCorrecto; i++) {
            boolean encontrado = false;

            for (int j = 0; j < botonesTratamiento.length && !encontrado; j++) {
                JButton botonTratamiento = botonesTratamiento[j];
                Integer casillaIndex = (Integer) botonTratamiento.getClientProperty("casillaIndex");
                if (casillaIndex != null && casillaIndex == i) {
                    // Verifica directamente si el texto coincide con el paso correcto
                    if (!botonTratamiento.getText().equalsIgnoreCase(pasosTratamientoCorrectos.get(i))) {
                        ordenCorrecto = false;
                    }
                    encontrado = true;
                }
            }

            // Si no se encuentra un botón para el índice actual, el orden es incorrecto
            if (!encontrado) {
                ordenCorrecto = false;
            }
        }

        if (ordenCorrecto) {
            // Tratamiento correcto
            jugadorActual.sumarPuntos(nivelActual.getPuntosPorRespuesta());
            desechosClasificadosCorrectamente++;
            mostrarMensaje("¡Correcto! Puntos actuales: " + jugadorActual.getPuntos());
            actualizarInformacionPantalla();

            // Agregar el desecho actual al registro del jugador
            jugadorActual.agregarDesechoCorrectamenteClasificado(desechoActual);
        } else {
            // Tratamiento incorrecto
            jugadorActual.restarVida(nivelActual.getVidasPerdidasPorError());
            mostrarMensaje("Tratamiento incorrecto. Vidas restantes: " + jugadorActual.getVidas());
            actualizarInformacionPantalla();
        }

        // Restablecer las casillas y botones de tratamiento para el siguiente desecho
        for (int i = 0; i < casillasTratamiento.length; i++) {
            casillasOcupadas[i] = false;
        }

        // Continuar con el siguiente desecho o verificar fin de turno
        if (jugadorActual.getVidas() <= 0) {
            verificarFinDeTurno();
        } else {
            cargarSiguienteDesecho();
        }
    }
}