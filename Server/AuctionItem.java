public class AuctionItem implements java.io.Serializable {

    private int itemId;
    private String itemTitle;
    private String itemDescription;
    private int itemCondition;

    public AuctionItem(int itemId, String itemTitle, String itemDescription, int itemCondition) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.itemCondition = itemCondition;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemCondition() {
        String condition = "New";
        if (itemCondition == 1)
            condition = "Like new";
        if (itemCondition == 2)
            condition = "In good state";
        if (itemCondition == 3)
            condition = "Has signs of use";
        if (itemCondition == 4)
            condition = "Broken";
        return condition;
    }

    // Debugging purposes.
    public void printSummary() {
        System.out.println("{ itemId : " + itemId + " },");
        System.out.println("{ itemTitle : '" + itemTitle + "'' },");
        System.out.println("{ itemDescription : '" + itemDescription + "' },");
        System.out.println("{ itemCondition : '" + getItemCondition() + "' }");
    }

}