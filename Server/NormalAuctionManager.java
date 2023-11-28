import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    // ACTIONS
    public static Double bid(int auctionID, User user, Double biddingAmount) {
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return DOESNT_EXIST;
        if (theAuction.isAuctionClosed())
            return AUCTION_CLOSED;
        return theAuction.bid(user, biddingAmount);
    }

    public static String checkAuctionStatusSeller(User user, int auctionID) {
        boolean valid = UserManager.validateUser(user, 'S');
        if (!valid) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        if (!theAuction.getSeller().equals(user)) {
            return "You don't have access to this auction's status.";
        }
        return theAuction.generateSellerStatus();
    }

    public static String checkAuctionStatusBuyer(User user, int auctionID) {
        boolean valid = UserManager.validateUser(user, 'B');
        if (!valid) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        return theAuction.generateBuyerStatus(user);
    }

    public static String viewReverseAuction(int itemID) {
        NormalAuction[] listAuctions = getLowestbids(itemID, 3);
        String reverseAuctions = "Three auctions with the currently lowest bids for the item #" + itemID + ":\n\n";
        for (int i = 0; i < listAuctions.length; i++) {
            if (listAuctions[i] != null) {
                reverseAuctions = reverseAuctions.concat(listAuctions[i].generateNormalAuctionSummary());
            }
        }
        return reverseAuctions;
    }

    private static NormalAuction[] getLowestbids(int itemID, int limitBids) {
        NormalAuction[] listAuctions = new NormalAuction[limitBids];
        for (int i : availableAuctions.keySet()) {
            for (NormalAuction temp : availableAuctions.get(i)) {
                if (temp.getItem().getItemId() == itemID) {
                    addLowerBid(listAuctions, temp);
                }
            }
        }
        sortAuctionsDecreasing(listAuctions);
        return listAuctions;
    }

    private static void sortAuctionsDecreasing(NormalAuction[] listAuctions) {
        for (int i = 0; i < listAuctions.length - 1; i++) {
            int tempHighest = i;
            for (int j = i; j < listAuctions.length; j++) {
                if (listAuctions[j] != null) {
                    if (listAuctions[tempHighest].getLowestPrice() < listAuctions[j].getLowestPrice()) {
                        tempHighest = j;
                    }
                }
            }
            NormalAuction temp = listAuctions[i];
            listAuctions[i] = listAuctions[tempHighest];
            listAuctions[tempHighest] = temp;
        }
    }

    private static void addLowerBid(NormalAuction[] listAuctions, NormalAuction temp) {
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

    public static String getAvailableItems() {
        return ITEMS.getItemsText();
    }

    private static boolean itemReferenceExists(int itemID) {
        return ITEMS.getItems().get(itemID) != null;
    }

    public static int addAuction(AuctionItem item, double reservePrice) {
        if (itemReferenceExists(item.getItemId())) {
            return -1;
        }
        int auctionID = generateAuctionID();
        NormalAuction newAuction = new NormalAuction(auctionID, item, reservePrice, item.getSellingPrice());
        availableAuctions.get(hashFunction(newAuction.getAuctionID())).add(newAuction);
        return newAuction.getAuctionID();
    }

    private static int hashFunction(int auctionID) {
        return auctionID % 5 + 1;
    }

    public static String getAuctionsDisplay(int itemID) {
        String displayString = "";
        for (int i = 1; i < 6; i++) {
            for (NormalAuction temp : availableAuctions.get(i)) {
                if ((temp.getItem().getItemId() == itemID || itemID < 0) && !temp.isAuctionClosed()) {
                    displayString = displayString.concat(temp.generateNormalAuctionSummary());
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
        if (availableAuctions.isEmpty()) {
            return "There are no open auctions.";
        }
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

    private static int generateAuctionID() {
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
        boolean valid = UserManager.validateUser(user, 'S');
        if (!valid) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return "There are no auctions with the specified ID.";
        if (theAuction.getSeller().equals(user))
            return theAuction.closeAuction();
        return "You don't have access to this editting this auction.";
    }

    public static String closeAuctionConfirmation(int auctionID, boolean isApproved) {
        NormalAuction theAuction = getAuction(auctionID);
        if (isApproved) {
            theAuction.setWinnerApproved(true);
            return theAuction.generateWinnerConfirmation();
        } else {
            theAuction.setWinnerApproved(false);
            return "Auction is CLOSED. There were no winners.";
        }
    }
}
