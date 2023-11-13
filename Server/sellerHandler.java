import java.rmi.RemoteException;
import java.util.HashMap;

public class sellerHandler implements iSeller {

    @Override
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException {
        AuctionItem item = AuctionManager.getAuctionItem(itemId);
        if (item == null)
            return null; // Create default object.
        return item;
    }

    @Override
    public int createAuction(int sellerID, int itemID, String itemTitle, String itemDescription, int itemCondition,
            Double startingPrice, Double acceptablePrice) throws RemoteException {
        int auctionID = AuctionManager.generateAuctionID();
        if (AuctionManager.getAvailableItems().get(itemID) == null) {
            return -1;
        }
        AuctionManager.addAuction(new Auction(sellerID, auctionID,
                new AuctionItem(itemID, itemTitle, itemDescription, itemCondition),
                startingPrice, acceptablePrice));
        return auctionID;
    }

    @Override
    public String closeAuction(int userID, int auctionID) throws RemoteException {
        return AuctionManager.closeAuction(userID, auctionID);
    }

    @Override
    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException {
        return AuctionManager.checkAuctionStatusSeller(userID, auctionID);
    }

    @Override
    public String getItemsReferenceID() throws RemoteException {
        HashMap<Integer, String> items = AuctionManager.getAvailableItems();
        String text = "";
        for (int i = 1; i < items.size() + 1; i++) {
            text = text.concat("The item #" + i + " references the product: \"" + items.get(i) + "\".\n");
        }
        return text;
    }

}
