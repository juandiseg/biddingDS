import java.util.ArrayList;

public class AuctionManager {

    private static ArrayList<Auction> availableAuctions = new ArrayList<>();
    private static int lastAuctionID = 0;
    private static int lastItemID = 0;

    public static void addAuction(Auction newAuction) {
        availableAuctions.add(newAuction);
    }

    public static AuctionItem getAuctionItem(int itemID) {
        for (Auction temp : availableAuctions) {
            if (temp.getItem().getItemId() == itemID)
                return temp.getItem();
        }
        return null;
    }

    public static float bid(int userID, int auctionID, float biddingAmount) {
        for (Auction temp : availableAuctions) {
            if (temp.getAuctionID() == auctionID)
                return temp.bid(userID, biddingAmount);
        }
        return 0;
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    public static int generateItemID() {
        lastItemID++;
        return lastItemID;
    }

    public static Auction getAuction(int auctionID) {
        for (Auction temp : availableAuctions) {
            if (temp.getAuctionID() == auctionID)
                return temp;
        }
        return null;
    }
}
