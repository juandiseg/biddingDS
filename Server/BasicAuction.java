import java.util.ArrayList;

public class BasicAuction {

    final static Double WINNING_BID = -1.0;
    final static Double ALREADY_WINNING_BID = -2.0;
    private AuctionItem item;

    private final int auctionID;
    private final Double reservePrice;
    private final Double sellingPrice;
    private boolean winnerApproved = false;
    private ArrayList<Bids> listBids;
    private boolean auctionClosed = false;

    public BasicAuction(int auctionID, AuctionItem item, Double reservePrice, Double sellingPrice) {
        this.auctionID = auctionID;
        this.item = item;
        this.reservePrice = reservePrice;
        this.sellingPrice = sellingPrice;
        listBids = new ArrayList<Bids>();
    }

    // ACTIONS

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

    public String closeAuction() {
        Bids winningBid = getWinningBid();
        if (auctionClosed) {
            return "The auction is already closed";
        }
        String response = "";
        if (winningBid == null) {
            auctionClosed = true;
            return response.concat("No bids were made.\nAuction is CLOSED.");
        }
        if (winningBid.getBidAmount() < reservePrice) {
            auctionClosed = true;
            return response
                    .concat("The reserve price wasn't reached. The highest bid was " + winningBid.getBidAmount()
                            + " EUR.\nAuction is CLOSED.");
        } else if (winningBid.getBidAmount() < sellingPrice) {
            return response
                    .concat("The selling price wasn't reached. The highest bid was " + winningBid.getBidAmount()
                            + " EUR.\nDo you want to grant the item to this bidder?");
        } else {
            auctionClosed = true;
            return generateWinnerConfirmation(winningBid) + "\nAuction is CLOSED.";
        }
    }

    // STRING METHODS

    // SUMMARY

    public String generateDisplay() {
        String buildStr = "----------------------------------------------------\n";
        buildStr = buildStr.concat("Auction ID #" + getAuctionID() + ": \n");
        buildStr = buildStr.concat("- - - - - - - - - - - - - - - \n");
        buildStr = buildStr.concat("Item\t| ID\t= " + getItem().getItemId() + "\n");
        buildStr = buildStr.concat("\t| Title\t= " + getItem().getItemTitle() + "\n");
        buildStr = buildStr.concat("\t| Description\t= " + getItem().getItemDescription() + "\n");
        buildStr = buildStr.concat("\t| Condition\t= " + getItem().getItemCondition() + "\n");
        Double winningBid = getWinningBidAmount();
        if (winningBid > 0) {
            buildStr = buildStr.concat("Highest bid\t\t| " + winningBid + " EUR.\n\n");
        } else {
            buildStr = buildStr.concat("Highest bid\t\t| There are no bids.\n\n");
        }
        return buildStr.concat("----------------------------------------------------\n\n");

    }

    // WINNER CONFIRMATION

    public String generateWinnerConfirmation() {
        Bids winningBid = getWinningBid();
        return generateWinnerConfirmation(winningBid);
    }

    private String generateWinnerConfirmation(Bids winningBid) {
        return "The winner {" + winningBid.getUser().getUsername() + "} bidded = "
                + winningBid.getBidAmount() + " EUR. You can contact them at " + winningBid.getUser().getEmail()
                + ".";
    }

    // GET SELLER STATUS
    public String generateSellerStatus() {
        String status = "";
        if (isAuctionClosed())
            status = "This auction is already CLOSED.\n";

        status = status.concat(generateSellerBidsStatus());
        Double winningBid = getWinningBidAmount();

        if (isClosedAndHighestBidBetweenReserveAndSellingPrices()) {
            return status.concat(generateWinnerApprovalStatus(winningBid));
        }
        status = status.concat(generateSellerWinningBidStatus(winningBid));
        status = status.concat(generateReservePriceStatus(winningBid));
        status = status.concat(generateSellingPriceStatus(winningBid));
        return status;
    }

    private String generateSellerBidsStatus() {
        int numberBids = getNumberOfBids();
        if (numberBids == 0) {
            return "There aren't any bids.\n";
        } else {
            return "There are " + numberBids + " bids.\n";
        }
    }

    private boolean isClosedAndHighestBidBetweenReserveAndSellingPrices() {
        Double winningBid = getWinningBidAmount();
        return winningBid >= reservePrice && winningBid < sellingPrice && isAuctionClosed();
    }

    private String generateWinnerApprovalStatus(Double winningBid) {
        if (winnerApproved) {
            return "The reserve price of " + reservePrice
                    + " EUR was reached, and a winner granted was granted with a bid for " + winningBid + " EUR.";
        } else {
            return "The reserve price of " + reservePrice + " EUR was reached, but the selling price of " + sellingPrice
                    + " was not. The highest bid was for " + winningBid + " EUR, but it was not granted a win.";
        }
    }

    private String generateSellerWinningBidStatus(Double winningBid) {
        int numberBids = getNumberOfBids();
        if (numberBids != 0) {
            return "The winning bid is for " + winningBid + " EUR. ";
        } else {
            return "";
        }
    }

    private String generateReservePriceStatus(Double winningBid) {
        if (winningBid >= reservePrice) {
            return "The reserve price of " + reservePrice + " EUR has been reached.";
        } else {
            return "The reserve price of " + reservePrice + " EUR has NOT been reached.";
        }
    }

    private String generateSellingPriceStatus(Double winningBid) {
        if (winningBid >= sellingPrice) {
            return "The selling price of " + sellingPrice + " EUR has been reached.";
        } else {
            return "The selling price of " + sellingPrice + " EUR has NOT been reached.";
        }
    }

    // GET BUYER STATUS
    public String generateBuyerStatus(User user) {
        String status = "";
        if (isAuctionClosed())
            status = "This auction has already been closed.\n";
        Double winningBid = getWinningBidAmount();
        status = status.concat(generateBidStatusBuyer(winningBid));
        status = status.concat(generateBuyerSpecificResult(user, winningBid));
        return status;
    }

    private String generateBidStatusBuyer(Double winningBid) {
        if (winningBid == -1.0) {
            return "There are no bids for this auction.";
        } else {
            return "The currently winning bid goes for " + winningBid + "EUR.";
        }
    }

    private String generateBuyerSpecificResult(User user, Double winningBid) {
        String winningUsername = getWinningBidUsername();
        if (winningUsername.equals(user.getUsername())) {
            return "You were the winning bid, with a " + winningBid
                    + "EUR bid. The seller will soon get in contact with you.";
        } else {
            return "Someone else won the bid with a " + winningBid + "EUR bid.";
        }
    }

    // SETTERS AND GETTERS

    // GETTERS
    public boolean isAuctionClosed() {
        return auctionClosed;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public AuctionItem getItem() {
        return item;
    }

    public Double getWinningBidAmount() {
        if (listBids.isEmpty())
            return -1.0;
        return listBids.get(listBids.size() - 1).getBidAmount();
    }

    private String getWinningBidUsername() {
        if (listBids.isEmpty())
            return "";
        return listBids.get(listBids.size() - 1).getUser().getUsername();
    }

    private Bids getWinningBid() {
        if (listBids.isEmpty())
            return null;
        return listBids.get(listBids.size() - 1);
    }

    public Double getLowestPrice() {
        Double winningBid = getWinningBidAmount();
        if (winningBid == -1) {
            return 0.0;
        } else {
            return winningBid;
        }
    }

    private int getNumberOfBids() {
        return listBids.size();
    }

    public User getSeller() {
        return item.getSeller();
    }

    // SETTER
    public void setWinnerApproved(boolean approvation) {
        winnerApproved = approvation;
        auctionClosed = true;
    }
}