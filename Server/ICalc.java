import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICalc extends Remote {
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException;

    public int createListing(String itemTitle, String itemDescription, int itemCondition, Float startingPrice,
            Float acceptablePrice) throws RemoteException;

    public float bid(int userID, int auctionID, float bidAmount) throws RemoteException;

}
