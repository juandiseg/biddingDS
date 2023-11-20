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

    public static String checkAuctionStatusSeller(User user, int auctionID) {
        boolean result = UserManager.validateUser(user, 'S');
        if (!result) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction.getSellerUsername().equals(user.getUsername())) {
            String status = "";
            if (theAuction.isAuctionClosed())
                status = "This auction is already CLOSED.\n";
            int numberBids = theAuction.getNumberOfBids();
            if (numberBids == 0) {
                status = status.concat("There aren't any bids.\n");
            } else {
                status = status.concat("There are " + numberBids + " bids.\n");
            }
            Double winningBid = theAuction.getWinningBidAmount();
            Double reservePrice = theAuction.getReservePrice();
            Double sellingPrice = theAuction.getSellingPrice();

            if (theAuction.isClosedAndHighestBidBetweenReserveAndSellingPrices()) {
                if (theAuction.isWinnerApproved()) {
                    status = status.concat("The reserve price of " + reservePrice
                            + " EUR was reached, and a winner granted was granted with a bid for " + winningBid
                            + " EUR.");
                    return status;
                }
                if (!theAuction.isWinnerApproved()) {
                    status = status.concat("The reserve price of " + reservePrice
                            + " EUR was reached, but the selling price of " + sellingPrice
                            + " was not. The highest bid was for " + winningBid
                            + " EUR, but it was not granted a win.");
                    return status;
                }
            }
            if (numberBids != 0) {
                status = status.concat("The winning bid is for " + winningBid + " EUR. ");
            }

            if (winningBid >= reservePrice) {
                status = status.concat("The reserve price of " + reservePrice + " EUR has been reached.");
            } else {
                status = status.concat("The reserve price of " + reservePrice + " EUR has NOT been reached.");
            }
            if (winningBid >= sellingPrice) {
                status = status.concat("The selling price of " + sellingPrice + " EUR has been reached.");
            } else {
                status = status.concat("The selling price of " + sellingPrice + " EUR has NOT been reached.");
            }
            return status;
        }
        return "You don't have access to this auction's status.";
    }

    public static String checkAuctionStatusBuyer(User user, int auctionID) {
        boolean valid = UserManager.validateUser(user, 'B');
        if (!valid) {
            return "You don't have access to this functionality";
        }
        NormalAuction theAuction = getAuction(auctionID);
        String status = "";
        if (theAuction.isAuctionClosed())
            status = "This auction has already been closed.\n";
        Double winningBid = theAuction.getWinningBidAmount();
        String winningUsername = theAuction.getWinningBidUsername();
        if (winningBid == -1.0) {
            status = status.concat("There are no bids for this auction.");
            return status;
        }
        if (winningUsername.equals(user.getUsername())) {
            status = status.concat("You were the winning bid, with a " + winningBid
                    + "EUR bid. The seller will soon get in contact with you.");
        } else {
            status = status.concat("Someone else won the bid with a " + winningBid + "EUR bid.");
        }
        return status;
    }

    public static String viewReverseAuction(int itemID) {
        NormalAuction[] listAuctions = getLowestbids(itemID, 3);
        String reverseAuctions = "Three auctions with the currently lowest bids for the item #" + itemID + ":\n\n";
        for (int i = 0; i < listAuctions.length; i++) {
            reverseAuctions = reverseAuctions.concat("Auction ID #" + listAuctions[i].getAuctionID() + ": \n");
            reverseAuctions = reverseAuctions
                    .concat("Item title\t\t|" + listAuctions[i].getItem().getItemTitle() + "\n");
            reverseAuctions = reverseAuctions
                    .concat("Item description\t\t|" + listAuctions[i].getItem().getItemDescription() + "\n");
            reverseAuctions = reverseAuctions
                    .concat("Item condition\t\t|" + listAuctions[i].getItem().getItemCondition() + "\n");
            reverseAuctions = reverseAuctions
                    .concat("Highest bid\t\t|" + listAuctions[i].getWinningBidAmount() + " EUR.\n\n");
        }
        return reverseAuctions;
    }

    private static NormalAuction[] getLowestbids(int itemID, int limitBids) {
        NormalAuction[] listAuctions = new NormalAuction[limitBids];
        for (int i = 0; i < limitBids; i++) {
            listAuctions[i] = null;
        }
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
                if (listAuctions[i].getWinningBidAmount() < listAuctions[j].getWinningBidAmount()) {
                    if (listAuctions[tempHighest].getWinningBidAmount() < listAuctions[j].getWinningBidAmount()) {
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
            if (listAuctions[highest].getWinningBidAmount() < listAuctions[i].getWinningBidAmount()) {
                highest = i;
            }
        }
        if (listAuctions[highest].getWinningBidAmount() > temp.getWinningBidAmount()) {
            listAuctions[highest] = temp;
        }
    }

    public static HashMap<Integer, String> getAvailableItems() {
        return ITEMS.getItems();
    }

    public static int addAuction(AuctionItem item, double reservePrice) {
        lastAuctionID++;
        NormalAuction newAuction = new NormalAuction(lastAuctionID, item, reservePrice, item.getSellingPrice());
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
                    String tempString = "Auction #" + temp.getAuctionID() + " :\n";
                    tempString = tempString.concat("----------------------------------------------------\n");
                    tempString = tempString.concat("Item\t| ID \t= " + temp.getItem().getItemId() + ".\n");
                    tempString = tempString.concat("\t| Title \t= " + temp.getItem().getItemTitle() + ".\n");
                    tempString = tempString
                            .concat("\t| Description \t= " + temp.getItem().getItemDescription() + ".\n");
                    tempString = tempString.concat("\t| Condition \t= " + temp.getItem().getItemCondition() + ".\n");
                    Double winningBid = temp.getWinningBidAmount();
                    if (winningBid == -1)
                        tempString = tempString.concat("Highest Bid\t= THERE ARE NO BIDS.\n\n");
                    else
                        tempString = tempString.concat("Highest Bid\t= " + winningBid + " EUR.\n");
                    tempString = tempString.concat("----------------------------------------------------\n\n");
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

    public static Double bid(int auctionID, User user, Double biddingAmount) {
        NormalAuction theAuction = getAuction(auctionID);
        if (theAuction == null)
            return DOESNT_EXIST;
        if (theAuction.isAuctionClosed())
            return AUCTION_CLOSED;
        return theAuction.bid(user, biddingAmount);
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

    public static String closeAuctionConfirmation(int auctionID, boolean isApproved) {
        NormalAuction theAuction = getAuction(auctionID);
        if (isApproved) {
            theAuction.setWinnerApproved(true);
            return theAuction.getWinnerConfirmation();
        } else {
            theAuction.setWinnerApproved(false);
            return "Auction is CLOSED. There were no winners.";
        }
    }
}
