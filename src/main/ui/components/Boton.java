package main.ui.components;

import main.utils.Sonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Boton {

    private static void configurarBoton(JButton boton, float tamanoTexto) {
        Sonido sonidoBoton = new Sonido("/sounds/buttonClick.wav");
        ActionListener listenerSonido = e -> sonidoBoton.reproducir();
        boton.addActionListener(listenerSonido);
        boton.setFont(new Font("minecraft", Font.PLAIN, Math.round(tamanoTexto)));
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setForeground(java.awt.Color.WHITE);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.CENTER);
        boton.setUI(new BotonConSombraUI());
        boton.setSize(boton.getPreferredSize());
    }

    private static void configurarBotonImagen(JButton boton, int ancho, int alto) {
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setSize(ancho, alto);
    }

    public static JButton crearBoton(Image imagen, int ancho, int alto, String texto, float tamanoTexto) {
        return crearBoton(imagen, null, null, ancho, alto, texto, tamanoTexto);
    }

    public static JButton crearBoton(Image imagen, Image imagenResaltada, int ancho, int alto, String texto, float tamanoTexto) {
        return crearBoton(imagen, imagenResaltada, null, ancho, alto, texto, tamanoTexto);
    }

    public static JButton crearBoton(Image imagen, Image imagenResaltada, Image imagenDeshabilitada, int ancho, int alto, String texto, float tamanoTexto) {
        JButton boton = new JButton(texto);

        // Escalar imágenes
        if (imagen != null) {
            Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagenEscalada));
        }
        if (imagenResaltada != null) {
            Image imagenResaltadaEscalada = imagenResaltada.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            boton.setRolloverIcon(new ImageIcon(imagenResaltadaEscalada));
        }
        if (imagenDeshabilitada != null) {
            Image imagenDeshabilitadaEscalada = imagenDeshabilitada.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            boton.setDisabledIcon(new ImageIcon(imagenDeshabilitadaEscalada));
        }

        // Configurar estilo y comportamiento del botón
        configurarBoton(boton, tamanoTexto);
        boton.setSize(ancho, alto);

        return boton;
    }

    public static JButton crearBotonImagen(Image imagen, int ancho, int alto) {
        return crearBotonImagen(imagen, null, ancho, alto);
    }

    public static JButton crearBotonImagen(Image imagen, Image imagenResaltada, int ancho, int alto) {
        JButton boton = new JButton();

        // Escalar imágenes
        if (imagen != null) {
            Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagenEscalada));
        }
        if (imagenResaltada != null) {
            Image imagenResaltadaEscalada = imagenResaltada.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            boton.setRolloverIcon(new ImageIcon(imagenResaltadaEscalada));
        }

        // Configurar estilo y comportamiento del botón
        configurarBotonImagen(boton, ancho, alto);

        return boton;
    }

    public static JLayeredPane crearBotonConIcono(Image imagen, Image imagenResaltada, int ancho, int alto, String texto, int alineacion, int tamanoIcono, int margen, int separacionIconos, float tamanoTexto, Object... iconos) {
        JButton botonBase = crearBoton(imagen, imagenResaltada, ancho, alto, texto, tamanoTexto);

        // Configurar Panel en capas
        JLayeredPane panelCapas = new JLayeredPane();
        panelCapas.setPreferredSize(new Dimension(ancho, alto));
        panelCapas.setSize(panelCapas.getPreferredSize());

        // Agregar botón
        botonBase.setLocation(0, 0);
        panelCapas.add(botonBase, Integer.valueOf(0));

        int posicionX;
        if (alineacion == 1) {
            posicionX = ancho - margen - tamanoIcono;
        } else if (alineacion == -1) {
            posicionX = margen;
        } else if (alineacion == 0) {
            int anchoTotalIconos = (tamanoIcono * iconos.length) + (separacionIconos * (iconos.length - 1));
            posicionX = (ancho - anchoTotalIconos) / 2;
        } else {
            throw new IllegalArgumentException("Alineación no válida. Usa -1 para izquierda, 0 para centrado o 1 para derecha.");
        }

        for (Object icono : iconos) {
            JLabel etiquetaIcono = null;
            switch (icono) {
                case Image image -> {
                    // Si el icono es de tipo Image
                    Image imagenEscalada = image.getScaledInstance(tamanoIcono, tamanoIcono, Image.SCALE_SMOOTH);
                    etiquetaIcono = new JLabel(new ImageIcon(imagenEscalada));
                }
                case ImageIcon imageIcon -> {
                    // Si el icono es de tipo ImageIcon
                    Image imagenEscalada = imageIcon.getImage().getScaledInstance(tamanoIcono, tamanoIcono, Image.SCALE_SMOOTH);
                    etiquetaIcono = new JLabel(new ImageIcon(imagenEscalada));
                }
                case JLabel etiqueta -> {
                    // Si el icono es de tipo JLabel
                    etiquetaIcono = etiqueta;
                }
                default -> throw new IllegalArgumentException("El objeto debe ser de tipo Image, ImageIcon o JLabel.");
            }

            etiquetaIcono.setBounds(posicionX, (alto - tamanoIcono) / 2, tamanoIcono, tamanoIcono);
            panelCapas.add(etiquetaIcono, Integer.valueOf(1));

            if (alineacion == 1) {
                posicionX -= (tamanoIcono + separacionIconos);
            } else {
                posicionX += (tamanoIcono + separacionIconos);
            }
        }

        return panelCapas;
    }

    public static JButton crearBotonTexto(String texto, boolean subrayado, float tamanoTexto) {
        JButton boton = new JButton(texto);

        // Configurar estilo y comportamiento del botón
        configurarBoton(boton, tamanoTexto);

        // Aplica la UI de subrayado si está habilitado
        if (subrayado) {
            boton.setUI(new BotonConSubrayadoUI(boton));
        }

        return boton;
    }

    public static JLayeredPane crearCampoTexto(Image fondo, Image fondoResaltado, int ancho, int alto, float tamanoTexto) {
        // Crear un panel en capas
        JLayeredPane panelCapas = new JLayeredPane();
        panelCapas.setPreferredSize(new Dimension(ancho, alto));
        panelCapas.setSize(ancho, alto);

        // Crear un JLabel para la imagen de fondo
        JLabel fondoLabel = new JLabel(new ImageIcon(fondo.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)));
        fondoLabel.setBounds(0, 0, ancho, alto);

        // Crear el campo de texto
        JTextField campoTexto = new JTextField() {
            private boolean mostrarCursor = true;
            private Timer cursorTimer;

            {
                // Iniciar el temporizador para hacer parpadear el cursor
                cursorTimer = new Timer(300, e -> {
                    mostrarCursor = !mostrarCursor;
                    repaint();
                });
                cursorTimer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mostrarCursor && isFocusOwner()) { // Solo mostrar el cursor si el campo tiene el foco
                    g.setColor(Color.WHITE); // Color blanco para el cursor
                    FontMetrics fm = g.getFontMetrics();
                    int x = fm.stringWidth(getText()) + getInsets().left;
                    int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent() + 2; // Ajustar la posición vertical
                    g.drawString("_", x, y); // Dibujar el guion bajo como cursor
                }
            }

            @Override
            public void removeNotify() {
                super.removeNotify();
                cursorTimer.stop(); // Detener el temporizador cuando el campo se elimina
            }
        };

        campoTexto.setFont(new Font("minecraft", Font.PLAIN, Math.round(tamanoTexto)));
        campoTexto.setOpaque(false); // Hacer el campo transparente para ver la imagen de fondo
        campoTexto.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Sin bordes visibles
        campoTexto.setForeground(Color.WHITE);
        campoTexto.setHorizontalAlignment(JTextField.LEFT); // Alinear el texto a la izquierda
        campoTexto.setBounds(0, 0, ancho, alto); // Ajustar el campo de texto al mismo tamaño que el fondo

        // Ocultar el cursor vertical por defecto
        campoTexto.setCaret(new javax.swing.text.DefaultCaret() {
            {
                setBlinkRate(0); // Detener el parpadeo
            }

            @Override
            public void paint(Graphics g) {
                // No hacer nada, esto oculta el cursor vertical
            }
        });

        // Agregar un listener para cambiar la imagen de fondo cuando el campo esté en foco
        campoTexto.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                fondoLabel.setIcon(new ImageIcon(fondoResaltado.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                fondoLabel.setIcon(new ImageIcon(fondo.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)));
            }
        });

        // Agregar componentes al panel en capas
        panelCapas.add(fondoLabel, Integer.valueOf(0)); // Fondo en la capa inferior
        panelCapas.add(campoTexto, Integer.valueOf(1)); // Campo de texto en la capa superior

        return panelCapas;
    }
}