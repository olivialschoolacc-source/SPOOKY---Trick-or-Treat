package Code;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class GameScreen {
    
    public static JPanel inGameMenuOverlay;

    public static void startInGame() {
        Main.layeredPane.removeAll();

        JPanel gamePanel = new JPanel();
        gamePanel.setBounds(0, 0, 800, 600);
        gamePanel.setLayout(null);

        JLabel gameLabel = new JLabel("In Game Page");
        gameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gameLabel.setBounds(150, 250, 500, 50);
        gamePanel.add(gameLabel);

        JButton menuButton = new JButton("MENU");
        menuButton.setBounds(10, 10, 90, 30);
        menuButton.addActionListener(e -> showInGameMenu());
        gamePanel.add(menuButton);

        Main.layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void showInGameMenu() {
        if (inGameMenuOverlay != null) {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
        }

        inGameMenuOverlay = new JPanel() {
            @Override 
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inGameMenuOverlay.setBounds(0,0,800,600);
        inGameMenuOverlay.setLayout(null);
        inGameMenuOverlay.setOpaque(false);
        inGameMenuOverlay.addMouseListener(new MouseAdapter() {});

        JPanel popupBox = new JPanel();
        popupBox.setBounds(200, 100, 400, 400);
        popupBox.setLayout(null);
        popupBox.setBackground(new Color(20, 10, 10));
        popupBox.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));

        JLabel titleLabel = new JLabel("GAME MENU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 100, 100));
        titleLabel.setBounds(50, 25, 300, 30);
        popupBox.add(titleLabel);

        JButton saveGameButton = new JButton("SAVE GAME");
        saveGameButton.setBounds(100, 90, 200, 45);
        saveGameButton.addActionListener(e -> {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
            showInGameSaveSlotsPopup();
        });
        popupBox.add(saveGameButton);

        JButton settingsButton = new JButton("SETTINGS");
        settingsButton.setBounds(100, 150, 200, 45);
        settingsButton.addActionListener(e -> {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
            showInGameSettingsPopup();
        });
        popupBox.add(settingsButton);

        JButton returnMenuButton = new JButton("RETURN TO MENU");
        returnMenuButton.setBounds(100, 210, 200, 45);
        returnMenuButton.addActionListener(e -> {
            if (GameState.hasUnsavedProgress) {
                showUnsavedProgressWarning();
            } else {
                exitToMainMenu();
            }
        });
        popupBox.add(returnMenuButton);

        JButton resumeButton = new JButton("RESUME");
        resumeButton.setBounds(100, 290, 200, 45);
        resumeButton.addActionListener(e -> {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
            Main.layeredPane.revalidate();
            Main.layeredPane.repaint();
        });
        popupBox.add(resumeButton);

        inGameMenuOverlay.add(popupBox);
        Main.layeredPane.add(inGameMenuOverlay, JLayeredPane.POPUP_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void showInGameSaveSlotsPopup() {
        if (inGameMenuOverlay != null) {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
        }

        inGameMenuOverlay = new JPanel() {
            @Override 
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0,0,0,180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inGameMenuOverlay.setBounds(0,0,800,600);
        inGameMenuOverlay.setLayout(null);
        inGameMenuOverlay.setOpaque(false);
        inGameMenuOverlay.addMouseListener(new MouseAdapter() {});

        JPanel popupBox = new JPanel();
        popupBox.setBounds(50, 40, 700, 520);
        popupBox.setLayout(null);
        popupBox.setBackground(new Color(20, 10, 10));

        JLabel popupLabel = new JLabel("SELECT SAVE SLOT TO SAVE", SwingConstants.CENTER);
        popupLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        popupLabel.setForeground(new Color(255, 100, 100));
        popupLabel.setBounds(50, 15, 600, 35);
        popupBox.add(popupLabel);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(635, 15, 45, 30);
        closeButton.addActionListener(e -> {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
            showInGameMenu();
        });
        popupBox.add(closeButton);

        JPanel slotsContainer = new JPanel();
        slotsContainer.setLayout(new GridLayout(5, 1, 0, 8));
        slotsContainer.setOpaque(false);

        for (int i = 0; i < 5; i++) {
            final int slotIndex = i;
            JPanel slotPanel = new JPanel();
            slotPanel.setLayout(null);
            slotPanel.setPreferredSize(new Dimension(640, 70));
            slotPanel.setBackground(new Color(30, 25, 30));
            slotPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 90, 100), 2));

            JLabel slotNum = new JLabel("SLOT " + (slotIndex + 1));
            slotNum.setForeground(Color.WHITE);
            slotNum.setFont(new Font("Monospaced", Font.BOLD, 14));
            slotNum.setBounds(20, 25, 80, 20);
            
            JLabel saveInfo = new JLabel();
            if (GameState.saveSlots[slotIndex]) {
                saveInfo.setText(GameState.saveDetails[slotIndex]);
                saveInfo.setForeground(new Color(100, 255, 100));
            } else {
                saveInfo.setText("--- EMPTY SLOT (NEW SAVE) ---");
                saveInfo.setForeground(Color.GRAY);
            }
            saveInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
            saveInfo.setBounds(150, 25, 300, 20);

            slotPanel.add(slotNum);
            slotPanel.add(saveInfo);

            slotPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    if (GameState.saveSlots[slotIndex]) {
                        // Overwrite confirmation prompt
                        MainMenu.showOverwriteConfirmation(slotIndex, popupBox, false);
                    } else {
                        // New save in empty slot
                        GameState.createNewGameInSlot(slotIndex);
                        Main.layeredPane.remove(inGameMenuOverlay);
                        inGameMenuOverlay = null;
                        Main.layeredPane.revalidate();
                        Main.layeredPane.repaint();
                        showInGameMenu();
                    }
                }
            });
            
            slotsContainer.add(slotPanel);
        }

        JScrollPane scrollPane = new JScrollPane(slotsContainer);
        scrollPane.setBounds(20, 65, 660, 430);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        popupBox.add(scrollPane);

        inGameMenuOverlay.add(popupBox);
        Main.layeredPane.add(inGameMenuOverlay, JLayeredPane.POPUP_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void showInGameSettingsPopup() {
        if (inGameMenuOverlay != null) {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
        }

        inGameMenuOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inGameMenuOverlay.setBounds(0, 0, 800, 600);
        inGameMenuOverlay.setLayout(null);
        inGameMenuOverlay.setOpaque(false);
        inGameMenuOverlay.addMouseListener(new MouseAdapter() {});

        JPanel popupBox = new JPanel();
        popupBox.setBounds(50, 40, 700, 520);
        popupBox.setLayout(null);
        popupBox.setBackground(new Color(20, 10, 10));

        JLabel popupLabel = new JLabel("SETTINGS", SwingConstants.CENTER);
        popupLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        popupLabel.setForeground(new Color(255, 100, 100));
        popupLabel.setBounds(50, 15, 600, 35);
        popupBox.add(popupLabel);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(635, 15, 45, 30);
        closeButton.addActionListener(e -> {
            Main.layeredPane.remove(inGameMenuOverlay);
            inGameMenuOverlay = null;
            showInGameMenu();
        });
        popupBox.add(closeButton);

        JLabel settingsContent = new JLabel("In-game settings controls go here...", SwingConstants.CENTER);
        settingsContent.setForeground(Color.WHITE);
        settingsContent.setBounds(100, 200, 500, 50);
        popupBox.add(settingsContent);

        inGameMenuOverlay.add(popupBox);
        Main.layeredPane.add(inGameMenuOverlay, JLayeredPane.POPUP_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void showUnsavedProgressWarning() {
        JPanel warningOverlay = new JPanel();
        warningOverlay.setBounds(150, 180, 500, 220);
        warningOverlay.setLayout(null);
        warningOverlay.setBackground(new Color(50, 10, 10));
        warningOverlay.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

        JLabel msg = new JLabel("UNSAVED PROGRESS WARNING", SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 18));
        msg.setForeground(Color.WHITE);
        msg.setBounds(50, 30, 400, 30);

        JLabel subMsg = new JLabel("You have unsaved progress. Leave anyway?", SwingConstants.CENTER);
        subMsg.setFont(new Font("Monospaced", Font.PLAIN, 14));
        subMsg.setForeground(Color.LIGHT_GRAY);
        subMsg.setBounds(50, 70, 400, 20);

        JButton yesButton = new JButton("YES");
        yesButton.setBounds(100, 130, 120, 40);
        yesButton.addActionListener(e -> {
            Main.layeredPane.remove(warningOverlay);
            if (inGameMenuOverlay != null) {
                Main.layeredPane.remove(inGameMenuOverlay);
                inGameMenuOverlay = null;
            }
            exitToMainMenu();
        });

        JButton noButton = new JButton("NO");
        noButton.setBounds(280, 130, 120, 40);
        noButton.addActionListener(e -> {
            Main.layeredPane.remove(warningOverlay);
            Main.layeredPane.revalidate();
            Main.layeredPane.repaint();
        });

        warningOverlay.add(msg);
        warningOverlay.add(subMsg);
        warningOverlay.add(yesButton);
        warningOverlay.add(noButton);

        Main.layeredPane.add(warningOverlay, JLayeredPane.DRAG_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void exitToMainMenu() {
        Main.layeredPane.removeAll();
        MainMenu.buildMainMenu();
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }
}
