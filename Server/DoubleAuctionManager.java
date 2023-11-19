
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

    public static boolean bid(int doubleAuctionID, User user, Double biddingAmount) {
        DoubleAuction theAuction = getDoubleAuction(doubleAuctionID);
        return theAuction.addBid(user, biddingAmount);
    }

    public static int generateAuctionID() {
        lastAuctionID++;
        return lastAuctionID;
    }

    public static boolean isSellersLimitReached(int auctionID) {
        return getDoubleAuction(auctionID).isSellersLimitReached();
    }

}
