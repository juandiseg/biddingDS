import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicAuctionManager {

    private final static Double DOESNT_EXIST = 0.0;
    private final static Double AUCTION_CLOSED = -4.0;
    private static int lastAuctionID = 0;
    private static HashMap<Integer, BasicAuction> availableAuctions = ServerReplication.getBasicAuctionState();
    private static HashMap<Integer, BasicAuction> unsynchronized = new HashMap<Integer, BasicAuction>();

    public static synchronized HashMap<Integer, BasicAuction> getUnsyncronizedAuctions() {
        return BasicAuctionManager.unsynchronized;
    }

    public static synchronized void cleanUnsynchronizedAuctions() {
        BasicAuctionManager.unsynchronized.clear();
    }

    // ACTIONS
    public static Double bid(int auctionID, User user, Double biddingAmount) {
        BasicAuction theAuction = get(auctionID);
        if (theAuction == null)
            return DOESNT_EXIST;
        if (theAuction.isAuctionClosed())
            return AUCTION_CLOSED;
        return theAuction.bid(user, biddingAmount);
    }

    public static String getStatusSeller(User user, int auctionID) {
        BasicAuction theAuction = get(auctionID);
        if (theAuction == null) {
            return "The given auction ID does not exist";
        } else if (!theAuction.getSeller().equals(user)) {
            return "You don't have access to this auction's status.";
        }
        return theAuction.generateSellerStatus();
    }

    public static String getStatusBuyer(User user, int auctionID) {
        return get(auctionID).generateBuyerStatus(user);
    }

    public static String getReversedDisplay(int itemID) {
        BasicAuction[] listAuctions = getLowestbids(itemID, 5);
        String reverseAuctions = "Five auctions with the currently lowest bids for the item #" + itemID + ":\n\n";
        for (int i = 0; i < listAuctions.length; i++) {
            if (listAuctions[i] != null) {
                reverseAuctions = reverseAuctions.concat(listAuctions[i].generateDisplay());
            }
        }
        return reverseAuctions;
    }

    private static BasicAuction[] getLowestbids(int itemID, int limitBids) {
        BasicAuction[] listAuctions = new BasicAuction[limitBids];
        for (BasicAuction temp : availableAuctions.values()) {
            if (temp.getItem().getID() == itemID) {
                addLowerBid(listAuctions, temp);
            }
        }
        sortAuctionsDecreasing(listAuctions);
        return listAuctions;
    }

    private static void sortAuctionsDecreasing(BasicAuction[] listAuctions) {
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

    private static void addLowerBid(BasicAuction[] listAuctions, BasicAuction temp) {
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

    public static String getDisplayAvailableItems() {
        return ITEMS.getDisplayAvailable();
    }

    private static boolean itemReferenceExists(int itemID) {
        return ITEMS.getItems().get(itemID) != null;
    }

    public static int add(AuctionItem item, double reservePrice) {
        if (!itemReferenceExists(item.getID())) {
            return -1;
        }
        int auctionID = generateID();
        BasicAuction newAuction = new BasicAuction(auctionID, item, reservePrice, item.getSellingPrice());
        availableAuctions.put(auctionID, newAuction);
        unsynchronized.put(auctionID, newAuction);
        return newAuction.getID();
    }

    public static String getDisplay(int itemID) {
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

    public static String getItemsDisplay() {
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

    public static HashMap<Integer, AuctionItem> getSpec(User user, int itemID) {
        HashMap<Integer, AuctionItem> map = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            for (BasicAuction temp : availableAuctions.values().stream().filter(t -> (t.getItem().getID() == itemID))
                    .collect(Collectors.toList())) {
                map.put(temp.getID(), temp.getItem());
            }
        }
        return map;
    }

    private static int generateID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    private static BasicAuction get(int auctionID) {
        for (BasicAuction temp : availableAuctions.values()) {
            if (temp.getID() == auctionID)
                return temp;
        }
        return null;
    }

    public static String close(User user, int auctionID) {
        BasicAuction theAuction = get(auctionID);
        if (theAuction == null)
            return "There are no auctions with the specified ID.";
        if (theAuction.getSeller().equals(user))
            return theAuction.closeAuction();
        return "You don't have access to this editting this auction.";
    }

    public static String closeAndApproveWinner(int auctionID, boolean isApproved) {
        BasicAuction theAuction = get(auctionID);
        if (isApproved) {
            theAuction.setWinnerApproved(true);
            return theAuction.generateWinnerConfirmation();
        } else {
            theAuction.setWinnerApproved(false);
            return "Auction is CLOSED. There were no winners.";
        }
    }
}
