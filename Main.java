import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

    public static int Error(Scanner sc) {
        do {
            try {
            return sc.nextInt();
            } catch (Exception e) {
            System.out.println("Error: introduce una opción valida: ");
            sc.nextLine();
            }
        } while (true);
    }

    public static String pedirDato(String mensaje, Scanner sc) {
        System.out.print(mensaje);
        return sc.nextLine(); 
    }

    public static void registrarUsuario(Scanner sc) {
        String user = pedirDato("Introduce el usuario: ", sc);
        String password = pedirDato("Introduce la contraseña: ", sc);

        File carpetaUsuario = new File("data/usuarios/" + user);

        if (carpetaUsuario.exists()) {
            System.out.println("Error: Este usuario ya esta registrado");
        }
        else {
            carpetaUsuario.mkdirs();
            File archivoContrasena = new File(carpetaUsuario, "pass.txt");
            try (FileWriter escritor = new FileWriter(archivoContrasena)){
                escritor.write(password);
                System.out.println("Usuario: "+ user + " esta registrado");
            } catch (Exception e) {
                System.out.println("Error al guardar la contraseña "+ e.getMessage());
            }
        }
    }

    public static String usuarioLogin(Scanner sc) {
        System.out.println("--- INICIO DE SESION ---");
        
        String user = pedirDato("Introduce el usuario: ", sc);
        String password = pedirDato("Introduce la contraseña: ", sc);

        File ruta = new File("data/usuarios/"+ user);

        if (ruta.exists()) {
            File archivoPass = new File(ruta, "pass.txt");
            try (BufferedReader lector = new BufferedReader(new FileReader(archivoPass))) {
                String passGuard = lector.readLine();
                if (passGuard.equals(password)) {
                    System.out.println("--- Bienvenido "+ user);
                    return user;
                }
                else {
                    System.out.println("contraseña incorrecta");
                }

            } catch (Exception e) {
                System.out.println("Error al leer la contraseña: " + e.getMessage());
            }
        }
        else {
            System.out.println("Error: el usuario no existe");
        }
        return null;
    }

    public static void crearNota(String user, Scanner sc) {
        System.out.println("\n--- CREAR NUEVO FICHERO ---");
        
        String titulo = pedirDato("Dime el título de la nota (ej: compra): ", sc);
        String contenido = pedirDato("Escribe el contenido de la nota: ", sc);
        File archivoNota = new File("data/usuarios/" + user, titulo + ".txt");

        try (FileWriter escritor = new FileWriter(archivoNota)) {
            escritor.write(contenido);
            System.out.println("Se ha creado el fichero '" + titulo + ".txt' con éxito.");
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
                    System.out.println("! " + archivo.getName());
                    hayNotas = true;
                }
            }
        }

        if (!hayNotas) {
            System.out.println("Tu archivador está vacío.");
        }
    }

    public static void gestorNotas(String user, Scanner sc) {
        int opcion;
        do {
            opcion = menuUsuario(user, sc); 
            
            switch (opcion) {
                case 1:
                    crearNota(user, sc);
                    break;
                case 2:
                    listarNotas(user);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("🔒 Cerrando sesión de " + user + "...");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Usa un número del 1 al 5.");
            }
        } while (opcion != 5);
    }

    public static int  menuUsuario(String user, Scanner sc) {
        int opcion;
        System.out.println("1. Crear nota");
        System.out.println("2. Leer nota");
        System.out.println("3. Ver nota");
        System.out.println("4. Borrar nota ");
        System.out.println("5 Cerrar sesion");
        System.out.println("Introduce una opcion valida: ");
        opcion = Error(sc);
        sc.nextLine();
        return opcion;
    }

    public static int menuLogin(Scanner sc) {
        int opcion;
        System.out.println("1. Iniciar Sesion");
        System.out.println("2. Resgistrarse");
        System.out.println("3 Salir");
        System.out.println("Introduce una opcion valida: ");
        opcion = Error(sc);
        sc.nextLine();
        return opcion;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            opcion = menuLogin(sc);

            switch (opcion) {
                case 1:
                    String usuarioLogueado = usuarioLogin(sc); 
                    if (usuarioLogueado != null) {
                        gestorNotas(usuarioLogueado, sc);
                    }
                    break;
                case 2:
                    registrarUsuario(sc);
                    break;
                case 3:
                    System.out.println("--> Saliendo del sistema. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Error: Por favor, introduce una opción válida (1-3).");
            }
        } while (opcion != 3);
    }
}