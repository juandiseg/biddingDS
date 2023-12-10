import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicAuctionManager {

    private final static Double DOESNT_EXIST = 0.0;
    private final static Double AUCTION_CLOSED = -4.0;
    private HashMap<Integer, BasicAuction> availableAuctions = ServerReplication.getBasicAuctionState();
    private ArrayList<MethodCaller> RPCallsToMake = new ArrayList<>();
    private final String className = "BasicAuctionManager";
    private int lastAuctionID = 0;

    // ACTIONS + aux methods

    public Double bid(boolean makeCall, int auctionID, User user, Double biddingAmount) {
        if (makeCall) {
            Object[] args = { false, auctionID, user, biddingAmount };
            RPCallsToMake.add(new MethodCaller(className, "bid", args));
        }
        BasicAuction theAuction = availableAuctions.get(auctionID);
        if (theAuction == null) {
            return DOESNT_EXIST;
        } else if (theAuction.isAuctionClosed()) {
            return AUCTION_CLOSED;
        } else {
            Double result = theAuction.bid(user, biddingAmount);
            return result;
        }
    }

    public int add(boolean makeCall, AuctionItem item, double reservePrice) {
        if (makeCall) {
            Object[] args = { false, item, reservePrice };
            RPCallsToMake.add(new MethodCaller(className, "add", args));
        }
        if (!itemReferenceExists(item.getID())) {
            return -1;
        }
        int auctionID = generateID();
        BasicAuction newAuction = new BasicAuction(auctionID, item, reservePrice, item.getSellingPrice());
        availableAuctions.put(auctionID, newAuction);
        return newAuction.getID();
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

    public HashMap<Integer, AuctionItem> getSpec(User user, int itemID) {
        HashMap<Integer, AuctionItem> map = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            for (BasicAuction temp : availableAuctions.values().stream().filter(t -> (t.getItem().getID() == itemID))
                    .collect(Collectors.toList())) {
                map.put(temp.getID(), temp.getItem());
            }
        }
        return map;
    }

    public String close(boolean makeCall, User user, int auctionID) {
        if (makeCall) {
            Object[] args = { false, user, auctionID };
            RPCallsToMake.add(new MethodCaller(className, "close", args));
        }
        BasicAuction theAuction = availableAuctions.get(auctionID);
        if (theAuction == null) {
            return "There are no auctions with the specified ID.";
        } else if (theAuction.getSeller().equals(user)) {
            String result = theAuction.closeAuction();
            return result;
        } else {
            return "You don't have access to this editting this auction.";
        }
    }

    public String closeAndApproveWinner(boolean makeCall, int auctionID, boolean isApproved) {
        if (makeCall) {
            Object[] args = { false, auctionID, isApproved };
            RPCallsToMake.add(new MethodCaller(className, "closeAndApproveWinner", args));
        }
        BasicAuction theAuction = availableAuctions.get(auctionID);
        if (isApproved) {
            theAuction.setWinnerApproved(true);
            String result = theAuction.generateWinnerConfirmation();
            return result;
        } else {
            theAuction.setWinnerApproved(false);
            return "Auction is CLOSED. There were no winners.";
        }
    }

    // DISPLAY + aux methods

    public String getDisplay(int itemID) {
        String displayString = "";
        for (BasicAuction temp : availableAuctions.values()) {
            if ((temp.getItem().getID() == itemID || itemID < 0) && !temp.isAuctionClosed()) {
                displayString = displayString.concat(temp.generateDisplay());
            }
        }
        if (displayString.equals("")) {
            return "There are no open auctions for the specified item";
        }
        return displayString;
    }

    public String getStatusSeller(User user, int auctionID) {
        BasicAuction theAuction = availableAuctions.get(auctionID);
        if (theAuction == null) {
            return "The given auction ID does not exist";
        } else if (!theAuction.getSeller().equals(user)) {
            return "You don't have access to this auction's status.";
        } else {
            return theAuction.generateSellerStatus();
        }
    }

    public String getStatusBuyer(User user, int auctionID) {
        return availableAuctions.get(auctionID).generateBuyerStatus(user);
    }

    public String getReversedDisplay(int itemID) {
        BasicAuction[] listAuctions = getLowestbids(itemID, 5);
        String reverseAuctions = "Five auctions with the currently lowest bids for the item #" + itemID + ":\n\n";
        for (int i = 0; i < listAuctions.length; i++) {
            if (listAuctions[i] != null) {
                reverseAuctions = reverseAuctions.concat(listAuctions[i].generateDisplay());
            }
        }
        return reverseAuctions;
    }

    public String getDisplayAvailableItems() {
        return ITEMS.getDisplayAvailable();
    }

    public String getItemsDisplay() {
        List<Integer> alreadyListed = new LinkedList<Integer>();
        if (availableAuctions.isEmpty()) {
            return "There are no open auctions.";
        }
        String displayString = "The following items have open auctions:\n";
        for (BasicAuction temp : availableAuctions.values()) {
            int itemID = temp.getItem().getID();
            if (!alreadyListed.contains(itemID) && !temp.isAuctionClosed()) {
                displayString = displayString
                        .concat("\tItem #" + itemID + " : " + ITEMS.getItems().get(itemID) + "\n");
                alreadyListed.add(itemID);
            }
        }
        return displayString;
    }

    private BasicAuction[] getLowestbids(int itemID, int limitBids) {
        BasicAuction[] listAuctions = new BasicAuction[limitBids];
        for (BasicAuction temp : availableAuctions.values()) {
            if (temp.getItem().getID() == itemID) {
                addLowerBid(listAuctions, temp);
            }
        }
        sortAuctionsDecreasing(listAuctions);
        return listAuctions;
    }

    private void sortAuctionsDecreasing(BasicAuction[] listAuctions) {
        for (int i = 0; i < listAuctions.length - 1; i++) {
            int tempHighest = i;
            for (int j = i; j < listAuctions.length; j++) {
                if (listAuctions[j] != null) {
                    if (listAuctions[tempHighest].getLowestPrice() < listAuctions[j].getLowestPrice()) {
                        tempHighest = j;
                    }
                }
            }
            BasicAuction temp = listAuctions[i];
            listAuctions[i] = listAuctions[tempHighest];
            listAuctions[tempHighest] = temp;
        }
    }

    private void addLowerBid(BasicAuction[] listAuctions, BasicAuction temp) {
        for (int i = 0; i < listAuctions.length; i++) {
            if (listAuctions[i] == null) {
                listAuctions[i] = temp;
                return;
            }
        }
        int highest = 0;

        for (int i = 1; i < listAuctions.length; i++) {
            if (listAuctions[highest].getLowestPrice() < listAuctions[i].getLowestPrice()) {
                highest = i;
            }
        }
        if (listAuctions[highest].getLowestPrice() > temp.getLowestPrice()) {
            listAuctions[highest] = temp;
        }
    }

    // REMOTE PROCEDURE CALLS
    public ArrayList<MethodCaller> getRPCallsToMake() {
        return RPCallsToMake;
    }

    public void emptyRPCallsToMake() {
        RPCallsToMake.clear();
    }

}
