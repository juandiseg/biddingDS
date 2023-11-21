
import java.util.ArrayList;

public class NormalAuction {

    final static Double WINNING_BID = -1.0;
    final static Double ALREADY_WINNING_BID = -2.0;
    private AuctionItem item;

    private final int auctionID;
    private final Double reservePrice;
    private final Double sellingPrice;
    private boolean winnerApproved = false;
    private ArrayList<Bids> listBids;

    public NormalAuction(int auctionID, AuctionItem item, Double reservePrice, Double sellingPrice) {
        this.auctionID = auctionID;
        this.item = item;
        this.reservePrice = reservePrice;
        this.sellingPrice = sellingPrice;
        listBids = new ArrayList<Bids>();
    }

    public boolean isClosedAndHighestBidBetweenReserveAndSellingPrices() {
        Double winningBid = getWinningBidAmount();
        return winningBid >= reservePrice && winningBid < sellingPrice && isAuctionClosed();
    }

    public int getAuctionID() {
        return auctionID;
    }

    public boolean isWinnerApproved() {
        return winnerApproved;
    }

    public boolean isAuctionClosed() {
        return item.isAuctionClosed();
    }

    public Double getReservePrice() {
        return reservePrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public Double getLowestPrice() {
        Double winningBid = getWinningBidAmount();
        if (winningBid == -1) {
            return 0.0;
        } else {
            return winningBid;
        }
    }

    public int getNumberOfBids() {
        return listBids.size();
    }

    public String getSellerUsername() {
        return item.getSellerUsername();
    }

    public void setWinnerApproved(boolean approvation) {
        winnerApproved = approvation;
        item.closeAuction();
    }

    public String closeAuction() {
        Bids winningBid = getWinningBid();
        if (item.isAuctionClosed()) {
            return "The auction is already closed";
        }
        String response = "";
        if (winningBid == null) {
            item.closeAuction();
            return response.concat("No bids were made.\nAuction is CLOSED.");
        }
        if (winningBid.getBidAmount() < reservePrice) {
            item.closeAuction();
            return response
                    .concat("The reserve price wasn't reached. The highest bid was " + winningBid.getBidAmount()
                            + " EUR.\nAuction is CLOSED.");
        } else if (winningBid.getBidAmount() < sellingPrice) {
            return response
                    .concat("The selling price wasn't reached. The highest bid was " + winningBid.getBidAmount()
                            + " EUR.\nDo you want to grant the item to this bidder?");
        } else {
            item.closeAuction();
            return getWinnerConfirmation(winningBid) + "\nAuction is CLOSED.";
        }
    }

    public String getWinnerConfirmation() {
        Bids winningBid = getWinningBid();
        return "The winner {" + winningBid.getUser().getUsername() + "} bidded = "
                + winningBid.getBidAmount() + " EUR. You can contact them at " + winningBid.getUser().getEmail()
                + ".";
    }

    private String getWinnerConfirmation(Bids winningBid) {
        return "The winner {" + winningBid.getUser().getUsername() + "} bidded = "
                + winningBid.getBidAmount() + " EUR. You can contact them at " + winningBid.getUser().getEmail()
                + ".";
    }

    public AuctionItem getItem() {
        return item;
    }

    public Double bid(User user, Double bidAmount) {
        Bids winningBid = getWinningBid();
        if (winningBid == null) {
            listBids.add(new Bids(user, bidAmount));
            return WINNING_BID;
        }
        if (winningBid.getUser().getUsername().equals(user.getUsername()))
            return ALREADY_WINNING_BID;
        else if (winningBid.getBidAmount() < bidAmount) {
            listBids.add(new Bids(user, bidAmount));
            return WINNING_BID;
        }
        return winningBid.getBidAmount();
    }

    public Double getWinningBidAmount() {
        if (listBids.isEmpty())
            return -1.0;
        return listBids.get(listBids.size() - 1).getBidAmount();
    }

    public String getWinningBidUsername() {
        if (listBids.isEmpty())
            return "";
        return listBids.get(listBids.size() - 1).getUser().getUsername();
    }

    private Bids getWinningBid() {
        if (listBids.isEmpty())
            return null;
        return listBids.get(listBids.size() - 1);
    }

}
