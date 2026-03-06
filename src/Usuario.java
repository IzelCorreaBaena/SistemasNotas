package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.util.Scanner;

public class Usuario {

    public static int Error(Scanner sc) {
        do {
            try {
                return sc.nextInt();
            } catch (Exception e) {
                System.out.println("Error: introduce una opción válida (número): ");
                sc.nextLine();
            }
        } while (true);
    }

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

    public static String pedirDato(String mensaje, Scanner sc) {
        String dato;
        do {
            System.out.print(mensaje);
            dato = sc.nextLine().trim();
            if (dato.isEmpty()) {
                System.out.println("Error: el campo no puede estar vacío.");
            }
        } while (dato.isEmpty());
        return dato;
    }

    public static void registrarUsuario(Scanner sc) {
        System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
        String user = pedirDato("Introduce el nombre de usuario: ", sc);
        if (!user.matches("[a-zA-Z0-9_]+")) {
            System.out.println("Error: el nombre de usuario solo puede contener letras, números y '_'.");
            return;
        }

        String password = pedirDato("Introduce la contraseña (mínimo 4 caracteres): ", sc);
        if (password.length() < 4) {
            System.out.println("Error: la contraseña debe tener al menos 4 caracteres.");
            return;
        }

        String passwordEncriptada = encriptar(password);
        File carpetaUsuario = new File("data/usuarios/" + user);

        if (carpetaUsuario.exists()) {
            System.out.println("Error: este usuario ya está registrado.");
        } else {
            carpetaUsuario.mkdirs();
            File archivoContrasena = new File(carpetaUsuario, "pass.txt");
            try (FileWriter escritor = new FileWriter(archivoContrasena)) {
                escritor.write(passwordEncriptada);
                System.out.println("Usuario '" + user + "' registrado con éxito.");
            } catch (Exception e) {
                System.out.println("Error al guardar la contraseña: " + e.getMessage());
            }
        }
    }

    public static String usuarioLogin(Scanner sc) {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        String user = pedirDato("Introduce el usuario: ", sc);
        String password = pedirDato("Introduce la contraseña: ", sc);
        String intentoEncriptado = encriptar(password);

        File ruta = new File("data/usuarios/" + user);

        if (ruta.exists()) {
            File archivoPass = new File(ruta, "pass.txt");
            try (BufferedReader lector = new BufferedReader(new FileReader(archivoPass))) {
                String passGuard = lector.readLine();
                if (passGuard != null && passGuard.equals(intentoEncriptado)) {
                    System.out.println("Bienvenido/a, " + user + "!");
                    return user;
                } else {
                    System.out.println("Error: contraseña incorrecta.");
                }
            } catch (Exception e) {
                System.out.println("Error al leer la contraseña: " + e.getMessage());
            }
        } else {
            System.out.println("Error: el usuario '" + user + "' no existe.");
        }
        return null;
    }

    public static int menuLogin(Scanner sc) {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║   SISTEMA DE NOTAS - MENÚ    ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Iniciar sesión           ║");
        System.out.println("║  2. Registrarse              ║");
        System.out.println("║  3. Salir                    ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Elige una opción: ");
        int opcion = Error(sc);
        sc.nextLine();
        return opcion;
    }
}