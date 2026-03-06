package src;
import java.util.Scanner;
import static src.Notas.gestorNotas;
import static src.Usuario.menuLogin;
import static src.Usuario.registrarUsuario;
import static src.Usuario.usuarioLogin;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   BIENVENIDO AL SISTEMA DE NOTAS ║");
        System.out.println("╚══════════════════════════════════╝");

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
                    System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Error: introduce una opción válida (1-3).");
            }
        } while (opcion != 3);

        sc.close();
    }
}