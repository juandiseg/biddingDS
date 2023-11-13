import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class DoubleAuctionManager {

    private final static Double DOESNT_EXIST = 0.0;
    private final static Double AUCTION_CLOSED = -4.0;

    private static int lastAuctionID = 0;
    private static Hashtable<Integer, LinkedList<DoubleAuction>> availableDoubleAuctions = new Hashtable<>();

    static {
        availableDoubleAuctions.put(1, new LinkedList<DoubleAuction>());
        availableDoubleAuctions.put(2, new LinkedList<DoubleAuction>());
        availableDoubleAuctions.put(3, new LinkedList<DoubleAuction>());
        availableDoubleAuctions.put(4, new LinkedList<DoubleAuction>());
    }

    public static String checkAuctionStatusSeller(int userID, int auctionID) {
        Auction theAuction = getAuction(auctionID);
        if (theAuction.getSellerID() == userID) {
            String status = "";
            if (theAuction.isAuctionClosed())
                status = "This auction is already closed. Summary:\n";
            int numberBids = theAuction.getNumberOfBids();
            if (numberBids == 0)
                status = status.concat("There aren't any bids.\n");
            else
                status = status.concat("There are " + numberBids + " bids.\n");
            Double winningBid = theAuction.getWinningBidAmount();
            Double reservePrice = theAuction.getReservePrice();
            if (numberBids != 0)
                status = status.concat("The currently winning bid is for " + winningBid + "€. ");
            if (winningBid >= reservePrice)
                status = status.concat("The reserve price of " + reservePrice + "€ has been reached.");
            else
                status = status.concat("The reserve price of " + reservePrice + "€ has NOT been reached.");
            return status;
        }
        return "You don't have access to this auction's status.";
    }

    public static String checkAuctionStatusBuyer(String username, int auctionID) {
        Auction theAuction = getAuction(auctionID);
        String status = "";
        if (theAuction.isAuctionClosed())
            status = "This auction has already been closed.\n";
        Double winningBid = theAuction.getWinningBidAmount();
        String winningUsername = theAuction.getWinningBidUsername();
        if (winningBid == -1.0) {
            status = status.concat("There were no bids for this auction.");
            return status;
        }
        if (winningUsername.equals(username)) {
            status = status.concat("You were the winning bid, with a " + winningBid + "EUR bid.");
        } else {
            status = status.concat("Someone else won the bid with a " + winningBid + "EUR bid.");
        }
        return status;
    }

    public static HashMap<Integer, String> getAvailableItems() {
        return ITEMS.getItems();
    }

    public static void addAuction(Auction newAuction) {
        availableDoubleAuctions.get(newAuction.getItem().getItemId()).add(newAuction);
    }

    public static String getAuctionsDisplay(int itemID) {
        String displayString = "";
        for (int i = 1; i < 6; i++) {
            for (Auction temp : availableDoubleAuctions.get(i)) {
                if ((temp.getItem().getItemId() == itemID || itemID < 0) && !temp.isAuctionClosed()) {
                    String tempString = "Double Auction #" + i + " :\n";
                    tempString = tempString.concat("-------------\n");
                    tempString = tempString.concat("Item\t| " + itemNames.get(i) + "\n");
                    tempString = tempString.concat("# on sale\t| " + availableDoubleAuctions.get(i).size() + "\n");
                    Double winningBid = temp.getWinningBidAmount();
                    displayString = displayString.concat(tempString);
                }
            }
        }
        if (displayString.equals("")) {
            return "There are no available double auctions for the specified item.";
        }
        return displayString;
    }

    public static boolean bid(int doubleAuctionID, String username, String email, Double biddingAmount) {
        Auction theAuction = getAuction(doubleAuctionID);
        if (theAuction == null)
            return DOESNT_EXIST;
        if (theAuction.isAuctionClosed())
            return AUCTION_CLOSED;
        return theAuction.bid(username, email, biddingAmount);
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    private static Auction getAuction(int auctionID) {
        LinkedList<Auction> hashEntry = availableAuctions.get(hashFunction(auctionID));
        for (Auction temp : hashEntry) {
            if (temp.getAuctionID() == auctionID)
                return temp;
        }
        return null;
    }

    public static String closeAuction(int sellerID, int auctionID) {
        Auction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return "There are no auctions with the specified ID.";
        if (theAuction.getSellerID() == sellerID)
            return theAuction.closeAuction();
        return "You don't have access to this editting this auction.";
    }
}
