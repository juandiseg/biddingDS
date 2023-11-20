
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class DoubleAuctionManager {

    private static int lastAuctionID = 0;
    private static Hashtable<Integer, LinkedList<DoubleAuction>> availableDoubleAuctions = new Hashtable<>();
    static {
        availableDoubleAuctions.put(1, new LinkedList<DoubleAuction>());
        availableDoubleAuctions.put(2, new LinkedList<DoubleAuction>());
        availableDoubleAuctions.put(3, new LinkedList<DoubleAuction>());
        availableDoubleAuctions.put(4, new LinkedList<DoubleAuction>());
    }

    public static int addDoubleAuction(int itemID, int limitSellers, int limitBids) {
        // if(checkItemID) return false;
        lastAuctionID++;
        DoubleAuction temp = new DoubleAuction(lastAuctionID, lastAuctionID, limitSellers, limitBids);
        availableDoubleAuctions.get(hashFunction(lastAuctionID)).add(temp);
        return lastAuctionID;
    }

    public static String viewDoubleAuctionsSellers(User user) {
        String returnStr = "";
        for (int i = 1; i < 5; i++) {
            for (DoubleAuction temp : availableDoubleAuctions.get(i)) {
                if (temp.isAuctionClosed()) {
                    returnStr = returnStr
                            .concat("\nDouble Auction ID #" + temp.getDoubleAuctionID() + " is closed and resolved.\n");
                    returnStr = returnStr
                            .concat(temp.getResolutionSeller(user));
                } else {
                    returnStr = returnStr.concat("\nDouble Auction ID #" + temp.getDoubleAuctionID() + "\n");
                    returnStr = returnStr.concat(
                            "Item on sale\t| " + temp.getItemID() + " (" + ITEMS.getNameOfItem(temp.getItemID())
                                    + ")\n");
                    returnStr = returnStr.concat("Limit sellers\t| " + temp.getLimitItems() + " ("
                            + temp.getCurrentNumberListings() + ") \n");
                    returnStr = returnStr.concat(
                            "Limit bids\t| " + temp.getLimitBids() + " (" + temp.getCurrentNumberBids() + ") \n");
                    returnStr = returnStr.concat("------------------------------------------------\n");
                }
            }
        }
        return returnStr;
    }

    public static String viewDoubleAuctionsBuyers(User user) {
        String returnStr = "";
        for (int i = 1; i < 5; i++) {
            for (DoubleAuction temp : availableDoubleAuctions.get(i)) {
                if (temp.getLimitItems() == temp.getCurrentNumberListings()) {
                    if (temp.isAuctionClosed()) {
                        returnStr = returnStr
                                .concat("\nDouble Auction ID #" + temp.getDoubleAuctionID()
                                        + " is closed and resolved.\n");
                        returnStr = returnStr
                                .concat(temp.getResolutionBuyer(user));
                    } else {
                        returnStr = returnStr.concat("\nDouble Auction ID #" + temp.getDoubleAuctionID() + "\n");
                        returnStr = returnStr.concat(
                                "Item on sale\t| " + temp.getItemID() + " (" + ITEMS.getNameOfItem(temp.getItemID())
                                        + ")\n");
                        returnStr = returnStr.concat("Limit sellers\t| " + temp.getLimitItems() + " ("
                                + temp.getCurrentNumberListings() + ") \n");
                        returnStr = returnStr.concat(
                                "Limit bids\t| " + temp.getLimitBids() + " (" + temp.getCurrentNumberBids() + ") \n");
                        double userBid = temp.getUsersBid(user);
                        if (userBid != -1) {
                            returnStr = returnStr
                                    .concat("You have a bid in this double auction for " + userBid + " EUR.\n");
                        }
                        returnStr = returnStr.concat("------------------------------------------------\n");
                    }
                }
            }
        }
        if (returnStr.equals("")) {
            return "There are not any available double auctions.";
        }
        return returnStr;
    }

    private static int hashFunction(int auctionID) {
        return auctionID % 4 + 1;
    }

    public static int getItemIDofAuction(int auctionID) {
        return getDoubleAuction(auctionID).getItemID();
    }

    public static void addAuctionItem(int doubleAuctionID, AuctionItem newItem) {
        DoubleAuction doubleAuction = getDoubleAuction(doubleAuctionID);
        doubleAuction.addAuction(newItem);
    }

    private static DoubleAuction getDoubleAuction(int doubleAuctionID) {
        return availableDoubleAuctions.get(hashFunction(doubleAuctionID)).stream()
                .filter(temp -> (temp.getDoubleAuctionID() == doubleAuctionID)).collect(Collectors.toList()).get(0);
    }

    public static boolean doubleAuctionExists(int doubleAuctionID) {
        return availableDoubleAuctions.get(hashFunction(doubleAuctionID)).stream()
                .anyMatch(temp -> (temp.getDoubleAuctionID() == doubleAuctionID));
    }

    public static boolean bid(int doubleAuctionID, User user, Double biddingAmount) {
        if (doubleAuctionExists(doubleAuctionID)) {
            DoubleAuction theAuction = getDoubleAuction(doubleAuctionID);
            return theAuction.addBid(user, biddingAmount);
        }
        return false;
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    public static String getResolutionBuyer(int auctionID, User user) {
        if (!doubleAuctionExists(auctionID)) {
            return "The given double auction ID does not exist.";
        }
        return getDoubleAuction(auctionID).getResolutionBuyer(user);
    }

    public static String getResolutionSeller(int auctionID, User user) {
        if (!doubleAuctionExists(auctionID)) {
            return "The given double auction ID does not exist.";
        }
        return getDoubleAuction(auctionID).getResolutionSeller(user);
    }

    public static boolean isSellersLimitReached(int auctionID) {
        return getDoubleAuction(auctionID).isSellersLimitReached();
    }

}
