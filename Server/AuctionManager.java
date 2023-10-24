import java.util.ArrayList;
import java.util.Hashtable;

public class AuctionManager {

    private static Hashtable<Integer, ArrayList<Auction>> availableAuctions = new Hashtable<>();
    private static int lastAuctionID = 0;
    private static int lastItemID = 0;
    static {
        availableAuctions.put(1, new ArrayList<Auction>());
        availableAuctions.put(2, new ArrayList<Auction>());
        availableAuctions.put(3, new ArrayList<Auction>());
        availableAuctions.put(4, new ArrayList<Auction>());
        availableAuctions.put(5, new ArrayList<Auction>());
    }

    public static String checkAuctionStatus(int userID, int auctionID) {
        Auction theAuction = getAuction(auctionID);
        if (theAuction.getSellerID() == userID) {
            int numberBids = theAuction.getNumberOfBids();
            String status = "";
            if (numberBids == 0)
                status = "There aren't any bids yet.\n";
            else
                status = "There are " + numberBids + " bids.\n";
            float winningBid = theAuction.getWinningBidAmount();
            float reservePrice = theAuction.getReservePrice();
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

    public static void addAuction(Auction newAuction) {
        availableAuctions.get(hashFunction(newAuction.getAuctionID())).add(newAuction);
    }

    private static int hashFunction(int auctionID) {
        return auctionID % 5;
    }

    public static String getDisplayString() {
        String displayString = "";
        for (int i = 1; i < 6; i++) {
            for (Auction temp : availableAuctions.get(i)) {
                String tempString = "Auction #" + temp.getAuctionID() + " :\n";
                tempString = tempString.concat("-------------\n");
                tempString = tempString.concat("Item\t| ID \t= " + temp.getItem().getItemId() + ".\n");
                tempString = tempString.concat("\t| Title \t= " + temp.getItem().getItemTitle() + ".\n");
                tempString = tempString.concat("\t| Description \t= " + temp.getItem().getItemDescription() + ".\n");
                tempString = tempString.concat("\t| Condition \t= " + temp.getItem().getItemCondition() + ".\n");
                tempString = tempString.concat("Starting Price = " + temp.getStartingPrice() + "€. \n");
                float winningBid = temp.getWinningBidAmount();
                if (winningBid == -1)
                    tempString = tempString.concat("Highest Bid    = THERE ARE NO BIDS.\n\n");
                else
                    tempString = tempString.concat("Highest Bid    = " + winningBid + "€. \n\n");
                tempString = tempString.concat("----------------------------------------------------\n");

                displayString = displayString.concat(tempString);
            }
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

    public static float bid(int auctionID, String username, String email, float biddingAmount) {
        Auction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return 0;
        return theAuction.bid(username, email, biddingAmount);
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    public static int generateItemID() {
        lastItemID++;
        return lastItemID;
    }

    private static Auction getAuction(int auctionID) {
        ArrayList<Auction> hashEntry = availableAuctions.get(hashFunction(auctionID));
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
