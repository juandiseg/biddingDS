import java.util.ArrayList;

public class Auction {

    final static Double WINNING_BID = -1.0;
    final static Double ALREADY_WINNING_BID = -2.0;
    final static Double INSUFFICIENT_BID = -3.0;

    private final int auctionID;
    private AuctionItem item;
    private final Double startingPrice;
    private final Double reservePrice;
    // private float sellingPrice;
    private ArrayList<bids> listBids;

    public Auction(int auctionID, AuctionItem item, Double startingPrice, Double reservePrice) {
        this.auctionID = auctionID;
        this.item = item;
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        listBids = new ArrayList<bids>();
    }

    public int getAuctionID() {
        return auctionID;
    }

    public boolean isAuctionClosed() {
        return item.isAuctionClosed();
    }

    public Double getReservePrice() {
        return reservePrice;
    }

    public int getNumberOfBids() {
        return listBids.size();
    }

    public int getSellerID() {
        return item.getSellerID();
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public String closeAuction() {
        if (item.isAuctionClosed())
            return "The auction is already closed";
        item.closeAuction();
        bids winningBid = getWinningBid();
        String response = "The auction has been CLOSED. ";
        if (winningBid == null)
            return response.concat("No bids were made.");
        if (winningBid.bidAmount < reservePrice)
            return response
                    .concat("The reserve price wasn't reached. The highest bid was " + winningBid.bidAmount + " EUR.");
        return "The winner {" + winningBid.username + "} bidded = "
                + winningBid.bidAmount + " EUR. You can contact them at " + winningBid.email + ".";
    }

    public AuctionItem getItem() {
        return item;
    }

    public Double bid(String username, String email, Double bidAmount) {
        if (bidAmount < startingPrice)
            return INSUFFICIENT_BID;
        bids winningBid = getWinningBid();
        if (winningBid == null) {
            listBids.add(new bids(username, email, bidAmount));
            return WINNING_BID;
        }
        if (winningBid.username == username)
            return ALREADY_WINNING_BID;
        else if (winningBid.bidAmount < bidAmount) {
            listBids.add(new bids(username, email, bidAmount));
            return WINNING_BID;
        } else
            return winningBid.bidAmount;
    }

    public Double getWinningBidAmount() {
        if (listBids.isEmpty())
            return -1.0;
        return listBids.get(listBids.size() - 1).bidAmount;
    }

    public String getWinningBidUsername() {
        if (listBids.isEmpty())
            return "";
        return listBids.get(listBids.size() - 1).username;
    }

    private bids getWinningBid() {
        if (listBids.isEmpty())
            return null;
        return listBids.get(listBids.size() - 1);
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
