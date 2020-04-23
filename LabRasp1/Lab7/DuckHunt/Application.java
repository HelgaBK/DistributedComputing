package Lab7;

import javax.swing.*;

public class Application extends JFrame {
    public Application() {
        initUI();
    }

    private void initUI() {
        add(new GamePanel());

        setResizable(false);
        pack();

        setTitle("Duck Hunt");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        Application DuckHunt = new Application();
        DuckHunt.setVisible(true);
    }
}
