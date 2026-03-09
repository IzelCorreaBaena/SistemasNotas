package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;

public class Usuario {

    public static String encriptar(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error al encriptar la contraseña", ex);
        }
    }

    public static String registrar(String user, String password) {
        if (user == null || user.trim().isEmpty()) {
            return "El nombre de usuario no puede estar vacío.";
        }
        if (!user.matches("[a-zA-Z0-9_]+")) {
            return "El usuario solo puede tener letras, números y '_'.";
        }
        if (password == null || password.length() < 4) {
            return "La contraseña debe tener mínimo 4 caracteres.";
        }

        File carpetaUsuario = new File("data/usuarios/" + user);
        if (carpetaUsuario.exists()) {
            return "Este usuario ya está registrado.";
        }

        new File("data/usuarios/" + user + "/notas").mkdirs();

        File archivoPass = new File(carpetaUsuario, "pass.txt");
        try (FileWriter escritor = new FileWriter(archivoPass)) {
            escritor.write(encriptar(password));
            return null; 
        } catch (Exception e) {
            return "Error al guardar contraseña: " + e.getMessage();
        }
    }

    public static String login(String user, String password) {
        if (user == null || user.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        File carpetaUsuario = new File("data/usuarios/" + user);
        if (!carpetaUsuario.exists()) return null;

        File archivoPass = new File(carpetaUsuario, "pass.txt");
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoPass))) {
            String passGuardada = lector.readLine();
            if (passGuardada != null && passGuardada.equals(encriptar(password))) {
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}