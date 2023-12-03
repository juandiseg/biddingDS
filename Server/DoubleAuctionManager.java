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

    public static int createDoubleAuction(int itemID, int limitSellers, int limitBids) {
        if (itemReferenceExists(itemID)) {
            return -1;
        }
        lastAuctionID++;
        DoubleAuction temp = new DoubleAuction(lastAuctionID, lastAuctionID, limitSellers, limitBids);
        availableDoubleAuctions.get(hashFunction(lastAuctionID)).add(temp);
        return lastAuctionID;
    }

    public static String getDoubleAuctionsDisplaySellers(User user) {
        String returnStr = "";
        for (int i = 1; i < 5; i++) {
            for (DoubleAuction temp : availableDoubleAuctions.get(i)) {
                returnStr = returnStr.concat(temp.checkAuctionStatusSeller(user));
            }
        }
        if (returnStr.equals("")) {
            return "There are not any available double auctions.";
        }
        return returnStr;
    }

    private static boolean itemReferenceExists(int itemID) {
        return ITEMS.getItems().get(itemID) != null;
    }

    public static String getDoubleAuctionsDisplayBuyers(User user) {
        String returnStr = "";
        for (int i = 1; i < 5; i++) {
            for (DoubleAuction temp : availableDoubleAuctions.get(i)) {
                returnStr = returnStr.concat(temp.checkDoubleAuctionBuyer(user));
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

    public static int getDoubleAuctionsItemID(int auctionID) {
        return getDoubleAuction(auctionID).getItemID();
    }

    public static void joinDoubleAuction(int doubleAuctionID, AuctionItem newItem) {
        DoubleAuction doubleAuction = getDoubleAuction(doubleAuctionID);
        doubleAuction.join(newItem);
    }

    private static DoubleAuction getDoubleAuction(int doubleAuctionID) {
        return availableDoubleAuctions.get(hashFunction(doubleAuctionID)).stream()
                .filter(temp -> (temp.getDoubleAuctionID() == doubleAuctionID)).collect(Collectors.toList()).get(0);
    }

    public static boolean doesDoubleAuctionExists(int doubleAuctionID) {
        return availableDoubleAuctions.get(hashFunction(doubleAuctionID)).stream()
                .anyMatch(temp -> (temp.getDoubleAuctionID() == doubleAuctionID));
    }

    public static boolean bid(int doubleAuctionID, User user, Double biddingAmount) {
        if (doesDoubleAuctionExists(doubleAuctionID)) {
            DoubleAuction theAuction = getDoubleAuction(doubleAuctionID);
            return theAuction.addBid(user, biddingAmount);
        }
        return false;
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    public static String getResolutionBuyer(User user, int auctionID) {
        if (!doesDoubleAuctionExists(auctionID)) {
            return "The given double auction ID does not exist.";
        }
        return getDoubleAuction(auctionID).getResolutionBuyer(user);
    }

    public static String getResolutionSeller(User user, int auctionID) {
        if (!doesDoubleAuctionExists(auctionID)) {
            return "The given double auction ID does not exist.";
        }
        return getDoubleAuction(auctionID).getResolutionSeller(user);
    }

    public static boolean isSellersLimitReached(int auctionID) {
        return getDoubleAuction(auctionID).isSellersLimitReached();
    }

}