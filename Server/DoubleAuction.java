import java.util.LinkedList;

public class DoubleAuction {
    private final int doubleAuctionID;
    private LinkedList<bids> listBids;
    private LinkedList<AuctionItem> listItems;
    private boolean auctionClosed = false;

    final static Double WINNING_BID = -1.0;
    final static Double ALREADY_WINNING_BID = -2.0;
    final static Double INSUFFICIENT_BID = -3.0;

    public DoubleAuction(int doubleAuctionID) {
        this.doubleAuctionID = doubleAuctionID;
        listBids = new LinkedList<bids>();
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

    public int getNumberOfBids() {
        return listBids.size();
    }

    public String closeAuction() {
        if (isAuctionClosed())
            return "The auction is already closed";
        // match highest bid with lowest demanding and so on.
        return "";
    }

    public boolean bid(String username, String email, Double bidAmount) {
        if (isAuctionClosed()) {
            return false;
        }
        listBids.add(new bids(username, email, bidAmount));
        return true;
    }

    private class bids {
        public String username;
        public String email;
        public Double bidAmount;

        public bids(String username, String email, Double bidAmount) {
            this.username = username;
            this.email = email;
            this.bidAmount = bidAmount;
        }
    }

}
