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
            Bids temp = listBids[i];
            listBids[i] = listBids[tempLowest];
            listBids[tempLowest] = temp;
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

}
