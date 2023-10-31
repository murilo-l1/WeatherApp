import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String[] args) {
        // utilizando um metodo que garante segurança na execucao da thread ao inicializar a GUI, implementada a interface Runnable
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherAppGui().setVisible(true);
            }

        });
    }
}