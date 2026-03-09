package Vista;

import Modelo.Notas;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class PantallaNotas extends JFrame {

    private static final Color FONDO_BARRA   = new Color(45,  45,  45);
    private static final Color FONDO_LISTA   = new Color(35,  35,  35);
    private static final Color FONDO_EDITOR  = new Color(255, 251, 235);
    private static final Color FONDO_TITULO  = new Color(255, 248, 210);
    private static final Color TEXTO_CLARO   = new Color(240, 240, 240);
    private static final Color TEXTO_EDITOR  = new Color(30,  30,  30);
    private static final Color AMARILLO      = new Color(255, 214, 10);
    private static final Color ROJO          = new Color(210, 50,  50);
    private static final Color GRIS_BTN      = new Color(80,  80,  80);

    private final String usuario;

    private DefaultListModel<String> modeloLista;
    private JList<String>            listaNotas;
    private JTextField               campoTitulo;
    private JTextArea                areaContenido;
    private JButton btnGuardar, btnNueva, btnCerrarSesion;
    private JButton btnEliminar, btnBorrarTodas;

    public PantallaNotas(String usuario) {
        this.usuario = usuario;

        setTitle("Notas - " + usuario);
        setSize(900, 580);
        setMinimumSize(new Dimension(700, 450));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirBarra();
        construirLateral();
        construirEditor();
        configurarListeners();
        cargarListaNotas();
    }

    private void construirBarra() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(FONDO_BARRA);
        barra.setPreferredSize(new Dimension(0, 50));
        barra.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JLabel etiquetaUsuario = new JLabel("Notas de " + usuario);
        etiquetaUsuario.setFont(new Font("Dialog", Font.BOLD, 16));
        etiquetaUsuario.setForeground(TEXTO_CLARO);
        barra.add(etiquetaUsuario, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panelBotones.setOpaque(false);

        btnGuardar      = crearBoton("Guardar",        AMARILLO, Color.BLACK);
        btnNueva        = crearBoton("Nueva nota",      GRIS_BTN, TEXTO_CLARO);
        btnCerrarSesion = crearBoton("Cerrar sesion",   GRIS_BTN, TEXTO_CLARO);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnNueva);
        panelBotones.add(btnCerrarSesion);
        barra.add(panelBotones, BorderLayout.EAST);

        add(barra, BorderLayout.NORTH);
    }

    private void construirLateral() {

        modeloLista = new DefaultListModel<>();
        listaNotas  = new JList<>(modeloLista);
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaNotas.setBackground(FONDO_LISTA);
        listaNotas.setForeground(TEXTO_CLARO);
        listaNotas.setFont(new Font("Dialog", Font.PLAIN, 13));
        listaNotas.setFixedCellHeight(38);
        listaNotas.setSelectionBackground(AMARILLO);
        listaNotas.setSelectionForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(listaNotas);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 60, 60)));
        scroll.getViewport().setBackground(FONDO_LISTA);

        JLabel cabecera = new JLabel("  Mis notas");
        cabecera.setFont(new Font("Dialog", Font.BOLD, 11));
        cabecera.setForeground(new Color(150, 150, 150));
        cabecera.setOpaque(true);
        cabecera.setBackground(new Color(28, 28, 28));
        cabecera.setPreferredSize(new Dimension(220, 26));

        btnEliminar    = crearBoton("Eliminar",     ROJO, TEXTO_CLARO);
        btnBorrarTodas = crearBoton("Borrar todas", ROJO, TEXTO_CLARO);
        btnEliminar.setFont(new Font("Dialog", Font.PLAIN, 12));
        btnBorrarTodas.setFont(new Font("Dialog", Font.PLAIN, 12));

        JPanel panelEliminar = new JPanel(new GridLayout(1, 2, 4, 0));
        panelEliminar.setBackground(new Color(28, 28, 28));
        panelEliminar.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        panelEliminar.add(btnEliminar);
        panelEliminar.add(btnBorrarTodas);

        JPanel lateral = new JPanel(new BorderLayout());
        lateral.setPreferredSize(new Dimension(220, 0));
        lateral.add(cabecera,       BorderLayout.NORTH);
        lateral.add(scroll,         BorderLayout.CENTER);
        lateral.add(panelEliminar,  BorderLayout.SOUTH);

        add(lateral, BorderLayout.WEST);
    }


    private void construirEditor() {
        JPanel panelEditor = new JPanel(new BorderLayout());
        panelEditor.setBackground(FONDO_EDITOR);

        campoTitulo = new JTextField();
        campoTitulo.setFont(new Font("Dialog", Font.BOLD, 20));
        campoTitulo.setForeground(TEXTO_EDITOR);
        campoTitulo.setBackground(FONDO_TITULO);
        campoTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 210, 170)),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        areaContenido = new JTextArea();
        areaContenido.setFont(new Font("Serif", Font.PLAIN, 14));
        areaContenido.setForeground(TEXTO_EDITOR);
        areaContenido.setBackground(FONDO_EDITOR);
        areaContenido.setLineWrap(true);
        areaContenido.setWrapStyleWord(true);
        areaContenido.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JScrollPane scrollEditor = new JScrollPane(areaContenido);
        scrollEditor.setBorder(BorderFactory.createEmptyBorder());
        scrollEditor.getViewport().setBackground(FONDO_EDITOR);

        panelEditor.add(campoTitulo,   BorderLayout.NORTH);
        panelEditor.add(scrollEditor,  BorderLayout.CENTER);

        add(panelEditor, BorderLayout.CENTER);
    }

    private void configurarListeners() {
        listaNotas.addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                String nombreArchivo = listaNotas.getSelectedValue();
                if (nombreArchivo != null) {
                    cargarNota(nombreArchivo);
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            String titulo    = campoTitulo.getText().trim();
            String contenido = areaContenido.getText().trim();
            String error = Notas.guardarNota(usuario, titulo, contenido);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nota guardada.", "OK", JOptionPane.INFORMATION_MESSAGE);
                cargarListaNotas();
            }
        });

        btnEliminar.addActionListener(e -> {
            String seleccionada = listaNotas.getSelectedValue();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una nota primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int respuesta = JOptionPane.showConfirmDialog(this,
                "Borrar '" + seleccionada.replace(".txt", "") + "'?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                Notas.eliminarNota(usuario, seleccionada);
                limpiarEditor();
                cargarListaNotas();
            }
        });

        btnNueva.addActionListener(e -> limpiarEditor());

        btnBorrarTodas.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                "Borrar TODAS las notas? Esta accion no se puede deshacer.",
                "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                Notas.eliminarTodasLasNotas(usuario);
                limpiarEditor();
                cargarListaNotas();
            }
        });

        btnCerrarSesion.addActionListener(e -> {
            new PantallaLogin().setVisible(true);
            dispose();
        });
    }

    private void cargarListaNotas() {
        modeloLista.clear();
        List<String> notas = Notas.listarNotas(usuario);
        for (String nombre : notas) {
            modeloLista.addElement(nombre);
        }
    }

    private void cargarNota(String nombreArchivo) {
        String contenido = Notas.leerNota(usuario, nombreArchivo);
        if (contenido != null) {
            campoTitulo.setText(nombreArchivo.replace(".txt", ""));
            areaContenido.setText(contenido);
            areaContenido.setCaretPosition(0);
        } else {
            JOptionPane.showMessageDialog(this, "Error al abrir la nota.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarEditor() {
        campoTitulo.setText("");
        areaContenido.setText("");
        listaNotas.clearSelection();
    }

    private JButton crearBoton(String texto, Color fondo, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Dialog", Font.BOLD, 12));
        boton.setBackground(fondo);
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}