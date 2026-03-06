package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import static src.Usuario.Error;
import static src.Usuario.pedirDato;

public class Notas {
    
    private static void renumerarNotas(String user) {
        File carpetaUsuario = new File("data/usuarios/" + user);
        File[] archivos = carpetaUsuario.listFiles();
        if (archivos == null) return;

        int contador = 1;
        for (File archivo : archivos) {
            if (archivo.getName().endsWith(".txt") && !archivo.getName().equals("pass.txt")) {
                String titulo = archivo.getName().substring(archivo.getName().indexOf('-') + 1);
                String nombreNuevo = contador + "-" + titulo;
                if (!archivo.getName().equals(nombreNuevo)) {
                    archivo.renameTo(new File(carpetaUsuario, nombreNuevo));
                }
                contador++;
            }
        }
    }

    public static void crearNota(String user, Scanner sc) {
        System.out.println("\n--- CREAR NUEVA NOTA ---");

        File carpetaUsuario = new File("data/usuarios/" + user);
        int contadorNotas = 0;
        File[] archivos = carpetaUsuario.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().endsWith(".txt") && !archivo.getName().equals("pass.txt")) {
                    contadorNotas++;
                }
            }
        }

        int numeroNuevaNota = contadorNotas + 1;
        String titulo = pedirDato("Dime el título de la nota (ej: compra): ", sc);
        String contenido = pedirDato("Escribe el contenido de la nota: ", sc);
        String nombreFinal = numeroNuevaNota + "-" + titulo + ".txt";
        File archivoNota = new File(carpetaUsuario, nombreFinal);

        try (FileWriter escritor = new FileWriter(archivoNota)) {
            escritor.write(contenido);
            System.out.println("✔ Se ha creado el fichero '" + nombreFinal + "' con éxito.");
        } catch (Exception e) {
            System.out.println("Error al crear el fichero: " + e.getMessage());
        }
    }

    public static void listarNotas(String user) {
        System.out.println("\n--- TUS FICHEROS DE NOTAS ---");
        File carpetaUsuario = new File("data/usuarios/" + user);
        File[] archivos = carpetaUsuario.listFiles();
        boolean hayNotas = false;

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().endsWith(".txt") && !archivo.getName().equals("pass.txt")) {
                    System.out.println(archivo.getName());
                    hayNotas = true;
                }
            }
        }
        if (!hayNotas) {
            System.out.println("Tu archivador está vacío.");
        }
    }

    public static void verNota(String user, Scanner sc) {
        System.out.println("\n--- VER NOTA ---");
        listarNotas(user);
        String numeroBuscado = pedirDato("Introduce el número de la nota: ", sc);
        boolean encontrada = false;
        File carpetaUsuario = new File("data/usuarios/" + user);
        File[] archivos = carpetaUsuario.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().startsWith(numeroBuscado + "-")) {
                    System.out.println("\n--- Leyendo: " + archivo.getName() + " ---");
                    encontrada = true;
                    try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        while ((linea = lector.readLine()) != null) {
                            System.out.println(linea);
                        }
                        System.out.println("----------------------------------");
                    } catch (Exception e) {
                        System.out.println("Error al leer la nota: " + e.getMessage());
                    }
                    break;
                }
            }
        }
        if (!encontrada) {
            System.out.println("Error: la nota número " + numeroBuscado + " no existe.");
        }
    }

    public static void editarNota(String user, Scanner sc) {
        System.out.println("\n--- EDITAR NOTA ---");
        listarNotas(user);
        String numeroBuscado = pedirDato("Introduce el número de la nota a editar: ", sc);
        boolean encontrada = false;
        File carpetaUsuario = new File("data/usuarios/" + user);
        File[] archivos = carpetaUsuario.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().startsWith(numeroBuscado + "-")) {
                    encontrada = true;
                    System.out.println("\nContenido actual:");
                    System.out.println("----------------------------------");
                    try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        while ((linea = lector.readLine()) != null) {
                            System.out.println(linea);
                        }
                    } catch (Exception e) {
                        System.out.println("Error al leer la nota: " + e.getMessage());
                        break;
                    }
                    System.out.println("----------------------------------");
                    String nuevoContenido = pedirDato("Escribe el nuevo contenido: ", sc);
                    try (FileWriter escritor = new FileWriter(archivo, false)) {
                        escritor.write(nuevoContenido);
                        System.out.println("✔ Nota '" + archivo.getName() + "' actualizada.");
                    } catch (Exception e) {
                        System.out.println("Error al guardar los cambios: " + e.getMessage());
                    }
                    break;
                }
            }
        }
        if (!encontrada) {
            System.out.println("Error: la nota número " + numeroBuscado + " no existe.");
        }
    }

    public static void borrarNota(String user, Scanner sc) {
        System.out.println("\n--- BORRAR NOTA ---");
        listarNotas(user);
        String numeroBuscado = pedirDato("\nIntroduce el número de la nota que quieres BORRAR: ", sc);
        boolean encontrada = false;
        File carpetaUsuario = new File("data/usuarios/" + user);
        File[] archivos = carpetaUsuario.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().startsWith(numeroBuscado + "-") && archivo.getName().endsWith(".txt")) {
                    encontrada = true;
                    String nombreArchivo = archivo.getName();
                    if (archivo.delete()) {
                        System.out.println("✔ La nota '" + nombreArchivo + "' ha sido eliminada.");
                        renumerarNotas(user);
                    } else {
                        System.out.println("Error: no se pudo eliminar el archivo.");
                    }
                    break;
                }
            }
        }
        if (!encontrada) {
            System.out.println("Error: la nota número " + numeroBuscado + " no existe.");
        }
    }

    public static int menuUsuario(String user, Scanner sc) {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║  NOTAS DE: " + String.format("%-19s", user) + "║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Crear nota               ║");
        System.out.println("║  2. Listar notas             ║");
        System.out.println("║  3. Ver nota                 ║");
        System.out.println("║  4. Editar nota              ║");
        System.out.println("║  5. Borrar nota              ║");
        System.out.println("║  6. Cerrar sesión            ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Elige una opción: ");
        int opcion = Error(sc);
        sc.nextLine();
        return opcion;
    }

    public static void gestorNotas(String user, Scanner sc) {
        int opcion;
        do {
            opcion = menuUsuario(user, sc);
            switch (opcion) {
                case 1: crearNota(user, sc);  break;
                case 2: listarNotas(user);    break;
                case 3: verNota(user, sc);    break;
                case 4: editarNota(user, sc); break;
                case 5: borrarNota(user, sc); break;
                case 6:
                    System.out.println("Sesión cerrada. ¡Hasta pronto, " + user + "!");
                    break;
                default:
                    System.out.println("Opción inválida. Usa un número del 1 al 6.");
            }
        } while (opcion != 6);
    }
}