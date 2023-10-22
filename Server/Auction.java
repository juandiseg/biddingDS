import java.util.ArrayList;

public class Auction {

    private int auctionID;
    private AuctionItem item;
    private float startingPrice;
    private float acceptablePrice;
    private ArrayList<bids> listBids;

    public Auction(int auctionID, AuctionItem item, float startingPrice, float acceptablePrice) {
        this.auctionID = auctionID;
        this.item = item;
        this.startingPrice = startingPrice;
        this.acceptablePrice = acceptablePrice;
        listBids = new ArrayList<bids>();
    }

    public int getAuctionID() {
        return auctionID;
    }

    public AuctionItem getItem() {
        return item;
    }

    public float bid(int bidderID, float bidAmount) {
        bids winningBid = null;
        for (bids temp : listBids) {
            if (winningBid == null || winningBid.bidAmount < temp.bidAmount)
                winningBid = temp;
        }
        if (winningBid != null) {
            if (winningBid.bidderID == bidderID)
                return -2;
            else if (winningBid.bidAmount < bidAmount) {
                listBids.add(new bids(bidderID, bidAmount));
                return -1;
            } else
                return winningBid.bidAmount;

        }
        listBids.add(new bids(bidderID, bidAmount));
        return -1;
    }

    private class bids {
        public int bidderID;
        public float bidAmount;

        public bids(int bidderID, float bidAmount) {
            this.bidderID = bidderID;
            this.bidAmount = bidAmount;
        }
    }

}
