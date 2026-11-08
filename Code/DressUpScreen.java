package Code;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class DressUpScreen {

    public static JPanel dressUpPanel;
    public static JPanel characterPreviewPanel;
    public static JTabbedPane categoryTabs;

    public static List<DressItem> availableItems = new ArrayList<>();
    public static Map<DressItem.Layer, DressItem> equippedItems = new EnumMap<>(DressItem.Layer.class);

    public static void startDressUpScene() {
        Main.layeredPane.removeAll();
        initItemDatabase();

        dressUpPanel = new JPanel();
        dressUpPanel.setBounds(0, 0, 800, 600);
        dressUpPanel.setLayout(null);
        dressUpPanel.setBackground(new Color(15, 10, 20));

        JPanel leftStagePanel = new JPanel();
        leftStagePanel.setBounds(0, 0, 533, 600);
        leftStagePanel.setLayout(null);
        leftStagePanel.setBackground(new Color(25, 18, 30));
        leftStagePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(180, 50, 50)));

        JLabel titleLabel = new JLabel("OCTOBER 31st - DRESS MARNIE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255, 120, 120));
        titleLabel.setBounds(10, 15, 513, 25);
        leftStagePanel.add(titleLabel);

        characterPreviewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                int boxWidth = 120;
                int boxHeight = 220;
                int boxX = cx - (boxWidth / 2);
                int boxY = cy - (boxHeight / 2);

                g2d.setColor(new Color(10, 5, 15, 180));
                g2d.fillOval(cx - 60, boxY + boxHeight + 10, 120, 20);

                g2d.setColor(new Color(50, 40, 60));
                g2d.fillRect(boxX, boxY, boxWidth, boxHeight);
                g2d.setColor(new Color(120, 100, 140));
                g2d.drawRect(boxX, boxY, boxWidth, boxHeight);

                g2d.setColor(new Color(180, 160, 200));
                g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
                g2d.drawString("MARNIE", cx - 30, boxY + 30);

                drawEquippedLayer(g2d, cx, cy, DressItem.Layer.BASE_COSTUMES);
                drawEquippedLayer(g2d, cx, cy, DressItem.Layer.FOOTWEAR);
                drawEquippedLayer(g2d, cx, cy, DressItem.Layer.OUTERWEAR);
                drawEquippedLayer(g2d, cx, cy, DressItem.Layer.HEADWEAR);
                drawEquippedLayer(g2d, cx, cy, DressItem.Layer.MASK);
                drawEquippedLayer(g2d, cx, cy, DressItem.Layer.HAND_PROP);
            }
        };
        characterPreviewPanel.setBounds(40, 55, 453, 460);
        characterPreviewPanel.setOpaque(false);
        leftStagePanel.add(characterPreviewPanel);

        JButton readyButton = new JButton("GO TRICK OR TREATING >");
        readyButton.setFont(new Font("Monospaced", Font.BOLD, 13));
        readyButton.setBounds(260, 530, 230, 45);
        readyButton.setBackground(new Color(180, 40, 40));
        readyButton.setForeground(Color.WHITE);
        readyButton.addActionListener(e -> {
            GameScreen.startInGame();
        });
        leftStagePanel.add(readyButton);

        JButton menuButton = new JButton("MENU");
        menuButton.setFont(new Font("Monospaced", Font.BOLD, 12));
        menuButton.setBounds(40, 530, 90, 45);
        menuButton.addActionListener(e -> GameScreen.showInGameMenu());
        leftStagePanel.add(menuButton);

        dressUpPanel.add(leftStagePanel);

        categoryTabs = new JTabbedPane();
        categoryTabs.setBounds(533, 0, 267, 600);
        categoryTabs.setFont(new Font("Monospaced", Font.BOLD, 11));
        categoryTabs.setBackground(new Color(35, 25, 40));
        categoryTabs.setForeground(Color.WHITE);

        for (DressItem.Category cat : DressItem.Category.values()) {
            JPanel categoryTabPanel = new JPanel(new BorderLayout());
            categoryTabPanel.setBackground(new Color(30, 20, 35));

            JPanel itemsGrid = new JPanel();
            itemsGrid.setLayout(new BoxLayout(itemsGrid, BoxLayout.Y_AXIS));
            itemsGrid.setBackground(new Color(30, 20, 35));

            for (DressItem item : availableItems) {
                if (item.getCategory() == cat) {
                    itemsGrid.add(createItemCard(item));
                    itemsGrid.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }

            JScrollPane scrollPane = new JScrollPane(itemsGrid);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(12);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane.getViewport().setBackground(new Color(30, 20, 35));

            categoryTabPanel.add(scrollPane, BorderLayout.CENTER);
            categoryTabs.addTab(cat.name(), categoryTabPanel);
        }

        dressUpPanel.add(categoryTabs);

        Main.layeredPane.add(dressUpPanel, JLayeredPane.DEFAULT_LAYER);
        Main.layeredPane.revalidate();
        Main.layeredPane.repaint();
    }

    private static void drawEquippedLayer(Graphics2D g2d, int cx, int cy, DressItem.Layer layer) {
        DressItem item = equippedItems.get(layer);
        if (item == null) return;

        g2d.setColor(item.getDisplayColor());

        switch (layer) {
            case BASE_COSTUMES:
                g2d.fillRect(cx - 55, cy - 70, 110, 140); 
                break;
            case OUTERWEAR:
                g2d.fillRect(cx - 62, cy - 75, 124, 160); 
                break;
            case FOOTWEAR:
                g2d.fillRect(cx - 50, cy + 115, 40, 25); 
                g2d.fillRect(cx + 10, cy + 115, 40, 25);  
                break;
            case HEADWEAR:
                g2d.fillPolygon(new int[]{cx - 60, cx, cx + 60}, new int[]{cy - 110, cy - 160, cy - 110}, 3); 
                break;
            case MASK:
                g2d.fillOval(cx - 45, cy - 120, 90, 80); 
                g2d.setColor(Color.BLACK);
                g2d.fillRect(cx - 25, cy - 100, 15, 15); 
                g2d.fillRect(cx + 10, cy - 100, 15, 15);
                break;
            case HAND_PROP:
                g2d.setColor(item.getDisplayColor());
                g2d.fillOval(cx + 70, cy, 35, 35); 
                break;
        }
    }

    private static JPanel createItemCard(DressItem item) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setMaximumSize(new Dimension(225, 65));
        card.setPreferredSize(new Dimension(225, 65));

        boolean isEquipped = equippedItems.get(item.getLayer()) == item;
        card.setBackground(isEquipped ? new Color(75, 40, 65) : new Color(45, 35, 55));
        card.setBorder(BorderFactory.createLineBorder(isEquipped ? new Color(255, 100, 100) : new Color(90, 70, 100), 2));

        JPanel colorIcon = new JPanel();
        colorIcon.setPreferredSize(new Dimension(35, 35));
        colorIcon.setBackground(item.getDisplayColor());
        colorIcon.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        JLabel nameLabel = new JLabel("<html><b>" + item.getName() + "</b><br><font size='2' color='#b0b0b0'>" + item.getDescription() + "</font></html>");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));

        card.add(colorIcon, BorderLayout.WEST);
        card.add(nameLabel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (equippedItems.get(item.getLayer()) == item) {
                    equippedItems.remove(item.getLayer());
                    card.setBorder(BorderFactory.createLineBorder(new Color(90, 70, 100), 2));
                    card.setBackground(new Color(45, 35, 55));
                } else {
                    equippedItems.put(item.getLayer(), item);
                    card.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
                    card.setBackground(new Color(75, 40, 65));
                }
                GameState.hasUnsavedProgress = true;
                characterPreviewPanel.repaint();
            }
        });

        return card;
    }

    public static void initItemDatabase() {
        if (!availableItems.isEmpty()) return;

        availableItems.add(new DressItem("Classic Witch", DressItem.Category.COSTUMES, DressItem.Layer.BASE_COSTUMES, new Color(70, 30, 90), "Cozy purple dress."));
        availableItems.add(new DressItem("Cardboard Robot", DressItem.Category.COSTUMES, DressItem.Layer.BASE_COSTUMES, new Color(160, 160, 160), "Heavy & clumsy armor."));
        availableItems.add(new DressItem("Vampire Cloak", DressItem.Category.COSTUMES, DressItem.Layer.OUTERWEAR, new Color(140, 20, 20), "Dark red velvet cape."));
        availableItems.add(new DressItem("Ghost Sheet", DressItem.Category.COSTUMES, DressItem.Layer.BASE_COSTUMES, new Color(230, 230, 240), "Plain sheet with cutouts."));

        availableItems.add(new DressItem("Witch Hat", DressItem.Category.MASKS, DressItem.Layer.HEADWEAR, new Color(40, 20, 50), "Pointy black hat."));
        availableItems.add(new DressItem("Papier Pumpkin", DressItem.Category.MASKS, DressItem.Layer.MASK, new Color(230, 110, 30), "Unsettling pumpkin mask."));
        availableItems.add(new DressItem("Fairy Tiara", DressItem.Category.MASKS, DressItem.Layer.HEADWEAR, new Color(255, 215, 0), "Glittery plastic crown."));

        availableItems.add(new DressItem("Plastic Pumpkin", DressItem.Category.PROPS, DressItem.Layer.HAND_PROP, new Color(240, 130, 20), "Standard candy bucket."));
        availableItems.add(new DressItem("Heavy Flashlight", DressItem.Category.PROPS, DressItem.Layer.HAND_PROP, new Color(220, 220, 100), "Casts a bright beam."));

        availableItems.add(new DressItem("Sneakers", DressItem.Category.SHOES, DressItem.Layer.FOOTWEAR, new Color(50, 120, 200), "Quiet & easy to run in."));
        availableItems.add(new DressItem("High Heels", DressItem.Category.SHOES, DressItem.Layer.FOOTWEAR, new Color(180, 30, 70), "Loud clicking noise!"));
    }
}

//////////////////////////////////ajshdklajhsdkjahsd
/// hafdsljkkklsfdhkjlfsdhkljfdhkslajfkldshjshjklfsdfhkjlsfdlhkajfsdkhjkjhsdfkhsdfhkljfdskhjlfsdahkjkhjldsafklhj
/// fkshjdkjhfsdhjksdhjklsdfkhjsfdahjklouhweuhfsdkhjlkhjdfhljksdf
/// skdjhffhaklsjdhfhsadfkljhkhopwiasdfjhflwejkhlehwfjlwhwlhkfejfhwe
/// weoiiojfwojifwefjiowefkmlewfkljdifjsjkweklwjefiojfsdwfelkj
/// fweojkiojdsfjiofweewfijiojfewijljksdflkjsdfljkeiojsdfljkwefljk
/// joswfikeqwjnohidqwpjoidfdfklmqeojidfikqdwojiqwiojdwqiojdqwojiqdwlknadskljasdkljn
/// asdljnkdaslkadskljzndklccadcnljacsljnvbjkjbvbjascjnlljnchiovalhjnsdkljdsajkasdkjdaskljadslkj