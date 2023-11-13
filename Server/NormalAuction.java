
import java.util.ArrayList;

public class NormalAuction {

    final static Double WINNING_BID = -1.0;
    final static Double ALREADY_WINNING_BID = -2.0;
    final static Double INSUFFICIENT_BID = -3.0;
    private AuctionItem item;

    private final int auctionID;
    private final Double reservePrice;
    private ArrayList<Bids> listBids;

    public NormalAuction(int auctionID, AuctionItem item, Double reservePrice) {
        this.auctionID = auctionID;
        this.item = item;
        this.reservePrice = reservePrice;
        listBids = new ArrayList<Bids>();
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

    public String getSellerUsername() {
        return item.getSellerUsername();
    }

    public String closeAuction() {
        if (item.isAuctionClosed())
            return "The auction is already closed";
        item.closeAuction();
        Bids winningBid = getWinningBid();
        String response = "The auction has been CLOSED. ";
        if (winningBid == null)
            return response.concat("No bids were made.");
        if (winningBid.getBidAmount() < reservePrice)
            return response
                    .concat("The reserve price wasn't reached. The highest bid was " + winningBid.getBidAmount()
                            + " EUR.");
        return "The winner {" + winningBid.getUser().getUsername() + "} bidded = "
                + winningBid.getBidAmount() + " EUR. You can contact them at " + winningBid.getUser().getEmail() + ".";
    }

    public AuctionItem getItem() {
        return item;
    }

    public Double bid(User user, Double bidAmount) {
        if (bidAmount < item.getSellingPrice())
            return INSUFFICIENT_BID;
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
        } else
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
