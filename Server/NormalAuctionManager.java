
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NormalAuctionManager {

    private final static Double DOESNT_EXIST = 0.0;
    private final static Double AUCTION_CLOSED = -4.0;

    private static int lastAuctionID = 0;
    private static Hashtable<Integer, LinkedList<NormalAuction>> availableAuctions = new Hashtable<>();
    static {
        availableAuctions.put(1, new LinkedList<NormalAuction>());
        availableAuctions.put(2, new LinkedList<NormalAuction>());
        availableAuctions.put(3, new LinkedList<NormalAuction>());
        availableAuctions.put(4, new LinkedList<NormalAuction>());
        availableAuctions.put(5, new LinkedList<NormalAuction>());
    }

    public static String checkAuctionStatusSeller(User user, int auctionID) {
        boolean result = UserManager.validateUser(user, 'S');
        if (!result) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction.getSellerUsername().equals(user.getUsername())) {
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
                status = status.concat("The reserve price of " + reservePrice + " EUR has been reached.");
            else
                status = status.concat("The reserve price of " + reservePrice + " EUR has NOT been reached.");
            return status;
        }
        return "You don't have access to this auction's status.";
    }

    public static String checkAuctionStatusBuyer(String username, String password, int auctionID) {
        char result = UserManager.validateUserByUsername(username, password);
        if (result == 'E') {
            return "The authetication details were wrong.";
        } else if (result == 'S') {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
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

    public static int addAuction(AuctionItem item, double reservePrice) {
        NormalAuction newAuction = new NormalAuction(lastAuctionID++, item, reservePrice);
        availableAuctions.get(hashFunction(newAuction.getAuctionID())).add(newAuction);
        return lastAuctionID;
    }

    private static int hashFunction(int auctionID) {
        return auctionID % 5;
    }

    public static String getAuctionsDisplay(int itemID) {
        String displayString = "";
        for (int i = 1; i < 6; i++) {
            for (NormalAuction temp : availableAuctions.get(i)) {
                if ((temp.getItem().getItemId() == itemID || itemID < 0) && !temp.isAuctionClosed()) {
                    String tempString = "Auction #" + temp.getAuctionID() + " :\n";
                    tempString = tempString.concat("-------------\n");
                    tempString = tempString.concat("Item\t| ID \t= " + temp.getItem().getItemId() + ".\n");
                    tempString = tempString.concat("\t| Title \t= " + temp.getItem().getItemTitle() + ".\n");
                    tempString = tempString
                            .concat("\t| Description \t= " + temp.getItem().getItemDescription() + ".\n");
                    tempString = tempString.concat("\t| Condition \t= " + temp.getItem().getItemCondition() + ".\n");
                    tempString = tempString.concat("Starting Price = " + temp.getItem().getSellingPrice() + " EUR. \n");
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
            for (NormalAuction temp : availableAuctions.get(i)) {
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

    public static HashMap<Integer, AuctionItem> getSpec(User user, int itemID) {
        HashMap<Integer, AuctionItem> map = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            for (NormalAuction temp : availableAuctions.get(i).stream().filter(t -> (t.getItem().getItemId() == itemID))
                    .collect(Collectors.toList())) {
                map.put(temp.getAuctionID(), temp.getItem());
            }
        }
        return map;
    }

    public static Double bid(int auctionID, String username, String email, Double biddingAmount) {
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return DOESNT_EXIST;
        if (theAuction.isAuctionClosed())
            return AUCTION_CLOSED;
        return theAuction.bid(UserManager.getUser(username), biddingAmount);
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    private static NormalAuction getAuction(int auctionID) {
        LinkedList<NormalAuction> hashEntry = availableAuctions.get(hashFunction(auctionID));
        for (NormalAuction temp : hashEntry) {
            if (temp.getAuctionID() == auctionID)
                return temp;
        }
        return null;
    }

    public static String closeAuction(User user, int auctionID) {
        boolean result = UserManager.validateUser(user, 'S');
        if (!result) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return "There are no auctions with the specified ID.";
        if (theAuction.getSellerUsername().equals(user.getUsername()))
            return theAuction.closeAuction();
        return "You don't have access to this editting this auction.";
    }
}
