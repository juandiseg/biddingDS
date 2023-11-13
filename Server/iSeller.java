
import java.rmi.RemoteException;
import java.util.HashMap;

public interface iSeller extends iUser {
    public HashMap<Integer, AuctionItem> getSpec(User user, int itemId) throws RemoteException;

    public int createAuction(String sellerUsername, int itemID, String itemTitle, String itemDescription,
            int itemCondition, Double reservePrice, Double sellingPrice) throws RemoteException;

    public String closeAuction(User user, int auctionID) throws RemoteException;

    public String checkAuctionStatus(User user, int auctionID) throws RemoteException;

    public String getItemsReferenceID() throws RemoteException;
}
