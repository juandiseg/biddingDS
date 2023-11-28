
import java.util.HashMap;

public class ITEMS {
    private final static HashMap<Integer, String> itemNames = new HashMap<>();
    static {
        itemNames.put(1, "iPhone");
        itemNames.put(2, "MacBook");
        itemNames.put(3, "Airpods");
        itemNames.put(4, "iWatch");
    }

    public static HashMap<Integer, String> getItems() {
        return itemNames;
    }

    public static String getItemsText() {
        String text = "";
        for (int i = 1; i < itemNames.size() + 1; i++) {
            text = text.concat("The item #" + i + " references the product: \"" + itemNames.get(i) + "\".\n");
        }
        return text;
    }

    public static String getNameOfItem(int itemID) {
        return itemNames.get(itemID);
    }
}
