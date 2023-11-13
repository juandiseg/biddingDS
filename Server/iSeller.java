import java.rmi.RemoteException;
import java.rmi.Remote;

public interface iSeller extends Remote {
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException;

    public int createAuction(int sellerID, int itemID, String itemTitle, String itemDescription, int itemCondition,
            Double startingPrice, Double acceptablePrice) throws RemoteException;

    public String closeAuction(int userID, int auctionID) throws RemoteException;

    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException;

    public String getItemsReferenceID() throws RemoteException;
}
