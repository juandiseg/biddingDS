
import java.util.Hashtable;

public class DoubleAuctionManager {

    private static int lastAuctionID = 4;
    private static Hashtable<Integer, DoubleAuction> availableDoubleAuctions = new Hashtable<>();
    static {
        availableDoubleAuctions.put(1, new DoubleAuction(1, 1));
        availableDoubleAuctions.put(2, new DoubleAuction(2, 2));
        availableDoubleAuctions.put(3, new DoubleAuction(3, 3));
        availableDoubleAuctions.put(4, new DoubleAuction(4, 4));
    }

    public static String checkDoubleAuctionStatus(int auctionID) {
        DoubleAuction theAuction = availableDoubleAuctions.get(auctionID);
        if (theAuction.isAuctionClosed()) {
            return "The auction is closed. You can check ";
        }
        String status = "This double auction is for: " + ITEMS.getItems().get(auctionID) + "(s).\n";
        status = status
                .concat("There are " + theAuction.getNumberOfBids() + " bids for " + theAuction.getNumberOfSellers()
                        + " items for sale.\n");
        return status;
    }

    public static void addAuctionItem(AuctionItem newItem) {
        availableDoubleAuctions.get(newItem.getItemId()).addAuction(newItem);
    }

    public static String getDoubleAuctionsDisplay(int itemID) {
        String displayString = "";
        for (int i = 1; i < availableDoubleAuctions.size() + 1; i++) {
            displayString = displayString.concat(checkDoubleAuctionStatus(i));
        }
        if (displayString.equals("")) {
            return "There are no available double auctions for the specified item.";
        }
        return displayString;
    }

    public static boolean bid(int doubleAuctionID, String username, Double biddingAmount) {
        DoubleAuction theAuction = availableDoubleAuctions.get(doubleAuctionID);
        return theAuction.bid(UserManager.getUser(username), biddingAmount);
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    public static String closeAuction(String sellerUsername, int doubleAuctionID) {
        DoubleAuction theAuction = availableDoubleAuctions.get(doubleAuctionID);
        if (theAuction == null)
            return "There are no auctions with the specified ID.";
        return theAuction.closeAuction(sellerUsername);
    }
}
