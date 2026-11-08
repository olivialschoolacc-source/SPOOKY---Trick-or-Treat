package Code;


public class GameState {
    public static boolean[] saveSlots = new boolean[5];
    public static String[] saveDetails = new String[5];
    public static boolean hasUnsavedProgress = true;

    public static void initSaves() {

    }

    public static void createNewGameInSlot(int slotIndex) {
        saveSlots[slotIndex] = true;
        saveDetails[slotIndex] = "Active Save (Slot " + (slotIndex + 1) + ")";
        hasUnsavedProgress = false;
    }

    public static void deleteSlot(int slotIndex) {
        saveSlots[slotIndex] = false;
        saveDetails[slotIndex] = null;
    }
}
