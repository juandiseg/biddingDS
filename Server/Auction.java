import java.util.ArrayList;

public class Auction {

    private final int sellerID;
    private final int auctionID;
    private AuctionItem item;
    private float startingPrice;
    private float reservePrice;
    private ArrayList<bids> listBids;
    private boolean auctionClosed = false;

    public Auction(int sellerID, int auctionID, AuctionItem item, float startingPrice, float reservePrice) {
        this.sellerID = sellerID;
        this.auctionID = auctionID;
        this.item = item;
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        listBids = new ArrayList<bids>();
    }

    public int getAuctionID() {
        return auctionID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public float getStartingPrice() {
        return startingPrice;
    }

    public String closeAuction() {
        if (auctionClosed)
            return "The auction is already closed";
        auctionClosed = true;
        bids winningBid = getWinningBid();
        String response = "The auction has been CLOSED. ";
        if (winningBid == null)
            return response.concat("No bids were made.");
        if (winningBid.bidAmount < reservePrice)
            return response
                    .concat("The reserve price wasn't reached. The highest bid was " + winningBid.bidAmount + "€.");
        return "The winner {" + winningBid.username + "} bidded = "
                + winningBid.bidAmount + "€. You can contact them at " + winningBid.email + ".";
    }

    public AuctionItem getItem() {
        return item;
    }

    public float bid(String username, String email, float bidAmount) {
        if (bidAmount < startingPrice)
            return -3;
        bids winningBid = getWinningBid();
        if (winningBid == null) {
            listBids.add(new bids(username, email, bidAmount));
            return -1;
        }
        if (winningBid.username == username)
            return -2;
        else if (winningBid.bidAmount < bidAmount) {
            listBids.add(new bids(username, email, bidAmount));
            return -1;
        } else
            return winningBid.bidAmount;
    }

    public float getWinningBidAmount() {
        if (listBids.isEmpty())
            return -1;
        return listBids.get(listBids.size() - 1).bidAmount;
    }

    private bids getWinningBid() {
        if (listBids.isEmpty())
            return null;
        return listBids.get(listBids.size() - 1);
    }

    private class bids {
        public String username;
        public String email;
        public float bidAmount;

        public bids(String username, String email, float bidAmount) {
            this.username = username;
            this.email = email;
            this.bidAmount = bidAmount;
        }
    }

}
