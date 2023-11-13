import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class AuctionManager {

    private final static Double DOESNT_EXIST = 0.0;
    private final static Double AUCTION_CLOSED = -4.0;

    private static int lastAuctionID = 0;
    private static Hashtable<Integer, LinkedList<Auction>> availableAuctions = new Hashtable<>();
    static {
        availableAuctions.put(1, new LinkedList<Auction>());
        availableAuctions.put(2, new LinkedList<Auction>());
        availableAuctions.put(3, new LinkedList<Auction>());
        availableAuctions.put(4, new LinkedList<Auction>());
        availableAuctions.put(5, new LinkedList<Auction>());
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
        availableAuctions.get(hashFunction(newAuction.getAuctionID())).add(newAuction);
    }

    private static int hashFunction(int auctionID) {
        return auctionID % 5;
    }

    public static String getAuctionsDisplay(int itemID) {
        String displayString = "";
        for (int i = 1; i < 6; i++) {
            for (Auction temp : availableAuctions.get(i)) {
                if ((temp.getItem().getItemId() == itemID || itemID < 0) && !temp.isAuctionClosed()) {
                    String tempString = "Auction #" + temp.getAuctionID() + " :\n";
                    tempString = tempString.concat("-------------\n");
                    tempString = tempString.concat("Item\t| ID \t= " + temp.getItem().getItemId() + ".\n");
                    tempString = tempString.concat("\t| Title \t= " + temp.getItem().getItemTitle() + ".\n");
                    tempString = tempString
                            .concat("\t| Description \t= " + temp.getItem().getItemDescription() + ".\n");
                    tempString = tempString.concat("\t| Condition \t= " + temp.getItem().getItemCondition() + ".\n");
                    tempString = tempString.concat("Starting Price = " + temp.getStartingPrice() + " EUR. \n");
                    Double winningBid = temp.getWinningBidAmount();
                    if (winningBid == -1)
                        tempString = tempString.concat("Highest Bid    = THERE ARE NO BIDS.\n\n");
                    else
                        tempString = tempString.concat("Highest Bid    = " + winningBid + " EUR. \n\n");
                    tempString = tempString.concat("----------------------------------------------------\n");

                    displayString = displayString.concat(tempString);
                }
            }
        }
        if (displayString.equals("")) {
            return "There are no open auctions for the specified item";
        }
        return displayString;
    }

    public static String getItemsDisplay() {
        List<Integer> alreadyListed = new LinkedList<Integer>();
        String displayString = "The following items have open auctions:\n";
        for (int i = 1; i < availableAuctions.size() + 1; i++) {
            for (Auction temp : availableAuctions.get(i)) {
                int itemID = temp.getItem().getItemId();
                if (!alreadyListed.contains(itemID) && !temp.isAuctionClosed()) {
                    displayString = displayString
                            .concat("\tItem #" + itemID + " : " + ITEMS.getItems().get(itemID) + "\n");
                    alreadyListed.add(itemID);
                }
            }
        }
        if (displayString.equals("")) {
            return "There are no open auctions.";
        }
        return displayString;
    }

    public static AuctionItem getAuctionItem(int itemID) {
        for (int i = 1; i < 6; i++) {
            for (Auction temp : availableAuctions.get(i)) {
                if (temp.getItem().getItemId() == itemID)
                    return temp.getItem();
            }
        }
        return null;
    }

    public static Double bid(int auctionID, String username, String email, Double biddingAmount) {
        Auction theAuction = getAuction(auctionID);
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
