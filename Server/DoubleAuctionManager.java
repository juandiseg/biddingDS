import java.util.HashMap;

public class DoubleAuctionManager {

    private static int lastAuctionID = 0;
    private static HashMap<Integer, DoubleAuction> availableAuctions = ServerReplication.getDoubleAuctionState();
    private static HashMap<Integer, DoubleAuction> unsynchronized = new HashMap<Integer, DoubleAuction>();
    private static boolean beenUpdated = false;

    public static synchronized HashMap<Integer, DoubleAuction> getUnsyncronizedAuctions() {
        if (beenUpdated) {
            beenUpdated = false;
            return DoubleAuctionManager.availableAuctions;
        } else {
            return DoubleAuctionManager.unsynchronized;
        }
    }

    public static synchronized void cleanUnsynchronizedAuctions() {
        DoubleAuctionManager.unsynchronized.clear();
    }

    public static int createDoubleAuction(int itemID, int limitSellers, int limitBids) {
        if (itemReferenceExists(itemID)) {
            return -1;
        }
        int auctionID = generateID();
        DoubleAuction temp = new DoubleAuction(auctionID, auctionID, limitSellers, limitBids);
        availableAuctions.put(auctionID, temp);
        unsynchronized.put(auctionID, temp);
        beenUpdated = true;
        return auctionID;
    }

    private static int generateID() {
        while (lastAuctionID <= availableAuctions.size()) {
            lastAuctionID++;
        }
        return lastAuctionID;
    }

    public static String getDoubleAuctionsDisplaySellers(User user) {
        String returnStr = "";
        for (DoubleAuction temp : availableAuctions.values()) {
            returnStr = returnStr.concat(temp.checkAuctionStatusSeller(user));
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
        for (DoubleAuction temp : availableAuctions.values()) {
            returnStr = returnStr.concat(temp.checkDoubleAuctionBuyer(user));
        }
        if (returnStr.equals("")) {
            return "There are not any available double auctions.";
        }
        return returnStr;
    }

    public static int getDoubleAuctionsItemID(int auctionID) {
        return getDoubleAuction(auctionID).getItemID();
    }

    public static void joinDoubleAuction(int doubleAuctionID, AuctionItem newItem) {
        DoubleAuction doubleAuction = getDoubleAuction(doubleAuctionID);
        doubleAuction.join(newItem);
        beenUpdated = true;
    }

    private static DoubleAuction getDoubleAuction(int doubleAuctionID) {
        return availableAuctions.get(doubleAuctionID);
    }

    public static boolean doesDoubleAuctionExists(int doubleAuctionID) {
        return availableAuctions.get(doubleAuctionID) != null;
    }

    public static boolean bid(int doubleAuctionID, User user, Double biddingAmount) {
        if (doesDoubleAuctionExists(doubleAuctionID)) {
            DoubleAuction theAuction = getDoubleAuction(doubleAuctionID);
            beenUpdated = true;
            return theAuction.addBid(user, biddingAmount);
        }
        return false;
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