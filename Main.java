package Code;

import javax.swing.*;

public class Main {

    public static JFrame frame;
    public static JLayeredPane layeredPane;

    public static void main(String[] args) {
        frame = new JFrame("SPOOKY!! - Trick or Treat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 800, 600);
        frame.setContentPane(layeredPane);

        GameState.initSaves();
        MainMenu.buildMainMenu();
        frame.setVisible(true);
        
        Timer timer = new Timer (16, e -> {
            // game loop logic goes here
        });
        timer.start();
    }
}