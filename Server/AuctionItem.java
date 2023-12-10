
public class AuctionItem implements java.io.Serializable {

    private final User seller;
    private int id;
    private String title;
    private String description;
    private int condition;
    private double sellingPrice;

    public AuctionItem(User user, int id, String title, String description, int condition,
            double sellingPrice) {
        this.seller = user;
        this.id = id;
        this.title = title;
        this.description = description;
        if (condition < 1 || condition > 5) {
            this.condition = -1;
        } else {
            this.condition = condition;
        }
        this.sellingPrice = sellingPrice;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getDescription() {
        return description;
    }

    public User getSeller() {
        return seller;
    }

    public String getItemCondition() {
        if (condition == 2)
            return "Like new";
        if (condition == 3)
            return "In good state";
        if (condition == 4)
            return "Has signs of use";
        if (condition == 5)
            return "Broken";
        if (condition == -1)
            return "Unknown";
        return "New";
    }

    public void printSummary(int auctionID) {
        System.out.println("This item is being sold on the Auction #" + auctionID);
        System.out.println("\tItem's ID : " + id);
        System.out.println("\tItem's Title : " + title);
        System.out.println("\tItem's Description : " + description);
        System.out.println("\tItem's Condition : " + getItemCondition());
    }
}