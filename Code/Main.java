package Code;

import java.awt.*;
import javax.swing.*;

public class Main {

    public static String page = "START";
    public static JFrame frame;
    public static JButton newButton;
    public static JButton loadButton;
    public static JButton settingsButton;
    public static JButton backButton;
    public static JButton exitButton;
    public static JLabel title;

    public static String lastPage = "";

    public static void main(String[] args) {
        frame = new JFrame("SPOOKY!! - Trick or Treat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);

        callPage();

        frame.setVisible(true);

        // call update every 16 milliseconds (approximately 60 frames per second)
        Timer timer = new Timer(16, e -> update());
        timer.start();
    }

    public static void callPage() {

        frame.getContentPane().removeAll();

        if (!page.equals("START")) {
            backButton = new JButton("<");
            backButton.setBounds(10, 10, 50, 30);
            backButton.addActionListener(e -> page = "START");
            backButton.setVisible(true);
            frame.add(backButton);
        }
        
        if (page.equals("START")) {
            title = new JLabel("SPOOKY! - Trick or Treat");
            title.setFont(new Font("Arial", Font.BOLD, 50));
            title.setBounds(10, 150, 750, 50);

            newButton = new JButton("NEW GAME");
            newButton.setBounds(10, 300, 200, 50);
            newButton.addActionListener(e -> page = "IN_GAME");
            newButton.setVisible(true);

            loadButton = new JButton("LOAD GAME");
            loadButton.setBounds(10, 360, 200, 50);
            loadButton.addActionListener(e -> page = "IN_GAME");
            loadButton.setVisible(true);

            settingsButton = new JButton("SETTINGS");
            settingsButton.setBounds(10, 420, 200, 50);
            settingsButton.addActionListener(e -> page = "SETTINGS");
            settingsButton.setVisible(true);

            exitButton = new JButton("EXIT");
            exitButton.setBounds(10, 480, 200, 50);
            exitButton.addActionListener(e -> System.exit(0));
            exitButton.setVisible(true);
            
            frame.add(title);
            frame.add(newButton);
            frame.add(loadButton);
            frame.add(settingsButton);
            frame.add(exitButton);
        } else if (page.equals("IN_GAME")) {
            // Add your in-game components here
            JLabel label = new JLabel("In Game Page");
            label.setBounds(10, 50, 200, 50);
            frame.add(label);
        }
        
        frame.revalidate();
        frame.repaint();
    }

    public static void update() {
        if (!page.equals(lastPage)) {
            lastPage = page;
            callPage();
        }

        if (frame != null) {
            frame.revalidate();
            frame.repaint();
        }
    }
}
