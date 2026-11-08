package Code;

import java.awt.Color;

public class DressItem {
    public enum Category { COSTUMES, MASKS, PROPS, SHOES }
    public enum Layer { BASE_COSTUMES, OUTERWEAR, HEADWEAR, MASK, HAND_PROP, FOOTWEAR }

    private String name;
    private Category category;
    private Layer layer;
    private Color displayColor;
    private String description;

    public DressItem(String name, Category category, Layer layer, Color displayColor, String description) {
        this.name = name;
        this.category = category;
        this.layer = layer;
        this.displayColor = displayColor;
        this.description = description;
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public Layer getLayer() { return layer; }
    public Color getDisplayColor() { return displayColor; }
    public String getDescription() { return description; }
}