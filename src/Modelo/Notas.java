package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Notas {

    // ── Ruta base de notas del usuario ───────────────────────────────────────────
    private static File carpetaNotas(String user) {
        return new File("data/usuarios/" + user + "/notas");
    }

    // ── Listar nombres de ficheros de notas ──────────────────────────────────────
    public static List<String> listarNotas(String user) {
        List<String> nombres = new ArrayList<>();
        File[] archivos = carpetaNotas(user).listFiles();
        if (archivos != null) {
            for (File f : archivos) {
                if (f.getName().endsWith(".txt")) {
                    nombres.add(f.getName());
                }
            }
        }
        return nombres;
    }

    // ── Guardar (crear o sobreescribir) una nota ─────────────────────────────────
    // Devuelve null si va bien, o mensaje de error si falla
    public static String guardarNota(String user, String titulo, String contenido) {
        if (titulo == null || titulo.trim().isEmpty()) return "El título no puede estar vacío.";
        if (contenido == null || contenido.trim().isEmpty()) return "El contenido no puede estar vacío.";

        File carpeta = carpetaNotas(user);
        carpeta.mkdirs(); // por si acaso no existe todavía

        File nota = new File(carpeta, titulo + ".txt");
        try (FileWriter escritor = new FileWriter(nota)) {
            escritor.write(contenido);
            return null; // éxito
        } catch (Exception e) {
            return "Error al guardar: " + e.getMessage();
        }
    }

    // ── Leer el contenido de una nota ────────────────────────────────────────────
    // Devuelve el contenido como String, o null si no existe
    public static String leerNota(String user, String nombreArchivo) {
        File nota = new File(carpetaNotas(user), nombreArchivo);
        if (!nota.exists()) return null;

        StringBuilder sb = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(new FileReader(nota))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }

    // ── Eliminar una nota ────────────────────────────────────────────────────────
    public static boolean eliminarNota(String user, String nombreArchivo) {
        File nota = new File(carpetaNotas(user), nombreArchivo);
        return nota.exists() && nota.delete();
    }

    // ── Eliminar todas las notas del usuario ─────────────────────────────────────
    public static void eliminarTodasLasNotas(String user) {
        File[] archivos = carpetaNotas(user).listFiles();
        if (archivos != null) {
            for (File f : archivos) {
                if (f.getName().endsWith(".txt")) f.delete();
            }
        }
    }
}