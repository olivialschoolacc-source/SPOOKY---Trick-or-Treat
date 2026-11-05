package Code;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MainMenu {
    
    public static JPanel menuPanel;
    public static JPanel popupOverlay;
    public static JPanel subPopupOverlay;

    public static void buildMainMenu() {
        menuPanel = new JPanel();
        menuPanel.setBounds(0, 0, 800, 600);
        menuPanel.setLayout(null);
        menuPanel.setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("SPOOKY!! - Trick or Treat");
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setBounds(10, 150, 750, 50);

        JButton newButton = new JButton("NEW GAME");
        newButton.setBounds(10, 300, 200, 50);
        newButton.addActionListener(e -> showPopup("NEW GAME"));

        JButton loadButton = new JButton("LOAD GAME");
        loadButton.setBounds(10, 360, 200, 50);
        loadButton.addActionListener(e -> showPopup("LOAD GAME"));

        JButton settingsButton = new JButton("SETTINGS");
        settingsButton.setBounds(10, 420, 200, 50);
        settingsButton.addActionListener(e -> showPopup("SETTINGS"));

        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(10, 480, 200, 50);
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(title);
        menuPanel.add(newButton);
        menuPanel.add(loadButton);
        menuPanel.add(settingsButton);
        menuPanel.add(exitButton);

        Main.layeredPane.add(menuPanel, JLayeredPane.DEFAULT_LAYER);
    }

    public static void showPopup(String type) {
        if (popupOverlay != null) {
            Main.layeredPane.remove(popupOverlay);
            popupOverlay = null;
        }

        popupOverlay = new JPanel() {
            @Override 
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        popupOverlay.setBounds(0, 0, 800, 600);
        popupOverlay.setLayout(null);
        popupOverlay.setOpaque(false);
        popupOverlay.addMouseListener(new MouseAdapter() {});
        popupOverlay.addMouseMotionListener(new MouseAdapter() {});

        JPanel popupBox = new JPanel();
        popupBox.setBounds(50, 40, 700, 520);
        popupBox.setLayout(null);
        popupBox.setBackground(new Color(20, 10, 10));

        String headerTitle;
        if (type.equals("NEW GAME")) {
            headerTitle = "SELECT SAVE SLOT FOR NEW GAME";
        } else if (type.equals("LOAD GAME")) {
            headerTitle = "LOAD GAME";
        } else {
            headerTitle = "SETTINGS";
        }

        JLabel popupLabel = new JLabel(headerTitle, SwingConstants.CENTER);
        popupLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        popupLabel.setForeground(new Color(255, 100, 100));
        popupLabel.setBounds(50, 15, 600, 35);
        popupBox.add(popupLabel);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(635, 15, 45, 30);
        closeButton.addActionListener(e -> {
            Main.layeredPane.remove(popupOverlay);
            popupOverlay = null;
            Main.layeredPane.revalidate();
            Main.layeredPane.repaint();
        });
        popupBox.add(closeButton);

        if (type.equals("NEW GAME") || type.equals("LOAD GAME")) {
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

                JLabel slotNum = new JLabel();
                slotNum.setForeground(Color.WHITE);
                slotNum.setFont(new Font("Monospaced", Font.BOLD, 14));
                slotNum.setBounds(20, 25, 80, 20);

                JLabel saveInfo = new JLabel();
                if (GameState.saveSlots[slotIndex]) {
                    saveInfo.setText(GameState.saveDetails[slotIndex]);
                    saveInfo.setForeground(new Color(100, 255, 100));
                } else {
                    saveInfo.setText("--- Empty Slot ---");
                    saveInfo.setForeground(Color.GRAY);
                }
                saveInfo.setFont(new Font("Monospaced", Font.BOLD, 14));
                saveInfo.setBounds(150, 25, 300, 20);

                slotPanel.add(slotNum);
                slotPanel.add(saveInfo);

                if (type.equals("LOAD GAME") && GameState.saveSlots[slotIndex]) {
                    JButton deleteButton = new JButton("DELETE");
                    deleteButton.setBounds(520, 20, 95, 30);
                    deleteButton.setFont(new Font("Monospaced", Font.BOLD, 12));
                    deleteButton.addActionListener(e -> showDeleteConfirmation(slotIndex, type));
                    slotPanel.add(deleteButton);
                }

                slotPanel.addMouseListener (new MouseAdapter() {
                    @Override 
                    public void mouseClicked(MouseEvent evt) {
                        if (type.equals("NEW GAME")) {
                            if (GameState.saveSlots[slotIndex]) {
                                showOverwriteConfirmation(slotIndex, popupBox, true);
                            } else {
                                createNewGameInSlotFlow(slotIndex, popupBox);
                            }
                        } else {
                            if (GameState.saveSlots[slotIndex]) {
                                Main.layeredPane.remove(popupOverlay);
                                popupOverlay = null;
                                GameScreen.startInGame();
                            }
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
        } else if (type.equals("SETTINGS")) {
            JLabel settingsContent = new JLabel("Settings controls go here...", SwingConstants.CENTER);
            settingsContent.setForeground(Color.WHITE);
            settingsContent.setBounds(100, 200, 500, 50);
            popupBox.add(settingsContent);
        }

        popupOverlay.add(popupBox);
        Main.layeredPane.add(popupOverlay, JLayeredPane.POPUP_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void showOverwriteConfirmation(int slotIndex, JPanel popupBox, boolean isNewGame) {
        if (subPopupOverlay != null) Main.layeredPane.remove(subPopupOverlay);

        subPopupOverlay = new JPanel();
        subPopupOverlay.setBounds(150, 180, 500, 220);
        subPopupOverlay.setLayout(null);
        subPopupOverlay.setBackground(new Color(50, 10, 10));
        subPopupOverlay.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

        JLabel msg = new JLabel("OVERWRITE SAVE FILE?", SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 18));
        msg.setForeground(Color.WHITE);
        msg.setBounds(50, 30, 400, 30);

        JLabel subMsg = new JLabel("Slot " + (slotIndex + 1) + " already has data. Proceed?", SwingConstants.CENTER);
        subMsg.setFont(new Font("Monospaced", Font.PLAIN, 14));
        subMsg.setForeground(Color.LIGHT_GRAY);
        subMsg.setBounds(50, 70, 400, 20);

        JButton yesButton = new JButton("YES");
        yesButton.setBounds(100, 130, 120, 40);
        yesButton.addActionListener(e -> {
            Main.layeredPane.remove(subPopupOverlay);
            subPopupOverlay = null;
            if (isNewGame) {
                createNewGameInSlotFlow(slotIndex, popupBox);
            } else {
                GameState.createNewGameInSlot(slotIndex);
                Main.layeredPane.remove(popupOverlay);
                popupOverlay = null;
                Main.layeredPane.revalidate();
                Main.layeredPane.repaint();
                GameScreen.showInGameMenu();
            }
        });

        JButton noButton = new JButton("NO");
        noButton.setBounds(280, 130, 120, 40);
        noButton.addActionListener(e -> {
            Main.layeredPane.remove(subPopupOverlay);
            subPopupOverlay = null;
            Main.layeredPane.revalidate();
            Main.layeredPane.repaint();
        });

        subPopupOverlay.add(msg);
        subPopupOverlay.add(subMsg);
        subPopupOverlay.add(yesButton);
        subPopupOverlay.add(noButton);

        Main.layeredPane.add(subPopupOverlay, JLayeredPane.DRAG_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void showDeleteConfirmation(int slotIndex, String parentType) {
        if (subPopupOverlay != null) Main.layeredPane.remove(subPopupOverlay);

        subPopupOverlay = new JPanel();
        subPopupOverlay.setBounds(150, 100, 500, 220);
        subPopupOverlay.setLayout(null);
        subPopupOverlay.setBackground(new Color(50, 10, 10));
        subPopupOverlay.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

        JLabel msg = new JLabel("DELETE SAVE FILE?");
        msg.setFont(new Font("Monospaced", Font.BOLD, 18));
        msg.setForeground(Color.WHITE);
        msg.setBounds(50, 30, 400, 30);
        
        JLabel subMsg = new JLabel("Are you sure you want to delete Slot " + (slotIndex + 1) + "?", SwingConstants.CENTER);
        subMsg.setFont(new Font("Monospaced", Font.PLAIN, 14));
        subMsg.setForeground(Color.LIGHT_GRAY);
        subMsg.setBounds(50, 70, 400, 20);

        JButton yesButton = new JButton("YES");
        yesButton.setBounds(100, 130, 120, 40);
        yesButton.addActionListener(e -> {
            GameState.deleteSlot(slotIndex);
            Main.layeredPane.remove(subPopupOverlay);
            subPopupOverlay = null;
            showPopup(parentType);
        });

        JButton noButton = new JButton("NO");
        noButton.setBounds(280, 130, 120, 40);
        noButton.addActionListener(e -> {
            Main.layeredPane.remove(subPopupOverlay);
            subPopupOverlay = null;
            Main.layeredPane.revalidate();
            Main.layeredPane.repaint();
        });

        subPopupOverlay.add(msg);
        subPopupOverlay.add(subMsg);
        subPopupOverlay.add(yesButton);
        subPopupOverlay.add(noButton);

        Main.layeredPane.add(subPopupOverlay, JLayeredPane.DRAG_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    public static void createNewGameInSlotFlow(int slotIndex, JPanel popupBox) {
        GameState.createNewGameInSlot(slotIndex);

        popupBox.removeAll();

        JLabel madeLabel = new JLabel("SAVE FILE MADE");
        madeLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        madeLabel.setForeground(Color.GREEN);
        madeLabel.setBounds(100, 220, 500, 60);

        popupBox.revalidate();
        popupBox.repaint();

        Timer delayTimer = new Timer(1000, timerEvt -> {
            ((Timer)timerEvt.getSource()).stop();
            Main.layeredPane.remove(popupOverlay);
            popupOverlay = null;
            GameScreen.startInGame();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }
}
