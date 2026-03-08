package Vista;

import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class PantallaLogin extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton btnLogin, btnRegistrar;

    public PantallaLogin() {
        setTitle("Sistema de Notas - Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrar en pantalla
        setResizable(false);
        setLayout(new BorderLayout());

        // ── Panel del formulario ─────────────────────────────────────────────────
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panelFormulario.add(new JLabel("Usuario:"));
        campoUsuario = new JTextField();
        panelFormulario.add(campoUsuario);

        panelFormulario.add(new JLabel("Contraseña:"));
        campoPassword = new JPasswordField();
        panelFormulario.add(campoPassword);

        add(panelFormulario, BorderLayout.CENTER);

        // ── Panel de botones ─────────────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnLogin = new JButton("Iniciar sesión");
        btnRegistrar = new JButton("Registrarse");
        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistrar);
        add(panelBotones, BorderLayout.SOUTH);

        // ── Acciones ─────────────────────────────────────────────────────────────
        btnLogin.addActionListener(e -> accionLogin());
        btnRegistrar.addActionListener(e -> accionRegistrar());

        // Permitir pulsar Enter en el campo contraseña para hacer login
        campoPassword.addActionListener(e -> accionLogin());
    }

    private void accionLogin() {
        String user = campoUsuario.getText().trim();
        String pass = new String(campoPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellena usuario y contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String usuarioLogueado = Usuario.login(user, pass);
        if (usuarioLogueado != null) {
            // Abrir pantalla de notas y cerrar esta ventana
            PantallaNotas pantallaNotas = new PantallaNotas(usuarioLogueado);
            pantallaNotas.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            campoPassword.setText("");
        }
    }

    private void accionRegistrar() {
        String user = campoUsuario.getText().trim();
        String pass = new String(campoPassword.getPassword());

        String error = Usuario.registrar(user, pass);
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Usuario '" + user + "' registrado con éxito.\nYa puedes iniciar sesión.", "Registro OK", JOptionPane.INFORMATION_MESSAGE);
            campoPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this, error, "Error en el registro", JOptionPane.ERROR_MESSAGE);
        }
    }
} 
