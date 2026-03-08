package App;

import Vista.PantallaLogin;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin ventana = new PantallaLogin();
            ventana.setVisible(true);
        });
    }
}