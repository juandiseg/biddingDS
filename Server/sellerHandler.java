import java.rmi.RemoteException;

public class sellerHandler implements iSeller {

    @Override
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException {
        AuctionItem item = AuctionManager.getAuctionItem(itemId);
        if (item == null)
            return null; // Create default object.
        return item;
    }

    @Override
    public int createAuction(int sellerID, String itemTitle, String itemDescription, int itemCondition,
            Float startingPrice, Float acceptablePrice) throws RemoteException {
        int auctionID = AuctionManager.generateAuctionID();
        AuctionManager.addAuction(new Auction(sellerID, auctionID,
                new AuctionItem(AuctionManager.generateItemID(), itemTitle, itemDescription, itemCondition),
                startingPrice, acceptablePrice));
        return auctionID;
    }

    @Override
    public String closeAuction(int userID, int auctionID) throws RemoteException {
        return AuctionManager.closeAuction(userID, auctionID);
    }

    @Override
    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException {
        return AuctionManager.checkAuctionStatus(userID, auctionID);
    }

}
