import java.util.ArrayList;
import java.util.HashMap;

public class DoubleAuctionManager {

    private HashMap<Integer, DoubleAuction> availableAuctions = ServerReplication.getDoubleAuctionState();
    private ArrayList<MethodCaller> RPCallsToMake = new ArrayList<>();
    private final String className = "DoubleAuctionManager";
    private int lastAuctionID = 0;

    // ACTIONS + aux methods

    public int createDoubleAuction(boolean makeCall, int itemID, int limitSellers, int limitBids) {
        if (makeCall) {
            Object[] args = { false, itemID, limitSellers, limitBids };
            RPCallsToMake.add(new MethodCaller(className, "createDoubleAuction", args));
        }
        if (itemReferenceExists(itemID)) {
            return -1;
        }
        int auctionID = generateID();
        DoubleAuction temp = new DoubleAuction(auctionID, auctionID, limitSellers, limitBids);
        availableAuctions.put(auctionID, temp);
        return auctionID;
    }

    private boolean itemReferenceExists(int itemID) {
        return ITEMS.getItems().get(itemID) != null;
    }

    private int generateID() {
        while (lastAuctionID <= availableAuctions.size()) {
            lastAuctionID++;
        }
        return lastAuctionID;
    }

    public void joinDoubleAuction(boolean makeCall, int doubleAuctionID, AuctionItem newItem) {
        if (makeCall) {
            Object[] args = { false, doubleAuctionID, newItem };
            RPCallsToMake.add(new MethodCaller(className, "joinDoubleAuction", args));
        }
        DoubleAuction doubleAuction = getDoubleAuction(doubleAuctionID);
        doubleAuction.join(newItem);
    }

    public boolean bid(boolean makeCall, int doubleAuctionID, User user, Double biddingAmount) {
        if (makeCall) {
            Object[] args = { false, doubleAuctionID, user, biddingAmount };
            RPCallsToMake.add(new MethodCaller(className, "bid", args));
        }
        if (doesDoubleAuctionExists(doubleAuctionID)) {
            DoubleAuction theAuction = getDoubleAuction(doubleAuctionID);
            boolean result = theAuction.addBid(user, biddingAmount);
            return result;
        }
        return false;
    }

    // DISPLAY

    public String getDoubleAuctionsDisplaySellers(User user) {
        String returnStr = "";
        for (DoubleAuction temp : availableAuctions.values()) {
            returnStr = returnStr.concat(temp.checkAuctionStatusSeller(user));
        }
        if (returnStr.equals("")) {
            return "There are not any available double auctions.";
        }
        return returnStr;
    }

    public String getDoubleAuctionsDisplayBuyers(User user) {
        String returnStr = "";
        for (DoubleAuction temp : availableAuctions.values()) {
            returnStr = returnStr.concat(temp.checkDoubleAuctionBuyer(user));
        }
        if (returnStr.equals("")) {
            return "There are not any available double auctions.";
        }
        return returnStr;
    }

    public String getResolutionBuyer(User user, int auctionID) {
        if (!doesDoubleAuctionExists(auctionID)) {
            return "The given double auction ID does not exist.";
        }
        return getDoubleAuction(auctionID).getResolutionBuyer(user);
    }

    public String getResolutionSeller(User user, int auctionID) {
        if (!doesDoubleAuctionExists(auctionID)) {
            return "The given double auction ID does not exist.";
        }
        return getDoubleAuction(auctionID).getResolutionSeller(user);
    }

    // GETTERS

    public int getDoubleAuctionsItemID(int auctionID) {
        return getDoubleAuction(auctionID).getItemID();
    }

    private DoubleAuction getDoubleAuction(int doubleAuctionID) {
        return availableAuctions.get(doubleAuctionID);
    }

    // CHECKS

    public boolean doesDoubleAuctionExists(int doubleAuctionID) {
        return availableAuctions.get(doubleAuctionID) != null;
    }

    public boolean isSellersLimitReached(int auctionID) {
        return getDoubleAuction(auctionID).isSellersLimitReached();
    }

    // REMOTE PROCEDURE CALLS

    public ArrayList<MethodCaller> getRPCallsToMake() {
        return RPCallsToMake;
    }

    public void emptyRPCallsToMake() {
        RPCallsToMake.clear();
    }

}