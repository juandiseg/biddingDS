
public class AuctionItem implements java.io.Serializable {

    private final String sellerUsername;
    private boolean auctionClosed = false;
    private int itemId;
    private String itemTitle;
    private String itemDescription;
    private int itemCondition;
    private double sellingPrice;

    public AuctionItem(String sellerUsername, int itemId, String itemTitle, String itemDescription, int itemCondition,
            double sellingPrice) {
        this.sellerUsername = sellerUsername;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        if (itemCondition < 1 || itemCondition > 5) {
            this.itemCondition = -1;
        } else {
            this.itemCondition = itemCondition;
        }
        this.sellingPrice = sellingPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public void closeAuction() {
        auctionClosed = true;
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

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getItemCondition() {
        if (itemCondition == 2)
            return "Like new";
        if (itemCondition == 3)
            return "In good state";
        if (itemCondition == 4)
            return "Has signs of use";
        if (itemCondition == 5)
            return "Broken";
        if (itemCondition == -1)
            return "Unknown";
        return "New";
    }

    public void printSummary(int auctionID) {
        System.out.println("This item is being sold on the Auction #" + auctionID);
        System.out.println("\tItem's ID : " + itemId);
        System.out.println("\tItem's Title : " + itemTitle);
        System.out.println("\tItem's Description : " + itemDescription);
        System.out.println("\tItem's Condition : " + getItemCondition());
    }
}