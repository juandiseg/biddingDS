import java.rmi.RemoteException;
import java.rmi.Remote;

public interface iUser extends Remote {
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException;

    public float bid(int auctionID, String username, String email, float bidAmount) throws RemoteException;

    public int createAuction(int sellerID, String itemTitle, String itemDescription, int itemCondition,
            Float startingPrice, Float acceptablePrice) throws RemoteException;

    public String closeAuction(int userID, int auctionID) throws RemoteException;

    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException;

    public String displayAuctions() throws RemoteException;

}
