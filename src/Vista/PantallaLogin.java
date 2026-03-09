package Vista;

import Modelo.Usuario;
import java.awt.*;
import javax.swing.*;

public class PantallaLogin extends JFrame {

    private static final Color FONDO       = new Color(35,  35,  35);
    private static final Color FONDO_CAMPO = new Color(55,  55,  55);
    private static final Color TEXTO_CLARO = new Color(240, 240, 240);
    private static final Color TEXTO_GRIS  = new Color(160, 160, 160);
    private static final Color AMARILLO    = new Color(255, 214, 10);
    private static final Color GRIS_BTN    = new Color(70,  70,  70);
    private static final Color BORDE_CAMPO = new Color(80,  80,  80);

    private static final String TEXTO_USUARIO    = "Usuario";
    private static final String TEXTO_CONTRASENA = "Contrasena";

    private JTextField     campoUsuario;
    private JPasswordField campoContrasena;
    private JButton        btnEntrar;
    private JButton        btnRegistrar;

    public PantallaLogin() {
        setTitle("Notas - Iniciar sesion");
        setSize(400, 380);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        construirFormulario();
    }

    private void construirFormulario() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        JLabel etiquetaTitulo = new JLabel("Notas", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Dialog", Font.BOLD, 26));
        etiquetaTitulo.setForeground(AMARILLO);
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaSubtitulo = new JLabel("Inicia sesion o registrate", SwingConstants.CENTER);
        etiquetaSubtitulo.setFont(new Font("Dialog", Font.PLAIN, 12));
        etiquetaSubtitulo.setForeground(TEXTO_GRIS);
        etiquetaSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoUsuario    = crearCampoTexto(TEXTO_USUARIO);
        campoContrasena = crearCampoContrasena(TEXTO_CONTRASENA);

        btnEntrar    = crearBoton("Iniciar sesion", AMARILLO,  Color.BLACK);
        btnRegistrar = crearBoton("Registrarse",    GRIS_BTN,  TEXTO_CLARO);
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panel.add(etiquetaTitulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(etiquetaSubtitulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(campoUsuario);
        panel.add(Box.createVerticalStrut(10));
        panel.add(campoContrasena);
        panel.add(Box.createVerticalStrut(18));
        panel.add(btnEntrar);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnRegistrar);

        btnEntrar.addActionListener(e -> accionLogin());
        btnRegistrar.addActionListener(e -> accionRegistrar());
        campoContrasena.addActionListener(e -> accionLogin());

        add(panel, BorderLayout.CENTER);
    }

    private void accionLogin() {
        String usuario    = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());
        if (usuario.isEmpty() || usuario.equals(TEXTO_USUARIO) ||
            contrasena.isEmpty() || contrasena.equals(TEXTO_CONTRASENA)) {
            JOptionPane.showMessageDialog(this, "Rellena usuario y contrasena.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String usuarioLogueado = Usuario.login(usuario, contrasena);
        if (usuarioLogueado != null) {
            new PantallaNotas(usuarioLogueado).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contrasena incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            campoContrasena.setText("");
        }
    }


    private void accionRegistrar() {
        String usuario    = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        if (usuario.isEmpty() || usuario.equals(TEXTO_USUARIO) ||
            contrasena.isEmpty() || contrasena.equals(TEXTO_CONTRASENA)) {
            JOptionPane.showMessageDialog(this, "Rellena usuario y contrasena.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String error = Usuario.registrar(usuario, contrasena);
        if (error == null) {
            JOptionPane.showMessageDialog(this,
                "Usuario '" + usuario + "' registrado.\nYa puedes iniciar sesion.",
                "Registro OK", JOptionPane.INFORMATION_MESSAGE);
            campoContrasena.setText("");
        } else {
            JOptionPane.showMessageDialog(this, error, "Error en el registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField crearCampoTexto(String textoAyuda) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Dialog", Font.PLAIN, 14));
        campo.setBackground(FONDO_CAMPO);
        campo.setCaretColor(AMARILLO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_CAMPO),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campo.setText(textoAyuda);
        campo.setForeground(TEXTO_GRIS);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(textoAyuda)) {
                    campo.setText("");
                    campo.setForeground(TEXTO_CLARO);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(textoAyuda);
                    campo.setForeground(TEXTO_GRIS);
                }
            }
        });
        return campo;
    }


    private JPasswordField crearCampoContrasena(String textoAyuda) {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Dialog", Font.PLAIN, 14));
        campo.setBackground(FONDO_CAMPO);
        campo.setCaretColor(AMARILLO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_CAMPO),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        campo.setEchoChar((char) 0);
        campo.setText(textoAyuda);
        campo.setForeground(TEXTO_GRIS);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(campo.getPassword()).equals(textoAyuda)) {
                    campo.setText("");
                    campo.setForeground(TEXTO_CLARO);
                    campo.setEchoChar('*');
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(campo.getPassword()).isEmpty()) {
                    campo.setEchoChar((char) 0);
                    campo.setText(textoAyuda);
                    campo.setForeground(TEXTO_GRIS);
                }
            }
        });
        return campo;
    }


    private JButton crearBoton(String texto, Color fondo, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Dialog", Font.BOLD, 13));
        boton.setBackground(fondo);
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}