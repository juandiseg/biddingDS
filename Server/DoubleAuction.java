
import java.util.LinkedList;

public class DoubleAuction {
    private final int doubleAuctionID;
    private final int itemID;
    private LinkedList<Bids> listBids;
    private LinkedList<AuctionItem> listItems;
    private boolean auctionClosed = false;

    public DoubleAuction(int itemID, int doubleAuctionID) {
        this.doubleAuctionID = doubleAuctionID;
        this.itemID = itemID;
        listBids = new LinkedList<Bids>();
        listItems = new LinkedList<AuctionItem>();
    }

    public int getDoubleAuctionID() {
        return doubleAuctionID;
    }

    public boolean isAuctionClosed() {
        if (auctionClosed == false) {
            auctionClosed = listItems.stream().allMatch(item -> item.isAuctionClosed());
        }
        return auctionClosed;
    }

    public boolean isSellerAuctioning(String sellerUsername) {
        return listItems.stream().anyMatch(item -> item.getSellerUsername().equals(sellerUsername));
    }

    public int getNumberOfBids() {
        return listBids.size();
    }

    public int getNumberOfSellers() {
        return listItems.size();
    }

    public int getItemID() {
        return this.itemID;
    }

    public String closeAuction(String username) {
        listItems.stream().filter(item -> (item.getSellerUsername().equals(username)))
                .forEach(item -> item.closeAuction());
        if (isAuctionClosed()) {
            // fullyClosedAuction();
            return "The auction is closed, you can check the results using ADD COMMAND HERE";
        }
        return "There are other sellers who haven't yet agreed to close the auction.";
    }

    public boolean addAuction(AuctionItem auctionItem) {
        listItems.add(auctionItem);
        return true;
    }

    public boolean bid(User user, Double bidAmount) {
        if (isAuctionClosed()) {
            return false;
        }
        listBids.add(new Bids(user, bidAmount));
        return true;
    }

}
