package Code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GameState {
    public static boolean[] saveSlots = new boolean[5];
    public static String[] saveDetails = new String[5];
    public static boolean hasUnsavedProgress = true;

    @SuppressWarnings("unchecked")
    public static Map<DressItem.Layer, String>[] slotEquippedOutfits = new HashMap[5];

    private static final String SAVE_FILE_NAME = "savegame.json";

    public static void initSaves() {
        for (int i = 0; i < 5; i++) {
            slotEquippedOutfits[i] = new HashMap<>();
        }
        loadFromJson();
    }

    public static void createNewGameInSlot(int slotIndex) {
        saveSlots[slotIndex] = true;
        saveDetails[slotIndex] = "Active Save (Slot " + (slotIndex + 1) + ")";
        hasUnsavedProgress = false;

        slotEquippedOutfits[slotIndex].clear();
        for (Map.Entry<DressItem.Layer, DressItem> entry : DressUpScreen.equippedItems.entrySet()) {
            slotEquippedOutfits[slotIndex].put(entry.getKey(), entry.getValue().getName());
        }
        
        saveToJson();
    }

    public static void deleteSlot(int slotIndex) {
        saveSlots[slotIndex] = false;
        saveDetails[slotIndex] = null;
        if (slotEquippedOutfits[slotIndex] != null) {
            slotEquippedOutfits[slotIndex].clear();
        }
        saveToJson();
    }

    public static void loadSlotOutfit(int slotIndex) {
        DressUpScreen.equippedItems.clear();
        Map<DressItem.Layer, String> savedOutfit = slotEquippedOutfits[slotIndex];
        if (savedOutfit != null) {
            for (Map.Entry<DressItem.Layer, String> entry : savedOutfit.entrySet()) {
                for (DressItem item : DressUpScreen.availableItems) {
                    if (item.getLayer() == entry.getKey() && item.getName().equals(entry.getValue())) {
                        DressUpScreen.equippedItems.put(entry.getKey(), item);
                        break;
                    }
                }
            }
        }
        hasUnsavedProgress = false;
    }

    public static void saveToJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"slots\": [\n");

        for (int i = 0; i < 5; i++) {
            json.append("    {\n");
            json.append("      \"index\": ").append(i).append(",\n");
            json.append("      \"active\": ").append(saveSlots[i]).append(",\n");
            json.append("      \"details\": \"").append(saveDetails[i] != null ? saveDetails[i] : "").append("\",\n");
            json.append("      \"outfit\": {\n");
            
            int layerCount = 0;
            Map<DressItem.Layer, String> outfit = slotEquippedOutfits[i];
            if (outfit != null) {
                for (Map.Entry<DressItem.Layer, String> entry : outfit.entrySet()) {
                    json.append("        \"").append(entry.getKey().name()).append("\": \"").append(entry.getValue()).append("\"");
                    layerCount++;
                    if (layerCount < outfit.size()) {
                        json.append(",");
                    }
                    json.append("\n");
                }
            }
            json.append("      }\n");
            json.append("    }").append(i < 4 ? "," : "").append("\n");
        }

        json.append("  ]\n");
        json.append("}\n");

        try (FileWriter file = new FileWriter(SAVE_FILE_NAME)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromJson() {
        File file = new File(SAVE_FILE_NAME);
        if (!file.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(SAVE_FILE_NAME)));
            
            for (int i = 0; i < 5; i++) {
                String slotToken = "\"index\": " + i;
                int startIndex = content.indexOf(slotToken);
                if (startIndex == -1) continue;

                String activeToken = "\"active\": ";
                int activeIdx = content.indexOf(activeToken, startIndex);
                if (activeIdx != -1) {
                    int valStart = activeIdx + activeToken.length();
                    boolean isActive = content.substring(valStart, valStart + 4).trim().startsWith("true");
                    saveSlots[i] = isActive;
                }

                String detailsToken = "\"details\": \"";
                int detailsIdx = content.indexOf(detailsToken, startIndex);
                if (detailsIdx != -1) {
                    int valStart = detailsIdx + detailsToken.length();
                    int valEnd = content.indexOf("\"", valStart);
                    String details = content.substring(valStart, valEnd);
                    saveDetails[i] = details.isEmpty() ? null : details;
                }

                int outfitBlockStart = content.indexOf("\"outfit\": {", startIndex);
                int outfitBlockEnd = content.indexOf("}", outfitBlockStart);
                if (outfitBlockStart != -1 && outfitBlockEnd != -1) {
                    String outfitSubStr = content.substring(outfitBlockStart, outfitBlockEnd);
                    
                    slotEquippedOutfits[i].clear();
                    for (DressItem.Layer layer : DressItem.Layer.values()) {
                        String layerKey = "\"" + layer.name() + "\": \"";
                        int layerIdx = outfitSubStr.indexOf(layerKey);
                        if (layerIdx != -1) {
                            int lStart = layerIdx + layerKey.length();
                            int lEnd = outfitSubStr.indexOf("\"", lStart);
                            String itemName = outfitSubStr.substring(lStart, lEnd);
                            slotEquippedOutfits[i].put(layer, itemName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}