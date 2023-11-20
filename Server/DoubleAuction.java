public class DoubleAuction {
    private final int doubleAuctionID;
    private final int itemID;
    private Bids[] listBids;
    private AuctionItem[] listItems;
    private int lastBidIndex = -1;
    private int lastItemIndex = -1;
    private boolean auctionClosed = false;
    private boolean allItemsListed = false;

    public DoubleAuction(int itemID, int doubleAuctionID, int numberSellers, int numberBids) {
        this.doubleAuctionID = doubleAuctionID;
        this.itemID = itemID;
        listItems = new AuctionItem[numberSellers];
        if (numberBids < numberSellers) {
            numberBids = numberSellers;
        }
        listBids = new Bids[numberBids];
    }

    public boolean isSellersLimitReached() {
        return allItemsListed;
    }

    public int getDoubleAuctionID() {
        return doubleAuctionID;
    }

    public boolean isAuctionClosed() {
        return auctionClosed;
    }

    public boolean isSellerAuctioning(String sellerUsername) {
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i].getSellerUsername().equals(sellerUsername)) {
                return true;
            }
        }
        return false;
    }

    public int getLimitBids() {
        return listBids.length;
    }

    public int getCurrentNumberListings() {
        int countEmpty = 0;
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i] != null) {
                countEmpty++;
            }
        }
        return countEmpty;
    }

    public int getCurrentNumberBids() {
        int countEmpty = 0;
        for (int i = 0; i < listBids.length; i++) {
            if (listBids[i] != null) {
                countEmpty++;
            }
        }
        return countEmpty;
    }

    public int getLimitItems() {
        return listItems.length;
    }

    public int getItemID() {
        return this.itemID;
    }

    private void sortBids() {
        for (int i = 0; i < listBids.length - 1; i++) {
            int tempHighest = i;
            for (int j = i + 1; j < listBids.length; j++) {
                if (listBids[tempHighest].getBidAmount() < listBids[j].getBidAmount()) {
                    tempHighest = j;
                }
            }
            Bids temp = listBids[i];
            listBids[i] = listBids[tempHighest];
            listBids[tempHighest] = temp;
        }
    }

    private void sortItems() {
        for (int i = 0; i < listItems.length - 1; i++) {
            int tempLowest = i;
            for (int j = i + 1; j < listItems.length; j++) {
                if (listItems[tempLowest].getSellingPrice() > listItems[j].getSellingPrice()) {
                    tempLowest = j;
                }
            }
            AuctionItem temp = listItems[i];
            listItems[i] = listItems[tempLowest];
            listItems[tempLowest] = temp;
        }
    }

    public void closeAuction() {
        sortBids();
        sortItems();
        auctionClosed = true;
    }

    public void addAuction(AuctionItem auctionItem) {
        lastItemIndex++;
        listItems[lastItemIndex] = auctionItem;
        if (lastItemIndex == listItems.length - 1) {
            allItemsListed = true;
        }
    }

    public boolean addBid(User user, Double bidAmount) {
        if (auctionClosed || userHasAlreadyBidded(user)) {
            return false;
        } else {
            lastBidIndex++;
            listBids[lastBidIndex] = new Bids(user, bidAmount);
            if (lastBidIndex == listBids.length - 1) {
                closeAuction();
            }
            return true;
        }
    }

    private boolean userHasAlreadyBidded(User user) {
        for (int i = 0; i < listBids.length; i++) {
            if (listBids[i] != null) {
                if (listBids[i].getUser().equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    public double getUsersBid(User user) {
        for (int i = 0; i < listBids.length; i++) {
            if (listBids[i] != null) {
                if (listBids[i].getUser().equals(user)) {
                    return listBids[i].getBidAmount();
                }
            }
        }
        return -1;
    }

    public String getResolutionBuyer(User user) {
        if (!auctionClosed) {
            return "The double auction is not yet closed.";
        }
        int bidIndex = searchBuyerIndex(user);
        if (bidIndex == -1) {
            return "You didn't bid on the specified double auction.";
        } else if (bidIndex > listItems.length - 1
                || listBids[bidIndex].getBidAmount() < listItems[bidIndex].getSellingPrice()) {
            return "Your bid didn't match an item.";
        }
        String retStr = "Your bid MATCHED an item!\n";
        retStr = retStr.concat("Item specification:\n");
        retStr = retStr.concat("Item\t| " + ITEMS.getNameOfItem(listItems[bidIndex].getItemId()) + "\n");
        retStr = retStr.concat("Title\t| " + listItems[bidIndex].getItemTitle() + "\n");
        retStr = retStr.concat("Descr.\t| " + listItems[bidIndex].getItemDescription() + "\n");
        retStr = retStr.concat("State\t| " + listItems[bidIndex].getItemCondition() + "\n");
        retStr = retStr.concat("The seller will SOON get in contact with you!");
        return retStr;
    }

    public String getResolutionSeller(User user) {
        if (!auctionClosed) {
            return "The double auction is not yet closed.";
        }
        int itemIndex = searchSellerIndex(user);
        if (itemIndex == -1) {
            return "You didn't list an item on the specified double auction.";
        } else if (listBids[itemIndex].getBidAmount() < listItems[itemIndex].getSellingPrice()) {
            return "Your listing did NOT MATCH a bid that reached your selling price.";
        }
        String retStr = "Your listing MATCHED a bid!\n";
        retStr = retStr.concat("The matching bid was for " + listBids[itemIndex].getBidAmount() + " EUR.\n");
        retStr = retStr.concat("You can contact the seller (" + listBids[itemIndex].getUser().getUsername() + ") at: "
                + listBids[itemIndex].getUser().getEmail() + "\n");
        return retStr;
    }

    private int searchBuyerIndex(User user) {
        for (int i = 0; i < listBids.length; i++) {
            if (listBids[i].getUser().equals(user)) {
                return i;
            }
        }
        return -1;
    }

    private int searchSellerIndex(User user) {
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i].getSellerUsername().equals(user.getUsername())) {
                return i;
            }
        }
        return -1;
    }

}
