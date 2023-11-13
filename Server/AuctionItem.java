public class AuctionItem implements java.io.Serializable {

    private final int sellerID;
    private boolean auctionClosed = false;
    private int itemId;
    private String itemTitle;
    private String itemDescription;
    private int itemCondition;
    private double sellingPrice;

    public AuctionItem(int sellerID, int itemId, String itemTitle, String itemDescription, int itemCondition,
            double sellingPrice) {
        this.sellerID = sellerID;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.itemCondition = itemCondition;
        this.sellingPrice = sellingPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public void closeAuction() {
        auctionClosed = false;
    }

    public boolean isAuctionClosed() {
        return auctionClosed;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getSellerID() {
        return sellerID;
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