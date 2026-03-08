package Vista;

import Modelo.Notas;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PantallaNotas extends JFrame {

    private final String user; // usuario logueado, lo recibimos en el constructor

    private JList<String> listaNotas;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaTexto;
    private JTextField campoTitulo;
    private JButton btnGuardar, btnEliminar, btnLimpiar, btnBorrarTodas, btnCerrarSesion;

    public PantallaNotas(String user) {
        this.user = user;

        setTitle("Gestor de Notas — " + user);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloLista = new DefaultListModel<>();
        listaNotas = new JList<>(modeloLista);
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaNotas);
        scrollLista.setPreferredSize(new Dimension(200, 0));
        scrollLista.setBorder(BorderFactory.createTitledBorder("Tus notas"));
        add(scrollLista, BorderLayout.WEST);

        // ── Panel central: título + contenido ────────────────────────────────────
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        campoTitulo = new JTextField();
        campoTitulo.setBorder(BorderFactory.createTitledBorder("Título"));
        panelCentro.add(campoTitulo, BorderLayout.NORTH);

        areaTexto = new JTextArea();
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        scrollTexto.setBorder(BorderFactory.createTitledBorder("Contenido"));
        panelCentro.add(scrollTexto, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // ── Panel inferior: botones ──────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar      = new JButton("Guardar");
        btnEliminar     = new JButton("Eliminar");
        btnLimpiar      = new JButton("Limpiar");
        btnBorrarTodas  = new JButton("Borrar Todas");
        btnCerrarSesion = new JButton("Cerrar sesión");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnBorrarTodas);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // ── Cargar notas existentes al abrir ─────────────────────────────────────
        cargarListaNotas();

        // ── Al seleccionar una nota en la lista, cargar su contenido ─────────────
        listaNotas.addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                String nombreArchivo = listaNotas.getSelectedValue();
                if (nombreArchivo != null) {
                    cargarNota(nombreArchivo);
                }
            }
        });

        // ── Guardar nota ─────────────────────────────────────────────────────────
        btnGuardar.addActionListener(e -> {
            String titulo    = campoTitulo.getText().trim();
            String contenido = areaTexto.getText().trim();

            String error = Notas.guardarNota(user, titulo, contenido);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nota guardada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarListaNotas(); // refrescar la lista
            }
        });

        // ── Eliminar nota seleccionada ────────────────────────────────────────────
        btnEliminar.addActionListener(e -> {
            String notaSeleccionada = listaNotas.getSelectedValue();
            if (notaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una nota de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres borrar '" + notaSeleccionada + "'?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                Notas.eliminarNota(user, notaSeleccionada);
                limpiarFormulario();
                cargarListaNotas();
                JOptionPane.showMessageDialog(this, "Nota eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ── Limpiar formulario ────────────────────────────────────────────────────
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // ── Borrar todas las notas ────────────────────────────────────────────────
        btnBorrarTodas.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro? Se borrarán TODAS tus notas. Esta acción no se puede deshacer.",
                "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                Notas.eliminarTodasLasNotas(user);
                limpiarFormulario();
                cargarListaNotas();
                JOptionPane.showMessageDialog(this, "Todas las notas han sido eliminadas.", "Hecho", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ── Cerrar sesión: volver al login ────────────────────────────────────────
        btnCerrarSesion.addActionListener(e -> {
            new PantallaLogin().setVisible(true);
            dispose();
        });
    }

    private void cargarListaNotas() {
        modeloLista.clear();
        List<String> notas = Notas.listarNotas(user);
        for (String nombre : notas) {
            modeloLista.addElement(nombre);
        }
    }

    // Carga el contenido de una nota en el formulario
    private void cargarNota(String nombreArchivo) {
        String contenido = Notas.leerNota(user, nombreArchivo);
        if (contenido != null) {
            campoTitulo.setText(nombreArchivo.replace(".txt", ""));
            areaTexto.setText(contenido);
        } else {
            JOptionPane.showMessageDialog(this, "Error al abrir la nota.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        campoTitulo.setText("");
        areaTexto.setText("");
        listaNotas.clearSelection();
    }
}