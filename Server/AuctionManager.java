import java.util.ArrayList;

public class AuctionManager {

    private static ArrayList<Auction> availableAuctions = new ArrayList<>();
    private static int lastAuctionID = 0;
    private static int lastItemID = 0;

    public static void addAuction(Auction newAuction) {
        availableAuctions.add(newAuction);
    }

    public static String getDisplayString() {
        String displayString = "";
        for (Auction temp : availableAuctions) {
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
        return displayString;
    }

    public static AuctionItem getAuctionItem(int itemID) {
        for (Auction temp : availableAuctions) {
            if (temp.getItem().getItemId() == itemID)
                return temp.getItem();
        }
        return null;
    }

    public static float bid(int auctionID, String username, String email, float biddingAmount) {
        for (Auction temp : availableAuctions) {
            if (temp.getAuctionID() == auctionID)
                return temp.bid(username, email, biddingAmount);
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

    public static String closeAuction(int sellerID, int auctionID) {
        Auction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return "There are no auctions with the specified ID.";
        if (theAuction.getSellerID() == sellerID)
            return theAuction.closeAuction();
        return "You don't have access to this editting this auction.";
    }
}
